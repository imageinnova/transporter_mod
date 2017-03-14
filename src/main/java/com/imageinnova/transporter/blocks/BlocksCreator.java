package com.imageinnova.transporter.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BlocksCreator {
	public static BlockTransporter transporter;
	
	public static void create() {
		GameRegistry.registerBlock(transporter = new BlockTransporter("transporter"), "transporter");
	}
}
