package com.imageinnova.transporter.network;

import com.imageinnova.transporter.inventory.guicontainer.ContainerTransporter;
import com.imageinnova.transporter.sound.TransporterSoundHandler;
import com.imageinnova.transporter.tileentities.TileEntityTransporter;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MessageTransport implements IMessage {
	private BlockPos to;
	
	public MessageTransport() {
	}
	
	public MessageTransport(BlockPos to) {
		this.to = to;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		this.to = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
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
				IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				itemHandler.extractItem(te.INPUT_SLOT, 1, false);
				player.setPositionAndUpdate(message.to.getX(), message.to.getY(), message.to.getZ());
				player.world.playSound(null, message.to, TransporterSoundHandler.transport, SoundCategory.PLAYERS, 1.0f, 1.0f);

			}
			return null;
		}
		
	}
}
