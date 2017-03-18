package com.imageinnova.transporter;

import com.imageinnova.transporter.blocks.BlocksCreator;
import com.imageinnova.transporter.network.MessageTransport;
import com.imageinnova.transporter.network.TransporterGuiHandler;
import com.imageinnova.transporter.recipes.Recipes;
import com.imageinnova.transporter.sound.TransporterSoundHandler;
import com.imageinnova.transporter.tileentities.TileEntitiesInitiator;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		BlocksCreator.preInit();
		TileEntitiesInitiator.init();
		Transporter.network = NetworkRegistry.INSTANCE.newSimpleChannel(Transporter.MODID);
		Transporter.network.registerMessage(MessageTransport.Handler.class, MessageTransport.class, 0, Side.SERVER);
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
