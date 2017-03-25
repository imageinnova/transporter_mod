package com.imageinnova.transporter.client.render.blocks;

import com.imageinnova.transporter.blocks.BlocksCreator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public final class BlockRenderer {
	public static void registerBlockRenderer() {
		ModelLoader.setCustomModelResourceLocation(BlocksCreator.transporterItem, 0, new ModelResourceLocation(BlocksCreator.transporterItem.getRegistryName().toString(), "inventory"));
	}
}
