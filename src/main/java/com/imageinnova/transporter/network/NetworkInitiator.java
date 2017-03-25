package com.imageinnova.transporter.network;

import com.imageinnova.transporter.Transporter;
import com.imageinnova.transporter.network.MessageTransporterList.RequestHandler;
import com.imageinnova.transporter.network.MessageTransporterList.ResponseHandler;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public final class NetworkInitiator {
	static int discriminator = 0;

	public static void init() {
		Transporter.network = NetworkRegistry.INSTANCE.newSimpleChannel(Transporter.MODID);

		// transporter requests from client to server
		Transporter.network.registerMessage(MessageTransport.Handler.class, MessageTransport.class, discriminator++, Side.SERVER);

		// request for TransporterList
		Transporter.network.registerMessage(RequestHandler.class, MessageTransporterList.class, discriminator++, Side.SERVER);

		// response message to load TransporterList client-side
		Transporter.network.registerMessage(ResponseHandler.class, MessageTransporterList.class, discriminator++, Side.CLIENT);
	}
}
