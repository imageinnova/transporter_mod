package com.imageinnova.transporter.network;

import java.util.ArrayList;
import java.util.List;

import com.imageinnova.transporter.Transporter;
import com.imageinnova.transporter.worldsaveddata.TransporterList;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTransporterList implements IMessage {
	private List<BlockPos> list = new ArrayList<BlockPos>();

	public MessageTransporterList() {
		
	}
	
	public MessageTransporterList(List<BlockPos> list) {
		this.list = list;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			int x = buf.readInt();
			int y = buf.readInt();
			int z = buf.readInt();
			list.add(new BlockPos(x, y, z));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(list.size());
		for (BlockPos pos : list) {
			buf.writeInt(pos.getX());
			buf.writeInt(pos.getY());
			buf.writeInt(pos.getZ());
		}
	}

	public static class RequestHandler implements IMessageHandler<MessageTransporterList, IMessage> {

		@Override
		public IMessage onMessage(MessageTransporterList message, MessageContext ctx) {
			List<BlockPos> list = TransporterList.get(ctx.getServerHandler().playerEntity.world).getList();
			
			return new MessageTransporterList(list);
		}

	}
	
	public static class ResponseHandler implements IMessageHandler<MessageTransporterList, IMessage> {

		@Override
		public IMessage onMessage(MessageTransporterList message, MessageContext ctx) {
			Transporter.transporterList = message.list;
			
			return null;
		}
		
	}
}
