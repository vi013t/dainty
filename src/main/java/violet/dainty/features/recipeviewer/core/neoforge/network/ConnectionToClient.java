package violet.dainty.features.recipeviewer.core.neoforge.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToClient;
import violet.dainty.features.recipeviewer.core.common.network.packets.PlayToClientPacket;

public class ConnectionToClient implements IConnectionToClient {
	@Override
	public <T extends PlayToClientPacket<T>> void sendPacketToClient(T packet, ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, packet);
	}
}
