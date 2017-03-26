package com.imageinnova.transporter.worldsaveddata;

import java.util.ArrayList;
import java.util.List;

import com.imageinnova.transporter.Transporter;
import com.imageinnova.transporter.network.MessageTransporterList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.util.Constants;

public class TransporterList extends WorldSavedData {
	private static final String NAME = Transporter.MODID + "_transporter_list";
	
	private List<BlockPos> list= new ArrayList<BlockPos>();
	
	public TransporterList() {
		super(NAME);
	}
	
	public TransporterList(String s) {
		super(s);
	}

	public List<BlockPos> getList() {
		return list;
	}

	public static TransporterList get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		TransporterList instance = (TransporterList) storage.getOrLoadData(TransporterList.class, NAME);

		if (instance == null) {
			instance = new TransporterList();
			storage.setData(NAME, instance);
		}
		
		if (!world.isRemote) {
			Transporter.network.sendToDimension(new MessageTransporterList(instance.list), world.provider.getDimension());
		}
		return instance;
	}
	
	public void add(BlockPos pos) {
		list.add(pos);
		markDirty();
	}
	
	public void remove(BlockPos pos) {
		list.remove(pos);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList tagList = nbt.getTagList("transporter_list", Constants.NBT.TAG_COMPOUND);
		list.clear();
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound item = tagList.getCompoundTagAt(i);
			list.add(new BlockPos(item.getInteger("x"), item.getInteger("y"), item.getInteger("z")));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList tagList = new NBTTagList();
		for (int i = 0; i < list.size(); i++) {
			NBTTagCompound item = new NBTTagCompound();
			BlockPos pos = list.get(i);
			item.setInteger("x", pos.getX());
			item.setInteger("y", pos.getY());
			item.setInteger("z", pos.getZ());
			tagList.appendTag(item);
		}
		compound.setTag("transporter_list", tagList);
		
		
		return compound;
	}
}
