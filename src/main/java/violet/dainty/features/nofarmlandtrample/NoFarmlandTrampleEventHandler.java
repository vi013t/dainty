package violet.dainty.features.nofarmlandtrample;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.BlockEvent.FarmlandTrampleEvent;
import violet.dainty.Dainty;
import violet.dainty.DaintyConfig;

@EventBusSubscriber(modid=Dainty.MODID)
public class NoFarmlandTrampleEventHandler {

	/**
	 * Removes farmland trampling by listening for a {@link FarmlandTrampleEvent} and canceling it.
	 * 
	 * @param event The {@link FarmlandTrampleEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	public static void removeFarmlandTrampling(BlockEvent.FarmlandTrampleEvent event) {
		if (DaintyConfig.enableNoFarmlandTrampling()) {
			event.setCanceled(true);
		}
	}
}
