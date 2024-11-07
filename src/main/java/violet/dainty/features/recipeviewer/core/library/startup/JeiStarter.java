package violet.dainty.features.recipeviewer.core.library.startup;

import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSetMultimap;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.ConfigManager;
import violet.dainty.features.recipeviewer.core.common.config.DebugConfig;
import violet.dainty.features.recipeviewer.core.common.config.IIngredientFilterConfig;
import violet.dainty.features.recipeviewer.core.common.config.JeiClientConfigs;
import violet.dainty.features.recipeviewer.core.common.config.file.ConfigSchemaBuilder;
import violet.dainty.features.recipeviewer.core.common.config.file.FileWatcher;
import violet.dainty.features.recipeviewer.core.common.config.file.IConfigSchemaBuilder;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.common.util.RegistryUtil;
import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IColorHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.core.util.LoggedTimer;
import violet.dainty.features.recipeviewer.core.library.color.ColorHelper;
import violet.dainty.features.recipeviewer.core.library.config.ColorNameConfig;
import violet.dainty.features.recipeviewer.core.library.config.EditModeConfig;
import violet.dainty.features.recipeviewer.core.library.config.ModIdFormatConfig;
import violet.dainty.features.recipeviewer.core.library.config.RecipeCategorySortingConfig;
import violet.dainty.features.recipeviewer.core.library.focus.FocusFactory;
import violet.dainty.features.recipeviewer.core.library.helpers.CodecHelper;
import violet.dainty.features.recipeviewer.core.library.ingredients.subtypes.SubtypeManager;
import violet.dainty.features.recipeviewer.core.library.load.PluginCaller;
import violet.dainty.features.recipeviewer.core.library.load.PluginHelper;
import violet.dainty.features.recipeviewer.core.library.load.PluginLoader;
import violet.dainty.features.recipeviewer.core.library.load.registration.RuntimeRegistration;
import violet.dainty.features.recipeviewer.core.library.plugins.jei.JeiInternalPlugin;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.VanillaPlugin;
import violet.dainty.features.recipeviewer.core.library.recipes.RecipeManager;
import violet.dainty.features.recipeviewer.core.library.runtime.JeiHelpers;
import violet.dainty.features.recipeviewer.core.library.runtime.JeiRuntime;

public final class JeiStarter {
	private static final Logger LOGGER = LogManager.getLogger();

	private final StartData data;
	private final List<IModPlugin> plugins;
	private final VanillaPlugin vanillaPlugin;
	private final ModIdFormatConfig modIdFormatConfig;
	private final ColorNameConfig colorNameConfig;
	private final RecipeCategorySortingConfig recipeCategorySortingConfig;
	@SuppressWarnings("FieldCanBeLocal")
	private final FileWatcher fileWatcher = new FileWatcher("JEI Config File Watcher");
	private final ConfigManager configManager;
	private final JeiClientConfigs jeiClientConfigs;

	public JeiStarter(StartData data) {
		ErrorUtil.checkNotEmpty(data.plugins(), "plugins");
		this.data = data;
		this.plugins = data.plugins();
		this.vanillaPlugin = PluginHelper.getPluginWithClass(VanillaPlugin.class, plugins)
			.orElseThrow(() -> new IllegalStateException("vanilla plugin not found"));
		JeiInternalPlugin jeiInternalPlugin = PluginHelper.getPluginWithClass(JeiInternalPlugin.class, plugins)
			.orElse(null);
		PluginHelper.sortPlugins(plugins, vanillaPlugin, jeiInternalPlugin);

		Path configDir = Services.PLATFORM.getConfigHelper().createJeiConfigDir();

		this.configManager = new ConfigManager();

		IConfigSchemaBuilder debugFileBuilder = new ConfigSchemaBuilder(configDir.resolve("jei-debug.ini"), "dainty.config.debug");
		DebugConfig.create(debugFileBuilder);
		debugFileBuilder.build().register(fileWatcher, configManager);

		IConfigSchemaBuilder modFileBuilder = new ConfigSchemaBuilder(configDir.resolve("jei-mod-id-format.ini"), "dainty.config.modIdFormat");
		this.modIdFormatConfig = new ModIdFormatConfig(modFileBuilder);
		modFileBuilder.build().register(fileWatcher, configManager);

		IConfigSchemaBuilder colorFileBuilder = new ConfigSchemaBuilder(configDir.resolve("jei-colors.ini"), "dainty.config.colors");
		this.colorNameConfig = new ColorNameConfig(colorFileBuilder);
		colorFileBuilder.build().register(fileWatcher, configManager);

		this.jeiClientConfigs = new JeiClientConfigs(configDir.resolve("jei-client.ini"));
		jeiClientConfigs.register(fileWatcher, configManager);
		Internal.setJeiClientConfigs(jeiClientConfigs);

		fileWatcher.start();

		this.recipeCategorySortingConfig = new RecipeCategorySortingConfig(configDir.resolve("recipe-category-sort-order.ini"));

		PluginCaller.callOnPlugins("Sending ConfigManager", plugins, p -> p.onConfigManagerAvailable(configManager));
	}

