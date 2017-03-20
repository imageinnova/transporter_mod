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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiSlider;
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
	final static int SLIDER_MIN = 100;
	final static int SLIDER_MAX = Transporter.MAX_TRANSPORTER_DISTANCE;

	private GuiButton go;
	private GuiSlider distSlider;
	private TileEntityTransporter te;
	private HashMap<Point, BlockPos> transporterList = new HashMap<Point, BlockPos>();
	private BlockPos dest;	
	
	public GuiTransporter(IInventory playerInv, TileEntityTransporter te) {
        super(new ContainerTransporter(playerInv, te));
        
        this.te = te;
        
		this.xSize = 187;
		this.ySize = 225;
		
		this.dest = null;
    }

	@Override
	public void initGui() {
		super.initGui();
	    buttonList.add(go = new GuiButton(0, this.guiLeft + 5, this.guiTop + 111, 58, 20, "Fly Away"));
	    buttonList.add(distSlider = new GuiSlider(1, this.guiLeft + 5, this.guiTop + 82, 80, 20, "", "", SLIDER_MIN, SLIDER_MAX, 100, false, true));
	}
	
	private int getDist() {
		return Math.round((float) distSlider.sliderValue * (SLIDER_MAX - SLIDER_MIN)) + SLIDER_MIN;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		if (button == this.go) {	// user chose to launch
			EntityPlayer player = Minecraft.getMinecraft().player;
			BlockPos pos = null;
			
			// If the transporter is properly fueled, send the player. 
			IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (te.isItemValidForSlot(te.INPUT_SLOT, itemHandler.getStackInSlot(te.INPUT_SLOT))) {
				// The player chose a destination off the map
				if (dest != null) {
					pos = dest.add(0, 1, 0);
				}
				// No transporter selected; set coordinates for selected distance from  
				// current position based on direction of player
				else {	
					// North = negative z
					// East = positive x
					// South = positive z
					// West = negative x
					//
					// x -= r sin(T)
					// z += r cos(T)
					int yaw = Math.floorMod((int) player.rotationYawHead, 360);
					double deltaX = -getDist() * Math.sin(Math.toRadians(yaw));
					double deltaZ = getDist() * Math.cos(Math.toRadians(yaw));
					
					// Transport the player on the horizontal plane, then move up or down
					// as necessary to ensure the player won't be suffocated or transport
					// into thin air
					pos = player.getPosition();
					pos = pos.add(deltaX, 0, deltaZ);
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
			if (item instanceof TileEntityTransporter && !item.equals(te)) { // exclude this transporter
				// calculate x and y as relative to this position, scaled to map size
				int x = (item.getPos().getX() - te.getPos().getX()) * MAP_RADIUS / Transporter.MAX_TRANSPORTER_DISTANCE;
				int y = (item.getPos().getZ() - te.getPos().getZ()) * MAP_RADIUS / Transporter.MAX_TRANSPORTER_DISTANCE;
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
		int fuelNeeded;
		if (dest == null) {
			fuelNeeded = Math.round((float)getDist() / 100);
		}
		else {
			fuelNeeded = Math.round((float) dest.getDistance(te.getPos().getX(), dest.getY(), te.getPos().getZ()) / 100);
		}
		ItemStack fuelStack = itemHandler.getStackInSlot(te.INPUT_SLOT);
		this.go.enabled = te.isItemValidForSlot(te.INPUT_SLOT, fuelStack) && fuelStack.getCount() >= fuelNeeded;
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
