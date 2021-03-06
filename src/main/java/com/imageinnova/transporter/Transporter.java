package com.imageinnova.transporter;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;
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
	public static final String VERSION = "2.0.0";
	public static final int MAX_TRANSPORTER_DISTANCE = 1000;
	@Instance
	public static Transporter instance;
    public static SimpleNetworkWrapper network;
    public static List<BlockPos> transporterList = new ArrayList<BlockPos>();
	
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
