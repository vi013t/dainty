package violet.dainty.features.carryon.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import violet.dainty.Dainty;
import violet.dainty.features.carryon.client.CarryOnKeybinds;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Dainty.MODID, value = Dist.CLIENT)
public class CarryonModClientEventHandler {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerKeybinds(RegisterKeyMappingsEvent event) {
		CarryOnKeybinds.registerKeybinds(event::register);
	}
}