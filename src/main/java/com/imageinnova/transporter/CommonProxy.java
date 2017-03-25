package com.imageinnova.transporter;

import com.imageinnova.transporter.blocks.BlocksCreator;
import com.imageinnova.transporter.network.NetworkInitiator;
import com.imageinnova.transporter.network.TransporterGuiHandler;
import com.imageinnova.transporter.recipes.Recipes;
import com.imageinnova.transporter.sound.TransporterSoundHandler;
import com.imageinnova.transporter.tileentities.TileEntitiesInitiator;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		BlocksCreator.preInit();
		TileEntitiesInitiator.init();
		NetworkInitiator.init();
    }

    public void init(FMLInitializationEvent e) {
    	BlocksCreator.init();
    	
    	Recipes.init();
    	
    	TransporterSoundHandler.init();
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(Transporter.instance, new TransporterGuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}
