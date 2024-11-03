package violet.dainty.features.inventorysorting;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import violet.dainty.Dainty;

@EventBusSubscriber(modid = Dainty.MODID, bus = Bus.MOD)
public class InventorySortingModEventHandler {

	@SubscribeEvent
	public static void registerPacket(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToServer(SortPacket.TYPE, SortPacket.STREAM_CODEC, new SortPacketHandler());
	}
}