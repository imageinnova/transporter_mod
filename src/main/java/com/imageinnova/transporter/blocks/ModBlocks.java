package com.imageinnova.transporter.blocks;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModBlocks {
	public static BlockTransporter transporter;
	public static ItemBlock transporterItem;
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		transporter.initModel();
	}
}
