package violet.dainty.features.biomecompass.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import violet.dainty.Dainty;
import violet.dainty.features.biomecompass.NaturesCompass;
import violet.dainty.features.biomecompass.items.NaturesCompassItem;

@EventBusSubscriber(modid = Dainty.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NaturesCompassRegistry {
	
	@SubscribeEvent
	public static void register(RegisterEvent event) {
	    event.register(BuiltInRegistries.ITEM.key(), registry -> {
	    	NaturesCompass.naturesCompass = new NaturesCompassItem();
            registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, NaturesCompassItem.NAME), NaturesCompass.naturesCompass);
        });
	    
	    event.register(BuiltInRegistries.DATA_COMPONENT_TYPE.key(), registry -> {
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "biome_id"), NaturesCompass.BIOME_ID);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "compass_state"), NaturesCompass.COMPASS_STATE);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "found_x"), NaturesCompass.FOUND_X);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "found_z"), NaturesCompass.FOUND_Z);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "search_radius"), NaturesCompass.SEARCH_RADIUS);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "samples"), NaturesCompass.SAMPLES);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "display_coords"), NaturesCompass.DISPLAY_COORDS);
	    });
	}

}
