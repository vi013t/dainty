package violet.dainty.features.durabilityhud;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import violet.dainty.Dainty;

@EventBusSubscriber(modid = Dainty.MODID, bus = Bus.MOD)
public class DurabilityHudModEventHandler {
	
	@SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.BOSS_OVERLAY, ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "durability"), new DurabilityHudOverlay());
    }
}
