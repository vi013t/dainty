package violet.dainty.features.recipeviewer.core.common.network;

import net.minecraft.client.player.LocalPlayer;

public record ClientPacketContext(LocalPlayer player, IConnectionToServer connection) {
}
