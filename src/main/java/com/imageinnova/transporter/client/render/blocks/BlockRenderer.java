package com.imageinnova.transporter.client.render.blocks;

import com.imageinnova.transporter.blocks.BlocksCreator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BlockRenderer {
	public static void registerBlockRenderer() {
		ItemBlock itemBlock = new ItemBlock(BlocksCreator.transporter);
		itemBlock.setRegistryName(BlocksCreator.transporter.getRegistryName());
		GameRegistry.register(itemBlock);

		ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(itemBlock.getRegistryName().toString(), "inventory"));
	}
}
