package com.imageinnova.transporter;

import com.imageinnova.transporter.blocks.BlockTransporter;
import com.imageinnova.transporter.blocks.ModBlocks;
import com.imageinnova.transporter.network.NetworkInitiator;
import com.imageinnova.transporter.network.TransporterGuiHandler;
import com.imageinnova.transporter.sound.TransporterSoundHandler;
import com.imageinnova.transporter.tileentities.TileEntitiesInitiator;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		TileEntitiesInitiator.init();
		NetworkInitiator.init();
    }

    public void init(FMLInitializationEvent e) {    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(Transporter.instance, new TransporterGuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(ModBlocks.transporter = new BlockTransporter("transporter"));
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
		ModBlocks.transporterItem = new ItemBlock(ModBlocks.transporter);
		ModBlocks.transporterItem.setRegistryName(ModBlocks.transporter.getRegistryName());
		event.getRegistry().register(ModBlocks.transporterItem);
    }
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		TransporterSoundHandler.computer = generateSoundEvent("computer");
		event.getRegistry().register(TransporterSoundHandler.computer);
		TransporterSoundHandler.transport = generateSoundEvent("transport");
		event.getRegistry().register(TransporterSoundHandler.transport);
	}
	
	private static SoundEvent generateSoundEvent(String name) {
		ResourceLocation loc = new ResourceLocation(Transporter.MODID, name);
		SoundEvent e = new SoundEvent(loc).setRegistryName(name);

		return e;
	}
}
