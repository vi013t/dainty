package violet.dainty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import violet.dainty.features.DaintyFeatures;
import violet.dainty.registries.DaintyDataAttachments;
import violet.dainty.registries.DaintyDataComponents;
import violet.dainty.registries.DaintyItems;
import violet.dainty.registries.DaintyPotions;

/**
 * The main mod class for Dainty. A new instance of this is automatically constructed by Neoforge when the mod is loaded.
 */
@Mod(Dainty.MODID)
public class Dainty {

	/** 
	 * The unique mod ID for the Dainty mod. This must match the following exactly:
	 *
	 * <ul>
	 *	<li>The mod_id specified in gradle.properties</li>
	 * 	<li>The argument passed to the {@code @Mod} annotation on the main mod class</li>
	 *  <li>The modId specified in {@code /src/main/resources/META-INF/neoforge.mods.toml}</li>
	 * 	<li>The name of the folder {@code /src/main/resources/assets/<MODID>}</li>
	 * 	<li>The name of the folder {@code /src/main/resources/data/<MODID>}</li>
	 * 	<li>The name passed to registries, i.e. {@link DaintyItems#ITEMS}</li>
	 * </ul> 
	 * 
	 * And, preferably, but not required:
	 * 
	 * <ul>
	 *	<li>The main mod package, {@code violet.<MODID>}</li> 
	 *	<li>The name of the mixin configuration file, {@code <MODID>.mixins.json}</li> 
	 * </ul>
	 * 
	 * And, therefore obviously, cannot and will not ever change at runtime. Seriously, don't. I'm looking at you,
	 * reflection addicts. Seriously. Like, don't. I don't know why you'd ever want to, but if you really do... don't.
	 */
    public static final String MODID = "dainty"; 

	/**
	 * Dainty's debug logger, used for debugging, printing error messages, etc.
	 */
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	/**
	 * The main (and only) constructor for the main Dainty mod class. This should not be constructed manually; It will
	 * be automatically called by Neoforge when the mod is loaded.
	 * 
	 * @param modEventBus the event bus passed by Neoforge when constructing the mod
	 * @param modContainer the mod container passed by Neoforge when constructing the mod
	 */
    public Dainty(final IEventBus modEventBus, final ModContainer modContainer) {
		DaintyConfig.register(modContainer);
		DaintyFeatures.register(modEventBus, modContainer);
		DaintyDataComponents.register(modEventBus);
		DaintyDataAttachments.register(modEventBus);
		DaintyItems.register(modEventBus);
		DaintyPotions.register(modEventBus);
    }
}
