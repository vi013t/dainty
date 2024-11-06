package violet.dainty.features.playerspecificloot.neoforge.event; 

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import violet.dainty.Dainty;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;

@EventBusSubscriber(modid = Dainty.MODID)
public class HandleAdvancement {
  @SubscribeEvent
  public static void onAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
    if (!event.getEntity().level().isClientSide()) {
      LootrRegistry.getAdvancementTrigger().trigger((ServerPlayer) event.getEntity(), event.getAdvancement().id());
    }
  }

  @SubscribeEvent
  public static void onAdvancement(AdvancementEvent.AdvancementProgressEvent event) {
    if (!event.getEntity().level().isClientSide()) {
      LootrRegistry.getAdvancementTrigger().trigger((ServerPlayer) event.getEntity(), event.getAdvancement().id());
    }
  }
}
