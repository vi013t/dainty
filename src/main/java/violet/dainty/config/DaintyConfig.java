package violet.dainty.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.MapCodec;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import violet.dainty.Dainty;

public class DaintyConfig {

	/**
	 * A register of <a href="https://docs.neoforged.net/docs/1.20.4/resources/server/conditional/">conditions</a>. This currently only contains {@link #CONDITION
	 * the condition for checking a boolean config value}.
	 * 
	 * <br/><br/>
	 * 
	 * See <a href="https://docs.neoforged.net/docs/1.20.4/resources/server/conditional/#creating-custom-conditions">the corresponding part of the Neoforge docs</a>
	 * for more information.
	 */
	private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, Dainty.MODID);

	/**
	 * A map of all config value names and their corresponding config values. This is filled in {@link #configValue(String, Object, String) the function that
	 * creates configuration values}, and used by {@link #getConfigValueFromName(String)} to get the value of a config value from it's name.
	 */
	private static final Map<String, ModConfigSpec.ConfigValue<?>> ALL_CONFIG_VALUES = new HashMap<>();

	/**
	 * The builder for the mod's configuration. Individual configuration options are added to this (see below), and then it's built into {@link #SPEC},
	 * which is registered in {@link Dainty#Dainty the Dainty main mod class constructor}.
	 */
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<Float> ENDERMAN_PHANTOM_MEMBRANE_DROP_CHANCE = configValue("endermanPhantomMembraneDropChance", 0.5f, "Drop chance of phantom membranes from endermen.");
    public static final ModConfigSpec.ConfigValue<Boolean> DISABLE_PHANTOMS = configValue("enableNoPhantoms", true, "Disable phantom spawns.");
    public static final ModConfigSpec.ConfigValue<Boolean> DISABLE_FARMLAND_TRAMPLING = configValue("enableNoFarmlandTrampling", true, "Disable farmland trampling.");
    public static final ModConfigSpec.ConfigValue<Boolean> ALLOW_JUMPING_ON_FENCES = configValue("enableJumpingOnFences", true, "Allow players to jump onto fences.");
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_CRAWL_KEYBIND = configValue("enableCrawlKeybind", true, "Enable entering crawl mode with a keybinding.");
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_ZOOMING = configValue("enableZooming", true, "Enable zooming in with a keybinding.");
	public static final ModConfigSpec.ConfigValue<Boolean> DISABLE_BERRY_DAMAGE = configValue("enableNoBerryDamage", true, "Disable damage from sweet berry bushes.");
	public static final ModConfigSpec.ConfigValue<Boolean> DISABLE_BERRY_SLOWDOWN = configValue("enableNoBerrySlowdown", true, "Disable being slowed while in sweet berry bushes.");
	public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_CRAFTING_BRICKS_INTO_TERRACOTTA = configValue("enableCraftingBricksIntoTerracotta", true, "Enables crafting one brick block into one terracotta block.");
	public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_CRAFTING_TERRACOTTA_INTO_BRICKS = configValue("enableCraftingTerracottaIntoBricks", true, "Enables crafting one terracotta block into one brick block.");
	public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_COOKED_BERRIES = configValue("enableCookedBerries", true, "Enables smelting sweet berry bushes into cooked berries.");
	public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_OPENING_BLOCKED_CHESTS = configValue("enableOpeningBlockedChests", true, "Enable opening chests that have a solid block on them or a cat sitting on them.");

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
    public static final ModConfigSpec.ConfigValue<Float> WARDEN_HEART_DROP_CHANCE = configValue("wardenHeartDropChance", 1f, "Chance for wardens to drop a Warden Heart.");

	/**
	 * The configuration value for enabling/disabling automatic right-click crop harvest and replanting. When the mod's configuration is loaded 
	 * ({@link #onConfigLoad(ModConfigEvent)}), the value of this will be retrieved and stored in {@link #enableRightClickCropHarvest}. It can
	 * be read publicly via {@link #rightClickCropHarvestIsEnabled()}. By default, this configuration value is set to {@code true}, meaning that
	 * the right-click crop harvest feature is enabled by default and will be enabled unless the user explicitly sets this to {@link false} in
	 * their Dainty configuration file.
	 */
	public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_RIGHT_CLICK_CROP_HARVEST = configValue("enableRightClickCropHarvest", true, "Enables right-clicking on mature crops to harvest and replant them automatically.");

	/**
	 * The constructed {@link ModConfigSpec}, built from the various individual mod configuration options. This is registered with the mods container
	 * in {@link Dainty#Dainty the Dainty main mod class constructor}.
	 */
    public static final ModConfigSpec SPEC = BUILDER.build();

	/**
	 * A custom <a href="https://docs.neoforged.net/docs/1.20.4/resources/server/conditional/">condition</a> that checks if a given boolean config value
	 * is {@code true}. This allows resources, such as recipes, to be conditionally loaded based on if a configuration value is {@code true}. Without this,
	 * config values such as {@link #ENABLE_CRAFTING_BRICKS_INTO_TERRACOTTA the one enabling crafting bricks into terracotta} wouldn't be possible.
	 * 
	 * <br/><br/>
	 * 
	 * See <a href="https://docs.neoforged.net/docs/1.20.4/resources/server/conditional/#creating-custom-conditions">the corresponding part of the Neoforge docs</a>
	 * for more information.
	 */
	public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<ConfigCondition>> CONDITION = CONDITIONS.register("config", () -> ConfigCondition.CODEC);

	/**
	 * Registers the mod's configuration with the mod's container. This should be called exactly once, during the mod's construction in the main mod class's
	 * constructor ({@link Dainty#Dainty(net.neoforged.bus.api.IEventBus, ModContainer) new Dainty()})
	 * 
	 * @param modContainer The mod's container supplied by Neoforge at the time of construction
	 */
	public static void register(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, DaintyConfig.SPEC);
		CONDITIONS.register(modEventBus);
	}

	/**
	 * Creates a new configuration value. The value is added to {@link #ALL_CONFIG_VALUES} and created using the {@link #BUILDER}.
	 * 
	 * @param <T> The type of value stored in the config value.
	 * 
	 * @param name The name of the config value as it should appear in the config file
	 * @param defaultValue The default value of the config value
	 * @param description A description describing what the config value does
	 * 
	 * @return The config value object
	 */
	private static <T> ModConfigSpec.ConfigValue<T> configValue(String name, T defaultValue, String description) {
		ModConfigSpec.ConfigValue<T> value = BUILDER.comment(description).define(name, defaultValue);
		ALL_CONFIG_VALUES.put(name, value);
		return value;
	}

	/**
	 * Returns the configuration value with the given name (as specified in the config file), or {@link Optional#empty()} if none exists.
	 * 
	 * <br/><br/>
	 * 
	 * This is used by {@link #CONDITION the configuration condition} to get boolean configuration values from the name string specified
	 * in the data file. That logic is handled in the {@link ConfigCondition} class.
	 * 
	 * @param name The name of the configuration value
	 * 
	 * @return The configuration value with the given name
	 */
	public static Optional<ModConfigSpec.ConfigValue<?>> getConfigValueFromName(String name) {
		return Optional.ofNullable(ALL_CONFIG_VALUES.get(name));
	}
}
