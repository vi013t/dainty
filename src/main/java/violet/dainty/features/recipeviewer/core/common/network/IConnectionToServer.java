package violet.dainty.features.recipeviewer.core.common.network;

import violet.dainty.features.recipeviewer.core.common.network.packets.PlayToServerPacket;

public interface IConnectionToServer {
	boolean isJeiOnServer();

	<T extends PlayToServerPacket<T>> void sendPacketToServer(T packet);
}
