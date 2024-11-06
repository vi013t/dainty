package violet.dainty.features.crawl;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import violet.dainty.Dainty;

@EventBusSubscriber(modid = Dainty.MODID, bus = Bus.MOD)
public class CrawlModEventListener {
	
	/**
	 * Registers the {@link CrawlPacket} and it's {@link CrawlPacketHandler associated packet handler}
	 * with the game. This is called when packet handlers are registered via {@link RegisterPayloadHandlersEvent}.
	 * Naturally, this event is on {@link Bus#MOD the mod event bus}.
	 * 
	 * <br/><br/>
	 * 
	 * As with all event listener methods, this will automatically be called by Neoforge when the event is fired, and this shouldn't
	 * be called manually anywhere.
	 * 
	 * @param event The event fired by Neoforge.
	 */
	@SubscribeEvent
	public static void registerPacket(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToServer(CrawlPacket.TYPE, CrawlPacket.STREAM_CODEC, new CrawlPacketHandler());
	}
}
