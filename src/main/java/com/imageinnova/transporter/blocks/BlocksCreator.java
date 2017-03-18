package com.imageinnova.transporter.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BlocksCreator {
	public static BlockTransporter transporter;
	
	public static void preInit() {
		GameRegistry.register(transporter = new BlockTransporter("transporter"));
	}
	
	public static void init() {
	}
}
