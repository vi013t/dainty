package violet.dainty.features.carryon;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public interface PacketBase extends CustomPacketPayload {
	void handle(Player player);
}