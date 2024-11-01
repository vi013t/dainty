package violet.dainty;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Dainty.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DaintyConfig {

	/**
	 * The builder for the mod's configuration. Individual configuration options are added to this (see below), and then it's built into {@link #SPEC},
	 * which is registered in {@link Dainty#Dainty the Dainty main mod class constructor}.
	 */
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<Float> ENDERMAN_PHANTOM_MEMBRANE_DROP_CHANCE = BUILDER
		.comment("Drop chance of phantom membranes from enderman")
		.define("endermanPhantomMembraneDropChance", 0.5f);

    private static final ModConfigSpec.ConfigValue<Boolean> DISABLE_PHANTOMS = BUILDER
		.comment("Disable phantom spawns")
		.define("disablePhantoms", true);

    private static final ModConfigSpec.ConfigValue<Boolean> DISABLE_FARMLAND_TRAMPLING = BUILDER
		.comment("Disable farmland trampling")
		.define("enableNoFarmlandTrampling", true);

    private static final ModConfigSpec.ConfigValue<Boolean> ALLOW_JUMPING_ON_FENCES = BUILDER
		.comment("Allow jumping on fences")
		.define("allowJumpingOnFences", true);

	/**
	 * The configuration value for the number of wither skeleton kills required per guaranteed wither skeleton skull drop.
	 * 
	 * <br/><br/>
	 * 
	 * When the mod's configuration is loaded ({@link #onConfigLoad(ModConfigEvent)}), the value of this will be retrieved and stored in 
	 * {@link #witherSkeletonKillsPerGuaranteedSkull}. It can be read publicly via {@link #witherSkeletonKillsPerGuaranteedSkull()}. 
	 * 
	 * <br/><br/>
	 * 
	 * By default, this configuration value is set to {@code 20}, meaning that wither skeletons are guaranteed to drop a skull every 20 kills (tracked per-player). 
	 * Changing this value will change the amount of kills required per guaranteed skull, and <strong>setting it to 0 will disable the feature entirely,
	 * meaning wither skeleton skulls are never guaranteed</strong>.
	 */
	private static final ModConfigSpec.ConfigValue<Integer> WITHER_SKELETON_KILLS_PER_GUARANTEED_SKULL = BUILDER
		.comment("Number of wither skeleton kills between guaranteed skull drops")
		.define("witherSkeletonKillsPerGuaranteedSkull", 20);

	/**
	 * The configuration value for the percent drop chance of {@link violet.dainty.registries.DaintyItems#WARDEN_HEART warden hearts} from wardens. 
	 * 
	 * <br/><br/>
	 * 
	 * When the mod's configuration is loaded ({@link #onConfigLoad(ModConfigEvent)}), the value of this will be retrieved and stored in 
	 * {@link #wardenHeartDropChance}. It can be read publicly via {@link #wardenHeartDropChance()}. 
	 * 
	 * <br/><br/>
	 * 
	 * By default, this configuration value is set to 
	 * {@code 1.0}, meaning that warden hearts are a guaranteed drop from wardens. Setting this lower will make it a percent chance each time a warden
	 * dies. If this is set to {@code 0}, warden hearts will never be dropped.
	 */
    private static final ModConfigSpec.ConfigValue<Float> WARDEN_HEART_DROP_CHANCE = BUILDER
		.comment("Chance for Wardens to drop a Warden Heart")
		.define("wardenHeartDropChance", 1f);

	/**
	 * The configuration value for enabling/disabling automatic right-click crop harvest and replanting. When the mod's configuration is loaded 
	 * ({@link #onConfigLoad(ModConfigEvent)}), the value of this will be retrieved and stored in {@link #enableRightClickCropHarvest}. It can
	 * be read publicly via {@link #rightClickCropHarvestIsEnabled()}. By default, this configuration value is set to {@code true}, meaning that
	 * the right-click crop harvest feature is enabled by default and will be enabled unless the user explicitly sets this to {@link false} in
	 * their Dainty configuration file.
	 */
	private static final ModConfigSpec.ConfigValue<Boolean> ENABLE_RIGHT_CLICK_CROP_HARVEST = BUILDER
		.comment("Enable right-clicking on mature crops to harvest and replant them automatically.")
		.define("enableRightClickCropHarvest", true);

	/**
	 * The constructed {@link ModConfigSpec}, built from the various individual mod configuration options. This is registered with the mods container
	 * in {@link Dainty#Dainty the Dainty main mod class constructor}.
	 */
    public static final ModConfigSpec SPEC = BUILDER.build();

    private static float endermanPhantomMembraneDropChance;
	private static boolean disablePhantoms;
	private static boolean disableFarmlandTrampling;
	private static boolean allowJumpingOnFences;
	private static int witherSkeletonKillsPerGuaranteedSkull;
	private static float wardenHeartDropChance;
	private static boolean enableRightClickCropHarvest;

    @SubscribeEvent
    public static void onConfigLoad(final ModConfigEvent event) {
        endermanPhantomMembraneDropChance = ENDERMAN_PHANTOM_MEMBRANE_DROP_CHANCE.get();
		disablePhantoms = DISABLE_PHANTOMS.get();
		disableFarmlandTrampling = DISABLE_FARMLAND_TRAMPLING.get();
		allowJumpingOnFences = ALLOW_JUMPING_ON_FENCES.get();
		witherSkeletonKillsPerGuaranteedSkull = WITHER_SKELETON_KILLS_PER_GUARANTEED_SKULL.get();
		wardenHeartDropChance = WARDEN_HEART_DROP_CHANCE.get();
		enableRightClickCropHarvest = ENABLE_RIGHT_CLICK_CROP_HARVEST.get();
    }

	public static float endermanPhantomMembraneDropChance() {
		return endermanPhantomMembraneDropChance;
	}

	public static boolean disablePhantoms() {
		return disablePhantoms;
	}

	public static boolean enableNoFarmlandTrampling() {
		return disableFarmlandTrampling;
	}
	
	public static boolean allowJumpingOnFences() {
		return allowJumpingOnFences;
	}

	public static int witherSkeletonKillsPerGuaranteedSkull() {
		return witherSkeletonKillsPerGuaranteedSkull;
	}

	public static float wardenHeartDropChance() {
		return wardenHeartDropChance;
	}

	/**
	 * Returns whether the "right-click crop harvesting" feature is enabled in the mod config. By default, this is {@code true}, and will be true,
	 * unless the mod user has explicitly set it to {@code false}.
	 * 
	 * <br/><br/>
	 * 
	 * Internally, this accesses {@link #enableRightClickCropHarvest}, which retrieves the value of {@link #ENABLE_RIGHT_CLICK_CROP_HARVEST} when the config
	 * is loaded.
	 * 
	 * @return Whether the "right-click crop harvesting" feature is enabled in the mod config.
	 */
	public static boolean rightClickCropHarvestIsEnabled() {
		return enableRightClickCropHarvest;
	}

	/**
	 * Registers the mod's configuration with the mod's container. This should be called exactly once, during the mod's construction in the main mod class's
	 * constructor ({@link Dainty#Dainty(net.neoforged.bus.api.IEventBus, ModContainer) new Dainty()})
	 * 
	 * @param modContainer The mod's container supplied by Neoforge at the time of construction
	 */
	public static void register(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, DaintyConfig.SPEC);
	}
}
