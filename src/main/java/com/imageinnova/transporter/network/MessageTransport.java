package com.imageinnova.transporter.network;

import com.imageinnova.transporter.inventory.guicontainer.ContainerTransporter;
import com.imageinnova.transporter.tileentities.TileEntityTransporter;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTransport implements IMessage {
	private BlockPos from;
	private BlockPos to;
	
	public MessageTransport() {
	}
	
	public MessageTransport(BlockPos from, BlockPos to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		this.from = new BlockPos(x, y, z);
		
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		this.to = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.from.getX());
		buf.writeInt(this.from.getY());
		buf.writeInt(this.from.getZ());

		buf.writeInt(this.to.getX());
		buf.writeInt(this.to.getY());
		buf.writeInt(this.to.getZ());
	}
	
	public static class Handler implements IMessageHandler<MessageTransport, IMessage> {

		@Override
		public IMessage onMessage(final MessageTransport message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().playerEntity;
			
			if (player.openContainer instanceof ContainerTransporter) {
				ContainerTransporter container = (ContainerTransporter) player.openContainer;
				final TileEntityTransporter te = container.getTe();

				// Send the player on their way
				te.decrStackSize(te.INPUT_SLOT,  1);
				player.setPositionAndUpdate(message.to.getX(), message.to.getY(), message.to.getZ());
				player.worldObj.playSoundEffect(message.to.getX(), message.to.getY(), message.to.getZ(), "transporter:transport", 1.0F, 1.0F);
			}
			return null;
		}
		
	}
}
