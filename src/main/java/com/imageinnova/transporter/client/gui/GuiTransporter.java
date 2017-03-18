package com.imageinnova.transporter.client.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imageinnova.transporter.Transporter;
import com.imageinnova.transporter.inventory.guicontainer.ContainerTransporter;
import com.imageinnova.transporter.network.MessageTransport;
import com.imageinnova.transporter.tileentities.TileEntityTransporter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GuiTransporter extends GuiContainer {
	final static int MAP_X = 91;
	final static int MAP_Y = 40;
	final static int MAP_WIDTH = 91;
	final static int MAP_HEIGHT = 91;
	final static int TRANSPORTER_ICON_X = 200;
	final static int TRANSPORTER_ICON_Y = 0;
	final static int TRANSPORTER_WIDTH = 8;
	final static int TRANSPORTER_HEIGHT = 8;
	final static int TRANSPORTER_SEL_Y = 16;
	final static int MAP_CENTER_X = 137;
	final static int MAP_CENTER_Y = 84;
	final static int MAP_RADIUS = 42;

	private GuiButton go;
	private TileEntityTransporter te;
	private HashMap<Point, BlockPos> transporterList = new HashMap<Point, BlockPos>();
	private BlockPos dest;	
	
	public GuiTransporter(IInventory playerInv, TileEntityTransporter te) {
        super(new ContainerTransporter(playerInv, te));
        
        this.te = te;
        
		this.xSize = 197;
		this.ySize = 225;
		
		this.dest = null;
    }

	@Override
	public void initGui() {
		super.initGui();
	    this.buttonList.add(this.go = new GuiButton(0, this.guiLeft + 5, this.guiTop + 111, 58, 20, "Fly Away"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.go) {	//really, it's the only button right now
			EntityPlayer player = Minecraft.getMinecraft().player;
			BlockPos pos = null;
			
			// If the transporter is properly fueled, send the player. 
			// Otherwise, drop the item
			IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (te.isItemValidForSlot(te.INPUT_SLOT, itemHandler.getStackInSlot(te.INPUT_SLOT))) {
				// The player chose a destination off the map
				if (dest != null) {
					pos = dest.add(0, 1, 0);
				}
				// No transporter selected; set coordinates for 100m from current position 
				// based on direction of player
				else {	
					// North = negative z
					// East = positive x
					// South = positive z
					// West = negative x
					//
					// x -= r sin(T)
					// z += r cos(T)
					int yaw = Math.floorMod((int) player.rotationYawHead, 360);
					double deltaX = -Transporter.TRANSPORTER_DISTANCE * Math.sin(Math.toRadians(yaw));
					double deltaZ = Transporter.TRANSPORTER_DISTANCE * Math.cos(Math.toRadians(yaw));
					
					// Is the new location a safe place, i.e. no collision
					pos = player.getPosition();
					pos = pos.add(deltaX, 0, deltaZ);
					BlockPos posAbove = pos.add(0, 1, 0);
					while (player.world.getBlockState(pos).getBlock() != Blocks.AIR || player.world.getBlockState(posAbove).getBlock() != Blocks.AIR) {
						pos = pos.add(0, 1, 0);
						posAbove = pos.add(0, 1, 0);
					}
					// make sure the player isn't standing on air, either
					BlockPos posBelow = pos.add(0, -1, 0);
					while (player.world.getBlockState(posBelow).getBlock() == Blocks.AIR) {
						pos = posBelow;
						posBelow = posBelow.add(0, -1, 0);
					}
				}
				// send the request to the server to execute
				Transporter.network.sendToServer(new MessageTransport(pos));

				// close the GUI
				this.mc.displayGuiScreen(null);
				if (this.mc.currentScreen == null) {
					this.mc.setIngameFocus();
				}
			}
		}
		else {
			super.actionPerformed(button);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	    this.mc.getTextureManager().bindTexture(new ResourceLocation("transporter:textures/gui/containers/transporter_gui.png"));
	    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		// get the list of all tile entities, then pick out the transporters and  
		// plot the ones that are in the local region
		List<TileEntity> list = te.getWorld().loadedTileEntityList;
		transporterList.clear();
		for (TileEntity item : list) {
			if (item instanceof TileEntityTransporter && item != te) { // exclude this transporter
				// calculate x and y as relative to this position, scaled to map size
				int x = (item.getPos().getX() - te.getPos().getX()) * MAP_RADIUS / Transporter.TRANSPORTER_DISTANCE;
				int y = (item.getPos().getZ() - te.getPos().getZ()) * MAP_RADIUS / Transporter.TRANSPORTER_DISTANCE;
				if (Math.abs(x) < MAP_RADIUS && Math.abs(y) < MAP_RADIUS) {
					x += guiLeft + MAP_CENTER_X - TRANSPORTER_WIDTH / 2;
					y += guiTop + MAP_CENTER_Y - TRANSPORTER_HEIGHT / 2;
					transporterList.put(new Point(x, y), item.getPos());
					int iconY = (this.dest != null && this.dest.equals(item.getPos())) ? TRANSPORTER_SEL_Y : TRANSPORTER_ICON_Y;
					drawTexturedModalRect(x, y, TRANSPORTER_ICON_X, iconY, TRANSPORTER_WIDTH, TRANSPORTER_HEIGHT);
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final int LABEL_XPOS = 5;
		final int LABEL_YPOS = 5;
		fontRendererObj.drawString(te.getDisplayName().getUnformattedText(), LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		this.go.enabled = te.isItemValidForSlot(te.INPUT_SLOT, itemHandler.getStackInSlot(te.INPUT_SLOT));
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		Point p = new Point(mouseX, mouseY);
		Rectangle map = new Rectangle(MAP_X + guiLeft, MAP_Y + guiTop, MAP_WIDTH, MAP_HEIGHT);

		if (map.contains(p.x, p.y)) {
			for (Map.Entry<Point, BlockPos> entry : transporterList.entrySet()) {
				Rectangle r = new Rectangle(entry.getKey().x, entry.getKey().y, TRANSPORTER_WIDTH, TRANSPORTER_HEIGHT);
				if (r.contains(p.x, p.y)) {
					// toggle/switch to desired transporter
					this.dest = this.dest == null || !this.dest.equals(entry.getValue()) ? entry.getValue() : null;
					break;
				}
			}
		}
		else {
			try {	// not inside the map, do the default
				super.mouseClicked(mouseX, mouseY, mouseButton);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
