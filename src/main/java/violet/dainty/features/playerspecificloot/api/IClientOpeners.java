package violet.dainty.features.playerspecificloot.api;

import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;

public interface IClientOpeners extends IOpeners {
  // TODO: CLIENT ONLY
  @Nullable
  Set<UUID> getClientOpeners();

  boolean isClientOpened();

  void setClientOpened(boolean opened);

  @Override
  default boolean clearOpeners () {
    boolean result = IOpeners.super.clearOpeners();
    Set<UUID> clientOpeners = getClientOpeners();
    if (clientOpeners != null && !clientOpeners.isEmpty()) {
      clientOpeners.clear();
      markChanged();
      return true;
    }
    return result;
  }

  default boolean hasClientOpened () {
    Player player = LootrAPI.getPlayer();
    if (player == null) {
      return false;
    }

    return hasClientOpened(player.getUUID());
  }

  default boolean hasClientOpened (UUID uuid) {
    Set<UUID> clientOpeners = getClientOpeners();
    if (clientOpeners != null && !clientOpeners.isEmpty() && clientOpeners.contains(uuid)) {
      return true;
    }
    return isClientOpened();
  }
}
