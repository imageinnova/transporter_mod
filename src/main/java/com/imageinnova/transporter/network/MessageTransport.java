package com.imageinnova.transporter.network;

import com.imageinnova.transporter.inventory.guicontainer.ContainerTransporter;
import com.imageinnova.transporter.sound.TransporterSoundHandler;
import com.imageinnova.transporter.tileentities.TileEntityTransporter;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
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
			EntityPlayer player = ctx.getServerHandler().player;
			
			if (player.openContainer instanceof ContainerTransporter) {
				ContainerTransporter container = (ContainerTransporter) player.openContainer;
				final TileEntityTransporter te = container.getTe();
				
				BlockPos pos = message.to;
				// position to get chunk load rolling
				player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
				Chunk ch = player.world.getChunkFromBlockCoords(pos);
				ch = player.world.getChunkProvider().getLoadedChunk(ch.x, ch.z);
				while (!player.world.isChunkGeneratedAt(ch.x, ch.z) || !ch.isLoaded()) {
					//
				};


				// make sure the player isn't standing on air
				BlockPos posBelow = pos.add(0, -1, 0);
				while (player.world.getBlockState(posBelow).getBlock() == Blocks.AIR) {
					pos = posBelow;
					posBelow = posBelow.add(0, -1, 0);
				}
				// Ensure they player isn't buried
				BlockPos posAbove = pos.add(0, 1, 0);
				while (player.world.getBlockState(pos).getBlock() != Blocks.AIR || player.world.getBlockState(posAbove).getBlock() != Blocks.AIR) {
					pos = pos.add(0, 1, 0);
					posAbove = pos.add(0, 1, 0);
				}

				// Send the player on their way
				int fuelNeeded = Math.round((float) pos.getDistance(te.getPos().getX(), pos.getY(), te.getPos().getZ()) / 100);
				IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				itemHandler.extractItem(te.INPUT_SLOT, fuelNeeded, false);
				player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
				player.world.playSound(null, pos, TransporterSoundHandler.transport, SoundCategory.PLAYERS, 1.0f, 1.0f);

			}
			return null;
		}
		
	}
}
