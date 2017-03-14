package com.imageinnova.transporter.client.render.blocks;

import com.imageinnova.transporter.blocks.BlocksCreator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class BlockRenderer {
	public static void registerBlockRenderer() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	    .register(Item.getItemFromBlock(BlocksCreator.transporter), 0, new ModelResourceLocation("transporter:transporter", "inventory"));
	}
}
