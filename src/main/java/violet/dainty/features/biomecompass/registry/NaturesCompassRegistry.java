package violet.dainty.features.biomecompass.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import violet.dainty.Dainty;
import violet.dainty.features.biomecompass.BiomeCompass;
import violet.dainty.features.biomecompass.items.NaturesCompassItem;

@EventBusSubscriber(modid = Dainty.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NaturesCompassRegistry {
	
	@SubscribeEvent
	public static void register(RegisterEvent event) {
	    event.register(BuiltInRegistries.ITEM.key(), registry -> {
	    	BiomeCompass.naturesCompass = new NaturesCompassItem();
            registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, NaturesCompassItem.NAME), BiomeCompass.naturesCompass);
        });
	    
	    event.register(BuiltInRegistries.DATA_COMPONENT_TYPE.key(), registry -> {
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "biome_id"), BiomeCompass.BIOME_ID);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "compass_state"), BiomeCompass.COMPASS_STATE);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "found_x"), BiomeCompass.FOUND_X);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "found_z"), BiomeCompass.FOUND_Z);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "search_radius"), BiomeCompass.SEARCH_RADIUS);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "samples"), BiomeCompass.SAMPLES);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "display_coords"), BiomeCompass.DISPLAY_COORDS);
	    });
	}

}
