package violet.dainty.features.playerspecificloot.api.advancement;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public interface IContainerTrigger extends ITrigger {
  void trigger(ServerPlayer player, UUID condition);
}
