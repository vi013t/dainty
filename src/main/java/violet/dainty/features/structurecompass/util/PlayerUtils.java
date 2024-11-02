package violet.dainty.features.structurecompass.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import violet.dainty.features.structurecompass.StructureCompass;

public class PlayerUtils {

	public static boolean canTeleport(MinecraftServer server, Player player) {
		return cheatModeEnabled(server, player) || isOp(player) || hasPermission(player);
	}

	public static boolean cheatModeEnabled(MinecraftServer server, Player player) {
		if (server != null && server.isSingleplayer()) {
			@SuppressWarnings("null")
			LevelData levelData = server.getLevel(player.level().dimension()).getLevelData();
			if (levelData instanceof ServerLevelData) {
				return ((ServerLevelData) levelData).isAllowCommands();
			}
		}

		return false;
	}

	public static boolean isOp(Player player) {
		if (player instanceof ServerPlayer) {
			@SuppressWarnings("null")
			final ServerOpListEntry userEntry = ((ServerPlayer) player).getServer().getPlayerList().getOps().get(player.getGameProfile());
			return userEntry != null;
		}

		return false;
	}

	public static boolean hasPermission(Player player) {
		if (player instanceof ServerPlayer) {
			return PermissionAPI.getPermission((ServerPlayer) player, StructureCompass.TELEPORT_PERMISSION);
		}
		
		return false;
	}

}