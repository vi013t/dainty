package violet.dainty.features.gravestone.corelib.player;

import violet.dainty.features.gravestone.corelib.Logger;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;

public class PlayerUtils {

    /**
     * Returns the model flags for the player skin
     *
     * @param player the player
     * @return the model flags
     */
    public static byte getModel(Player player) {
        try {
            Field dataPlayerModeCustomisation = Player.class.getDeclaredField("DATA_PLAYER_MODE_CUSTOMISATION");
            dataPlayerModeCustomisation.setAccessible(true);
            return player.getEntityData().get((EntityDataAccessor<Byte>) dataPlayerModeCustomisation.get(null));
        } catch (Exception e) {
            Logger.INSTANCE.error("Error getting player model", e);
            return 0;
        }
    }

}
