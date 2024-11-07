package violet.dainty.features.recipeviewer.core.neoforge.network;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.common.network.packets.PacketDeletePlayerItem;
import violet.dainty.features.recipeviewer.core.common.network.packets.PlayToServerPacket;

public final class ConnectionToServer implements IConnectionToServer {
	@Nullable
	private static UUID jeiOnServerCacheUuid = null;
	private static boolean jeiOnServerCacheValue = false;

	@Override
	public boolean isJeiOnServer() {
		Minecraft minecraft = Minecraft.getInstance();
		ClientPacketListener clientPacketListener = minecraft.getConnection();
		if (clientPacketListener == null || !clientPacketListener.getConnection().isConnected()) {
			return false;
		}
		UUID id = clientPacketListener.getId();
		if (!id.equals(jeiOnServerCacheUuid)) {
			jeiOnServerCacheUuid = id;
			jeiOnServerCacheValue = clientPacketListener.hasChannel(PacketDeletePlayerItem.TYPE);
		}
		return jeiOnServerCacheValue;
	}

	@Override
	public <T extends PlayToServerPacket<T>> void sendPacketToServer(T packet) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientPacketListener netHandler = minecraft.getConnection();
		if (netHandler != null && isJeiOnServer()) {
			PacketDistributor.sendToServer(packet);
		}
	}
}
