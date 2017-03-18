package com.imageinnova.transporter.recipes;

import com.imageinnova.transporter.blocks.BlocksCreator;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class Recipes {
	public static void init() {
		GameRegistry.addRecipe(new ItemStack(BlocksCreator.transporter), new Object[] {"ooo", "odo", "ooo", 'o', Blocks.OBSIDIAN, 'd', Items.DIAMOND});
	}
}
