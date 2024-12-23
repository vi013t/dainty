package violet.dainty.features.gravestone.corelib.client;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

public class PlayerSkins {

    @Nullable
    private static final Field gameProfileCacheField;

    static {
        Field gameProfileCache = null;
        Field[] fields = SkullBlockEntity.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(GameProfileCache.class)) {
                field.setAccessible(true);
                gameProfileCache = field;
                break;
            }
        }
        gameProfileCacheField = gameProfileCache;
    }

    private static HashMap<UUID, GameProfile> PLAYERS = new HashMap<>();

    /**
     * Gets the resource location of the skin of provided player UUID and name.
     * Defaults to the steve and alex skin.
     *
     * @param uuid the UUID of the player
     * @param name the name of the player
     * @return the resource location to the skin
     */
    public static ResourceLocation getSkin(UUID uuid, String name) {
        return getSkin(getGameProfile(uuid, name));
    }

    /**
     * Gets the resource location of the skin of the provided player
     *
     * @param player the player to get the skin of
     * @return the resource location to the skin
     */
    public static ResourceLocation getSkin(Player player) {
        return getSkin(player.getGameProfile());
    }

    /**
     * Gets the resource location of the skin of the provided game profile
     *
     * @param gameProfile the game profile of the player
     * @return the resource location to the skin
     */
    public static ResourceLocation getSkin(GameProfile gameProfile) {
        return Minecraft.getInstance().getSkinManager().getInsecureSkin(gameProfile).texture();
    }

    /**
     * Gets the game profile of the provided player UUID and name
     *
     * @param uuid the UUID of the player
     * @param name the name of the player
     * @return the game profile
     */
    public static GameProfile getGameProfile(UUID uuid, String name) {
        if (PLAYERS.containsKey(uuid)) {
            return PLAYERS.get(uuid);
        }
        GameProfile gameProfile = new GameProfile(uuid, name);
        if (gameProfileCacheField == null) {
            return gameProfile;
        }
        try {
            @SuppressWarnings("null")
			GameProfileCache cache = (GameProfileCache) gameProfileCacheField.get(null);
            cache.getAsync(name).thenAccept(p -> {
                p.ifPresent(value -> PLAYERS.put(uuid, value));
            });
        } catch (Exception e) {
            PLAYERS.put(uuid, gameProfile);
            return gameProfile;
        }
        /*CompoundTag tag = new CompoundTag();
        tag.putString("SkullOwner", name);
        SkullBlockEntity.resolveGameProfile(tag);*/
        return gameProfile;
    }

    /**
     * Returns if the skin is slim
     *
     * @param uuid the UUID of the player
     * @return if the skin is slim
     */
    public static boolean isSlim(UUID uuid) {
        @SuppressWarnings("null")
		PlayerInfo networkPlayerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(uuid);
        return networkPlayerInfo == null ? (uuid.hashCode() & 1) == 1 : networkPlayerInfo.getSkin().model().equals(PlayerSkin.Model.SLIM);
    }

    /**
     * Returns if the skin is slim
     *
     * @param player the player
     * @return if the skin is slim
     */
    public static boolean isSlim(Player player) {
        return isSlim(player.getUUID());
    }

}
