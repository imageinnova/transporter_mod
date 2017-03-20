package com.imageinnova.transporter;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Transporter.MODID, name = Transporter.MODNAME, version = Transporter.VERSION)
public class Transporter {
	public static final String MODID = "transporter";
	public static final String MODNAME = "Transporter";
	public static final String VERSION = "1.0.1";
	public static final int MAX_TRANSPORTER_DISTANCE = 180;
	@Instance
	public static Transporter instance;
    public static SimpleNetworkWrapper network;
	
	@SidedProxy(clientSide="com.imageinnova.transporter.ClientProxy", serverSide="com.imageinnova.transporter.ServerProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}
	        
	@EventHandler
	public void init(FMLInitializationEvent e) {
	    proxy.init(e);            
	}
	        
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}
}
