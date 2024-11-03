package violet.dainty.features.crawl;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import violet.dainty.Dainty;

@EventBusSubscriber(modid = Dainty.MODID, bus = Bus.MOD)
public class CrawlModEventListener {
	
	@SubscribeEvent
	public static void registerPacket(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToServer(CrawlPacket.TYPE, CrawlPacket.STREAM_CODEC, new CrawlPacketHandler());
	}
}
