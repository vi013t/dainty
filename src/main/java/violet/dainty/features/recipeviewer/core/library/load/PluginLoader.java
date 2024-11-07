package violet.dainty.features.recipeviewer.core.library.load;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSetMultimap;

import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.config.IIngredientFilterConfig;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformFluidHelperInternal;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.StackHelper;
import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IColorHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IModIdHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IStackHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.IRecipeManagerPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.IRecipeCategoryDecorator;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferHandlerHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientVisibility;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiFeatures;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.core.util.LoggedTimer;
import violet.dainty.features.recipeviewer.core.library.config.EditModeConfig;
import violet.dainty.features.recipeviewer.core.library.config.IModIdFormatConfig;
import violet.dainty.features.recipeviewer.core.library.config.RecipeCategorySortingConfig;
import violet.dainty.features.recipeviewer.core.library.focus.FocusFactory;
import violet.dainty.features.recipeviewer.core.library.gui.helpers.GuiHelper;
import violet.dainty.features.recipeviewer.core.library.helpers.CodecHelper;
import violet.dainty.features.recipeviewer.core.library.helpers.ModIdHelper;
import violet.dainty.features.recipeviewer.core.library.ingredients.IngredientBlacklistInternal;
import violet.dainty.features.recipeviewer.core.library.ingredients.IngredientVisibility;
import violet.dainty.features.recipeviewer.core.library.ingredients.subtypes.SubtypeInterpreters;
import violet.dainty.features.recipeviewer.core.library.ingredients.subtypes.SubtypeManager;
import violet.dainty.features.recipeviewer.core.library.load.registration.AdvancedRegistration;
import violet.dainty.features.recipeviewer.core.library.load.registration.GuiHandlerRegistration;
import violet.dainty.features.recipeviewer.core.library.load.registration.IngredientManagerBuilder;
import violet.dainty.features.recipeviewer.core.library.load.registration.ModInfoRegistration;
import violet.dainty.features.recipeviewer.core.library.load.registration.RecipeCatalystRegistration;
import violet.dainty.features.recipeviewer.core.library.load.registration.RecipeCategoryRegistration;
import violet.dainty.features.recipeviewer.core.library.load.registration.RecipeManagerPluginHelper;
import violet.dainty.features.recipeviewer.core.library.load.registration.RecipeRegistration;
import violet.dainty.features.recipeviewer.core.library.load.registration.RecipeTransferRegistration;
import violet.dainty.features.recipeviewer.core.library.load.registration.SubtypeRegistration;
import violet.dainty.features.recipeviewer.core.library.load.registration.VanillaCategoryExtensionRegistration;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.VanillaPlugin;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.VanillaRecipeFactory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.anvil.SmithingRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.CraftingRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.recipes.RecipeManager;
import violet.dainty.features.recipeviewer.core.library.recipes.RecipeManagerInternal;
import violet.dainty.features.recipeviewer.core.library.runtime.JeiHelpers;
import violet.dainty.features.recipeviewer.core.library.startup.StartData;
import violet.dainty.features.recipeviewer.core.library.transfer.RecipeTransferHandlerHelper;

