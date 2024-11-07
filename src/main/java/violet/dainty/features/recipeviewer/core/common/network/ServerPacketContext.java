package violet.dainty.features.recipeviewer.core.common.network;

import net.minecraft.server.level.ServerPlayer;
import violet.dainty.features.recipeviewer.core.common.config.IServerConfig;

public record ServerPacketContext(ServerPlayer player,
								IServerConfig serverConfig,
								IConnectionToClient connection
) {
}
