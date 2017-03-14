package com.imageinnova.transporter.tileentities;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class TileEntitiesInitiator {
	public static void init() {
		GameRegistry.registerTileEntity(TileEntityTransporter.class, "transporter_entity");
	}
}