import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public final class PluginLoader {
	private PluginLoader() {}

	public static SubtypeManager registerSubtypes(StartData data) {
		IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
		List<IModPlugin> plugins = data.plugins();
		SubtypeRegistration subtypeRegistration = new SubtypeRegistration();
		PluginCaller.callOnPlugins("Registering item subtypes", plugins, p -> p.registerItemSubtypes(subtypeRegistration));
		PluginCaller.callOnPlugins("Registering fluid subtypes", plugins, p ->
			p.registerFluidSubtypes(subtypeRegistration, fluidHelper)
		);
		SubtypeInterpreters subtypeInterpreters = subtypeRegistration.getInterpreters();
		return new SubtypeManager(subtypeInterpreters);
	}

	public static IIngredientManager registerIngredients(StartData data, SubtypeManager subtypeManager, IColorHelper colorHelper, IIngredientFilterConfig ingredientFilterConfig) {
		List<IModPlugin> plugins = data.plugins();
		IngredientManagerBuilder ingredientManagerBuilder = new IngredientManagerBuilder(subtypeManager, colorHelper);
		PluginCaller.callOnPlugins("Registering ingredients", plugins, p -> p.registerIngredients(ingredientManagerBuilder));
		PluginCaller.callOnPlugins("Registering extra ingredients", plugins, p -> p.registerExtraIngredients(ingredientManagerBuilder));

		if (ingredientFilterConfig.getSearchIngredientAliases()) {
			PluginCaller.callOnPlugins("Registering search ingredient aliases", plugins, p -> p.registerIngredientAliases(ingredientManagerBuilder));
		}
		return ingredientManagerBuilder.build();
	}

	public static ImmutableSetMultimap<String, String> registerModAliases(
		StartData data,
		IIngredientFilterConfig ingredientFilterConfig
	) {
		List<IModPlugin> plugins = data.plugins();
		if (!ingredientFilterConfig.getSearchModAliases()) {
			return ImmutableSetMultimap.of();
		}
		ModInfoRegistration modInfoRegistration = new ModInfoRegistration();
		PluginCaller.callOnPlugins("Registering Mod Info", plugins, p -> p.registerModInfo(modInfoRegistration));
		return modInfoRegistration.getModAliases();
	}

	public static JeiHelpers createJeiHelpers(
		ImmutableSetMultimap<String, String> modAliases,
		IModIdFormatConfig modIdFormatConfig,
		IColorHelper colorHelper,
		EditModeConfig editModeConfig,
		FocusFactory focusFactory,
		CodecHelper codecHelper,
		IIngredientManager ingredientManager,
		SubtypeManager subtypeManager
	) {
		VanillaRecipeFactory vanillaRecipeFactory = new VanillaRecipeFactory(ingredientManager);
		StackHelper stackHelper = new StackHelper(subtypeManager);
		GuiHelper guiHelper = new GuiHelper(ingredientManager);
		IModIdHelper modIdHelper = new ModIdHelper(modIdFormatConfig, ingredientManager, modAliases);

		IClientToggleState toggleState = Internal.getClientToggleState();
		IngredientBlacklistInternal blacklist = new IngredientBlacklistInternal();
		ingredientManager.registerIngredientListener(blacklist);

		IIngredientVisibility ingredientVisibility = new IngredientVisibility(
			blacklist,
			toggleState,
			editModeConfig,
			ingredientManager
		);

		return new JeiHelpers(guiHelper, stackHelper, modIdHelper, focusFactory, colorHelper, ingredientManager, vanillaRecipeFactory, codecHelper, ingredientVisibility);
	}

	@Unmodifiable
	private static List<IRecipeCategory<?>> createRecipeCategories(List<IModPlugin> plugins, VanillaPlugin vanillaPlugin, JeiHelpers jeiHelpers) {
		RecipeCategoryRegistration recipeCategoryRegistration = new RecipeCategoryRegistration(jeiHelpers);
		PluginCaller.callOnPlugins("Registering categories", plugins, p -> p.registerCategories(recipeCategoryRegistration));
		CraftingRecipeCategory craftingCategory = vanillaPlugin.getCraftingCategory()
			.orElseThrow(() -> new NullPointerException("vanilla crafting category"));
		SmithingRecipeCategory smithingCategory = vanillaPlugin.getSmithingCategory()
			.orElseThrow(() -> new NullPointerException("vanilla smithing category"));
		VanillaCategoryExtensionRegistration vanillaCategoryExtensionRegistration = new VanillaCategoryExtensionRegistration(craftingCategory, smithingCategory, jeiHelpers);
		PluginCaller.callOnPlugins("Registering vanilla category extensions", plugins, p -> p.registerVanillaCategoryExtensions(vanillaCategoryExtensionRegistration));
		return recipeCategoryRegistration.getRecipeCategories();
	}

	public static IScreenHelper createGuiScreenHelper(List<IModPlugin> plugins, IJeiHelpers jeiHelpers, IIngredientManager ingredientManager) {
		GuiHandlerRegistration guiHandlerRegistration = new GuiHandlerRegistration(jeiHelpers);
		PluginCaller.callOnPlugins("Registering gui handlers", plugins, p -> p.registerGuiHandlers(guiHandlerRegistration));
		return guiHandlerRegistration.createGuiScreenHelper(ingredientManager);
	}

	public static IRecipeTransferManager createRecipeTransferManager(
		VanillaPlugin vanillaPlugin,
		List<IModPlugin> plugins,
		JeiHelpers jeiHelpers,
		IConnectionToServer connectionToServer
	) {
		IStackHelper stackHelper = jeiHelpers.getStackHelper();
		CraftingRecipeCategory craftingCategory = vanillaPlugin.getCraftingCategory()
			.orElseThrow(() -> new NullPointerException("vanilla crafting category"));
		IRecipeTransferHandlerHelper handlerHelper = new RecipeTransferHandlerHelper(stackHelper, craftingCategory);
		RecipeTransferRegistration recipeTransferRegistration = new RecipeTransferRegistration(stackHelper, handlerHelper, jeiHelpers, connectionToServer);
		PluginCaller.callOnPlugins("Registering recipes transfer handlers", plugins, p -> p.registerRecipeTransferHandlers(recipeTransferRegistration));
		return recipeTransferRegistration.createRecipeTransferManager();
	}

	public static RecipeManager createRecipeManager(
		List<IModPlugin> plugins,
		VanillaPlugin vanillaPlugin,
		RecipeCategorySortingConfig recipeCategorySortingConfig,
		JeiHelpers jeiHelpers,
		IIngredientManager ingredientManager
	) {
		List<IRecipeCategory<?>> recipeCategories = createRecipeCategories(plugins, vanillaPlugin, jeiHelpers);

		RecipeCatalystRegistration recipeCatalystRegistration = new RecipeCatalystRegistration(ingredientManager, jeiHelpers);
		PluginCaller.callOnPlugins("Registering recipe catalysts", plugins, p -> p.registerRecipeCatalysts(recipeCatalystRegistration));
		ImmutableListMultimap<RecipeType<?>, ITypedIngredient<?>> recipeCatalysts = recipeCatalystRegistration.getRecipeCatalysts();

		LoggedTimer timer = new LoggedTimer();
		timer.start("Building recipe registry");
		RecipeManagerInternal recipeManagerInternal = new RecipeManagerInternal(
			recipeCategories,
			recipeCatalysts,
			ingredientManager,
			recipeCategorySortingConfig,
			jeiHelpers.getIngredientVisibility()
		);
		timer.stop();

		IJeiFeatures jeiFeatures = Internal.getJeiFeatures();
		RecipeManagerPluginHelper recipeManagerPluginHelper = new RecipeManagerPluginHelper(recipeManagerInternal);
		AdvancedRegistration advancedRegistration = new AdvancedRegistration(jeiHelpers, jeiFeatures, recipeManagerPluginHelper);
		PluginCaller.callOnPlugins("Registering advanced plugins", plugins, p -> p.registerAdvanced(advancedRegistration));

		List<IRecipeManagerPlugin> recipeManagerPlugins = advancedRegistration.getRecipeManagerPlugins();
		ImmutableListMultimap<RecipeType<?>, IRecipeCategoryDecorator<?>> recipeCategoryDecorators = advancedRegistration.getRecipeCategoryDecorators();
		recipeManagerInternal.addPlugins(recipeManagerPlugins);
		recipeManagerInternal.addDecorators(recipeCategoryDecorators);

		RecipeRegistration recipeRegistration = new RecipeRegistration(jeiHelpers, ingredientManager, recipeManagerInternal);
		PluginCaller.callOnPlugins("Registering recipes", plugins, p -> p.registerRecipes(recipeRegistration));

		recipeManagerInternal.compact();

		return new RecipeManager(recipeManagerInternal, ingredientManager);
	}
}
