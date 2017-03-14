package com.imageinnova.transporter.network;

import com.imageinnova.transporter.client.gui.GuiTransporter;
import com.imageinnova.transporter.inventory.guicontainer.ContainerTransporter;
import com.imageinnova.transporter.tileentities.TileEntityTransporter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
	public static final int TRANSPORTER_ENTITY_GUI = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == TRANSPORTER_ENTITY_GUI)
	        return new ContainerTransporter(player.inventory, (TileEntityTransporter) world.getTileEntity(new BlockPos(x, y, z)));

	    return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == TRANSPORTER_ENTITY_GUI) {
	        return new GuiTransporter(player.inventory, (TileEntityTransporter) world.getTileEntity(new BlockPos(x, y, z)));
		}

	    return null;
	}

}
