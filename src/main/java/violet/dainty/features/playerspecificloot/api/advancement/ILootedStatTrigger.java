package violet.dainty.features.playerspecificloot.api.advancement;

import net.minecraft.server.level.ServerPlayer;

public interface ILootedStatTrigger extends ITrigger {
  void trigger(ServerPlayer player);
}