package com.imageinnova.transporter.sound;

import com.imageinnova.transporter.Transporter;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TransporterSoundHandler {
	public static SoundEvent transport;
	
	private static int size = 0;
	
	public static void init() {
		size = SoundEvent.REGISTRY.getKeys().size();
		
		transport = register("transport");
	}
	
	public static SoundEvent register(String name) {
		ResourceLocation loc = new ResourceLocation(Transporter.MODID + ":" + name);
		SoundEvent e = new SoundEvent(loc);
		
		SoundEvent.REGISTRY.register(size, loc, e);
		size++;
		
		return e;
	}
}
