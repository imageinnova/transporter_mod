package com.imageinnova.transporter.blocks;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BlocksCreator {
	public static BlockTransporter transporter;
	public static ItemBlock transporterItem;
	
	public static void preInit() {
		GameRegistry.register(transporter = new BlockTransporter("transporter"));
		transporterItem = new ItemBlock(transporter);
		transporterItem.setRegistryName(transporter.getRegistryName());
		GameRegistry.register(transporterItem);
	}
	
	public static void init() {
	}
}
