package violet.dainty.features.structurecompass.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import violet.dainty.Dainty;
import violet.dainty.features.structurecompass.StructureCompass;
import violet.dainty.features.structurecompass.items.NaturesCompassItem;

@EventBusSubscriber(modid = Dainty.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NaturesCompassRegistry {
	
	@SubscribeEvent
	public static void register(RegisterEvent event) {
	    event.register(BuiltInRegistries.ITEM.key(), registry -> {
	    	StructureCompass.naturesCompass = new NaturesCompassItem();
            registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, NaturesCompassItem.NAME), StructureCompass.naturesCompass);
        });
	    
	    event.register(BuiltInRegistries.DATA_COMPONENT_TYPE.key(), registry -> {
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "biome_id"), StructureCompass.BIOME_ID);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "compass_state"), StructureCompass.COMPASS_STATE);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "found_x"), StructureCompass.FOUND_X);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "found_z"), StructureCompass.FOUND_Z);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "search_radius"), StructureCompass.SEARCH_RADIUS);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "samples"), StructureCompass.SAMPLES);
	    	registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "display_coords"), StructureCompass.DISPLAY_COORDS);
	    });
	}

}
