package violet.dainty.features.recipeviewer.core.common.network.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import violet.dainty.features.recipeviewer.core.common.config.IServerConfig;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToClient;
import violet.dainty.features.recipeviewer.core.common.network.ServerPacketContext;
import violet.dainty.features.recipeviewer.core.common.util.ServerCommandUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;

public class PacketRequestCheatPermission extends PlayToServerPacket<PacketRequestCheatPermission> {
	public static final PacketRequestCheatPermission INSTANCE = new PacketRequestCheatPermission();
	public static final CustomPacketPayload.Type<PacketRequestCheatPermission> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "request_cheat_permission"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PacketRequestCheatPermission> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	private PacketRequestCheatPermission() {

	}

	@Override
	public Type<PacketRequestCheatPermission> type() {
		return TYPE;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, PacketRequestCheatPermission> streamCodec() {
		return STREAM_CODEC;
	}

	@Override
	public void process(ServerPacketContext context) {
		ServerPlayer player = context.player();
		IServerConfig serverConfig = context.serverConfig();
		boolean hasPermission = ServerCommandUtil.hasPermissionForCheatMode(player, serverConfig);
		PacketCheatPermission packetCheatPermission = new PacketCheatPermission(hasPermission, serverConfig);

		IConnectionToClient connection = context.connection();
		connection.sendPacketToClient(packetCheatPermission, player);
	}
}
