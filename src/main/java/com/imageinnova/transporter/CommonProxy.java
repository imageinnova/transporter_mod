package com.imageinnova.transporter;

import com.imageinnova.transporter.blocks.BlocksCreator;
import com.imageinnova.transporter.network.MessageTransport;
import com.imageinnova.transporter.network.ModGuiHandler;
import com.imageinnova.transporter.recipes.Recipes;
import com.imageinnova.transporter.tileentities.TileEntitiesInitiator;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		BlocksCreator.create();
		TileEntitiesInitiator.init();
		Transporter.network = NetworkRegistry.INSTANCE.newSimpleChannel(Transporter.MODID);
		Transporter.network.registerMessage(MessageTransport.Handler.class, MessageTransport.class, 0, Side.SERVER);
    }

    public void init(FMLInitializationEvent e) {
    	Recipes.init();
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(Transporter.instance, new ModGuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}
