package violet.dainty.features.recipeviewer.core.common.network.packets.handlers;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.network.ClientPacketContext;
import violet.dainty.features.recipeviewer.core.common.util.ChatUtil;

import java.util.List;

/**
 * Client-side-only functions related to cheat permissions
 */
public class ClientCheatPermissionHandler {
	public static void handleHasCheatPermission(ClientPacketContext context, boolean hasPermission, List<String> allowedCheatingMethods) {
		if (!hasPermission) {
			LocalPlayer player = context.player();
			ChatUtil.writeChatMessage(player, "dainty.chat.error.no.cheat.permission.1", ChatFormatting.RED);

			if (allowedCheatingMethods.isEmpty()) {
				ChatUtil.writeChatMessage(player, "dainty.chat.error.no.cheat.permission.disabled", ChatFormatting.RED);
			} else {
				ChatUtil.writeChatMessage(player, "dainty.chat.error.no.cheat.permission.enabled", ChatFormatting.RED);
				for (String allowedCheatingMethod : allowedCheatingMethods) {
					ChatUtil.writeChatMessage(player, allowedCheatingMethod, ChatFormatting.RED);
				}
			}

			IClientToggleState toggleState = Internal.getClientToggleState();
			toggleState.setCheatItemsEnabled(false);
			player.closeContainer();
		}
	}
}