	public void start() {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) {
			LOGGER.error("Failed to start JEI, there is no Minecraft client level.");
			return;
		}
		RegistryAccess registryAccess = minecraft.level.registryAccess();
		RegistryUtil.setRegistryAccess(registryAccess);

		LoggedTimer totalTime = new LoggedTimer();
		totalTime.start("Starting JEI");
		this.configManager.onJeiStarted();

		IColorHelper colorHelper = new ColorHelper(colorNameConfig);
		IIngredientFilterConfig ingredientFilterConfig = jeiClientConfigs.getIngredientFilterConfig();
		SubtypeManager subtypeManager = PluginLoader.registerSubtypes(data);
		IIngredientManager ingredientManager = PluginLoader.registerIngredients(data, subtypeManager, colorHelper, ingredientFilterConfig);

		FocusFactory focusFactory = new FocusFactory(ingredientManager);
		CodecHelper codecHelper = new CodecHelper(ingredientManager, focusFactory);

		Path configDir = Services.PLATFORM.getConfigHelper().createJeiConfigDir();
		EditModeConfig.FileSerializer editModeSerializer = new EditModeConfig.FileSerializer(
			configDir.resolve("blacklist.json"),
			registryAccess,
			codecHelper
		);
		EditModeConfig editModeConfig = new EditModeConfig(editModeSerializer, ingredientManager);

		ImmutableSetMultimap<String, String> modAliases = PluginLoader.registerModAliases(data, ingredientFilterConfig);
		JeiHelpers jeiHelpers = PluginLoader.createJeiHelpers(modAliases, modIdFormatConfig, colorHelper, editModeConfig, focusFactory, codecHelper, ingredientManager, subtypeManager);

		RecipeManager recipeManager = PluginLoader.createRecipeManager(
			plugins,
			vanillaPlugin,
			recipeCategorySortingConfig,
			jeiHelpers,
			ingredientManager
		);
		IRecipeTransferManager recipeTransferManager = PluginLoader.createRecipeTransferManager(
			vanillaPlugin,
			plugins,
			jeiHelpers,
			data.serverConnection()
		);

		LoggedTimer timer = new LoggedTimer();
		timer.start("Building runtime");
		IScreenHelper screenHelper = PluginLoader.createGuiScreenHelper(plugins, jeiHelpers, ingredientManager);

		RuntimeRegistration runtimeRegistration = new RuntimeRegistration(
			recipeManager,
			jeiHelpers,
			editModeConfig,
			ingredientManager,
			recipeTransferManager,
			screenHelper
		);
		PluginCaller.callOnPlugins("Registering Runtime", plugins, p -> p.registerRuntime(runtimeRegistration));

		JeiRuntime jeiRuntime = new JeiRuntime(
			recipeManager,
			ingredientManager,
			data.keyBindings(),
			jeiHelpers,
			screenHelper,
			recipeTransferManager,
			editModeConfig,
			runtimeRegistration.getIngredientListOverlay(),
			runtimeRegistration.getBookmarkOverlay(),
			runtimeRegistration.getRecipesGui(),
			runtimeRegistration.getIngredientFilter(),
			configManager
		);
		timer.stop();

		PluginCaller.callOnPlugins("Sending Runtime", plugins, p -> p.onRuntimeAvailable(jeiRuntime));
		Internal.setRuntime(jeiRuntime);

		totalTime.stop();
	}

	public void stop() {
		LOGGER.info("Stopping JEI");
		List<IModPlugin> plugins = data.plugins();
		PluginCaller.callOnPlugins("Sending Runtime Unavailable", plugins, IModPlugin::onRuntimeUnavailable);
		Internal.setRuntime(null);
		RegistryUtil.setRegistryAccess(null);
	}
}
