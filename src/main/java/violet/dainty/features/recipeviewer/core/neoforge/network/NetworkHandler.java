package violet.dainty.features.recipeviewer.core.neoforge.network;

import java.util.function.BiConsumer;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IServerConfig;
import violet.dainty.features.recipeviewer.core.common.network.ClientPacketContext;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToClient;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.common.network.ServerPacketContext;
import violet.dainty.features.recipeviewer.core.common.network.packets.PacketCheatPermission;
import violet.dainty.features.recipeviewer.core.common.network.packets.PacketDeletePlayerItem;
import violet.dainty.features.recipeviewer.core.common.network.packets.PacketGiveItemStack;
import violet.dainty.features.recipeviewer.core.common.network.packets.PacketRecipeTransfer;
import violet.dainty.features.recipeviewer.core.common.network.packets.PacketRequestCheatPermission;
import violet.dainty.features.recipeviewer.core.common.network.packets.PacketSetHotbarItemStack;
import violet.dainty.features.recipeviewer.core.common.network.packets.PlayToClientPacket;
import violet.dainty.features.recipeviewer.core.common.network.packets.PlayToServerPacket;
import violet.dainty.features.recipeviewer.core.neoforge.events.PermanentEventSubscriptions;

public class NetworkHandler {
	private final String protocolVersion;
	private final IServerConfig serverConfig;
	private final IConnectionToServer connectionToServer;
	private final IConnectionToClient connectionToClient;

	public NetworkHandler(String protocolVersion, IServerConfig serverConfig) {
		this.protocolVersion = protocolVersion;
		this.serverConfig = serverConfig;

		this.connectionToServer = new ConnectionToServer();
		Internal.setServerConnection(this.connectionToServer);
		this.connectionToClient = new ConnectionToClient();
	}

	public void registerPacketHandlers(PermanentEventSubscriptions subscriptions) {
		subscriptions.register(RegisterPayloadHandlersEvent.class, ev ->
			ev.registrar(this.protocolVersion)
			.executesOn(HandlerThread.MAIN)
			.optional()
			.playToServer(PacketDeletePlayerItem.TYPE, PacketDeletePlayerItem.STREAM_CODEC, wrapServerHandler(PacketDeletePlayerItem::process))
			.playToServer(PacketGiveItemStack.TYPE, PacketGiveItemStack.STREAM_CODEC, wrapServerHandler(PacketGiveItemStack::process))
			.playToServer(PacketRecipeTransfer.TYPE, PacketRecipeTransfer.STREAM_CODEC, wrapServerHandler(PacketRecipeTransfer::process))
			.playToServer(PacketSetHotbarItemStack.TYPE, PacketSetHotbarItemStack.STREAM_CODEC, wrapServerHandler(PacketSetHotbarItemStack::process))
			.playToServer(PacketRequestCheatPermission.TYPE, PacketRequestCheatPermission.STREAM_CODEC, wrapServerHandler(PacketRequestCheatPermission::process))
			.playToClient(PacketCheatPermission.TYPE, PacketCheatPermission.STREAM_CODEC, wrapClientHandler(PacketCheatPermission::process))
		);
	}

	private <T extends PlayToClientPacket<T>> IPayloadHandler<T> wrapClientHandler(BiConsumer<T, ClientPacketContext> consumer) {
		return (t, payloadContext) -> {
			LocalPlayer player = (LocalPlayer) payloadContext.player();
			var clientPacketContext = new ClientPacketContext(player, connectionToServer);
			payloadContext.enqueueWork(() -> {
				consumer.accept(t, clientPacketContext);
			});
		};
	}

	private <T extends PlayToServerPacket<T>> IPayloadHandler<T> wrapServerHandler(BiConsumer<T, ServerPacketContext> consumer) {
		return (t, payloadContext) -> {
			ServerPlayer player = (ServerPlayer) payloadContext.player();
			var serverPacketContext = new ServerPacketContext(player, serverConfig, connectionToClient);
			payloadContext.enqueueWork(() -> {
				consumer.accept(t, serverPacketContext);
			});
		};
	}

	public IConnectionToServer getConnectionToServer() {
		return connectionToServer;
	}
}
