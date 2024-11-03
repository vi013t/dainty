package violet.dainty.features.veinmine;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import violet.dainty.Dainty;

@EventBusSubscriber(modid = Dainty.MODID, bus = Bus.MOD)
public class VeinMineModEventHandler {
	
	@SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.BOSS_OVERLAY, ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "veinmine"), new VeinMineSettingsOverlay());
    }

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToServer(VeinMineSettings.TYPE, VeinMineSettings.STREAM_CODEC, new VeinMineSettingsPacketHandler());
	}
}
