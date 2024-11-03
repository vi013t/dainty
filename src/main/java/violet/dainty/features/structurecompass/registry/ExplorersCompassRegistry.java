package violet.dainty.features.structurecompass.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import violet.dainty.Dainty;
import violet.dainty.features.structurecompass.StructureCompass;
import violet.dainty.features.structurecompass.items.ExplorersCompassItem;

@EventBusSubscriber(modid = Dainty.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ExplorersCompassRegistry {

	@SubscribeEvent
	public static void registerItems(RegisterEvent e) {
		e.register(BuiltInRegistries.ITEM.key(), helper -> {
			StructureCompass.explorersCompass = new ExplorersCompassItem();
            helper.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, ExplorersCompassItem.NAME), StructureCompass.explorersCompass);
        });
		
		e.register(BuiltInRegistries.DATA_COMPONENT_TYPE.key(), registry -> {
			registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "structure_id"), StructureCompass.STRUCTURE_ID_COMPONENT);
			registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "compass_state"), StructureCompass.COMPASS_STATE_COMPONENT);
			registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "found_x"), StructureCompass.FOUND_X_COMPONENT);
			registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "found_z"), StructureCompass.FOUND_Z_COMPONENT);
			registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "search_radius"), StructureCompass.SEARCH_RADIUS_COMPONENT);
			registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "samples"), StructureCompass.SAMPLES_COMPONENT);
			registry.register(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "display_coords"), StructureCompass.DISPLAY_COORDS_COMPONENT);
		});
	}

}