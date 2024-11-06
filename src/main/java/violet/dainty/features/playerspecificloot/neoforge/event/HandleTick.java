package violet.dainty.features.playerspecificloot.neoforge.event; 

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import violet.dainty.Dainty;
import violet.dainty.features.playerspecificloot.common.block.entity.BlockEntityTicker;
import violet.dainty.features.playerspecificloot.common.data.DataStorage;
import violet.dainty.features.playerspecificloot.common.entity.EntityTicker;

@EventBusSubscriber(modid = Dainty.MODID)
public class HandleTick {
  @SubscribeEvent
  public static void onServerTick (ServerTickEvent.Post event) {
    DataStorage.doTick();

    EntityTicker.onServerTick();
    BlockEntityTicker.onServerTick();
  }
}
