package violet.dainty.features.recipeviewer.core.common.network;

import net.minecraft.server.level.ServerPlayer;
import violet.dainty.features.recipeviewer.core.common.network.packets.PlayToClientPacket;

public interface IConnectionToClient {
	<T extends PlayToClientPacket<T>> void sendPacketToClient(T packet, ServerPlayer player);
}
