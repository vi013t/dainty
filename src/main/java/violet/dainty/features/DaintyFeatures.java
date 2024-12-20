package violet.dainty.features;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import violet.dainty.features.biomecompass.BiomeCompass;
import violet.dainty.features.carryon.CarryOn;
import violet.dainty.features.foodinfo.FoodInfo;
import violet.dainty.features.gravestone.Gravestone;
import violet.dainty.features.playerspecificloot.neoforge.Lootr;
import violet.dainty.features.recipeviewer.addons.resources.neoforge.JEResources;
import violet.dainty.features.recipeviewer.core.neoforge.JustEnoughItems;
import violet.dainty.features.structurecompass.StructureCompass;

/**
 * Registry-style class (similar to those found in {@code violet.dainty.registries}) for registering features.
 */
public class DaintyFeatures {
	
	/**
	 * Registers the mod's features that have their own registries. This is usually for more complex features and features used from other mods,
	 * such as CarryOn and Nature's Compass. This should be called in the constructor of the main mod class at the time of mod construction, just
	 * like all other registries.
	 * 
	 * @param modEventBus The event bus passed by Neoforge at the time of the mod's construction.
	 * @param modContainer The mod's container supplied by Neoforge at the time of the mod's construction.
	 */
	public static void register(final IEventBus modEventBus, final ModContainer modContainer, final Dist dist) {
		new CarryOn(modContainer);
		new BiomeCompass(modContainer);
		new StructureCompass(modContainer);
		new Gravestone(modEventBus);
		new FoodInfo(modEventBus, modContainer);
		new Lootr(modContainer, modEventBus); 
		new JustEnoughItems(modEventBus, dist);
		new JEResources(modContainer, dist);
	}
}
