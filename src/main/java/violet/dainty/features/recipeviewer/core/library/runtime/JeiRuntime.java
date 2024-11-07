package violet.dainty.features.recipeviewer.core.library.runtime;

import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IBookmarkOverlay;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IEditModeConfig;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientFilter;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientListOverlay;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiKeyMappings;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiRuntime;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IRecipesGui;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.config.IJeiConfigManager;


public class JeiRuntime implements IJeiRuntime {
	private final IRecipeManager recipeManager;
	private final IRecipeTransferManager recipeTransferManager;
	private final IEditModeConfig editModeConfig;
	private final IIngredientManager ingredientManager;
	private final IJeiKeyMappings keyMappings;
	private final IJeiHelpers jeiHelpers;
	private final IScreenHelper screenHelper;
	private final IJeiConfigManager configManager;
	private final IIngredientListOverlay ingredientListOverlay;
	private final IBookmarkOverlay bookmarkOverlay;
	private final IRecipesGui recipesGui;
	private final IIngredientFilter ingredientFilter;

	public JeiRuntime(
		IRecipeManager recipeManager,
		IIngredientManager ingredientManager,
		IJeiKeyMappings keyMappings,
		IJeiHelpers jeiHelpers,
		IScreenHelper screenHelper,
		IRecipeTransferManager recipeTransferManager,
		IEditModeConfig editModeConfig,
		IIngredientListOverlay ingredientListOverlay,
		IBookmarkOverlay bookmarkOverlay,
		IRecipesGui recipesGui,
		IIngredientFilter ingredientFilter,
		IJeiConfigManager configManager
	) {
		this.recipeManager = recipeManager;
		this.recipeTransferManager = recipeTransferManager;
		this.editModeConfig = editModeConfig;
		this.ingredientListOverlay = ingredientListOverlay;
		this.bookmarkOverlay = bookmarkOverlay;
		this.recipesGui = recipesGui;
		this.ingredientFilter = ingredientFilter;
		this.ingredientManager = ingredientManager;
		this.keyMappings = keyMappings;
		this.jeiHelpers = jeiHelpers;
		this.screenHelper = screenHelper;
		this.configManager = configManager;
	}

	@Override
	public IRecipeManager getRecipeManager() {
		return recipeManager;
	}

	@Override
	public IIngredientFilter getIngredientFilter() {
		return ingredientFilter;
	}

	@Override
	public IIngredientListOverlay getIngredientListOverlay() {
		return ingredientListOverlay;
	}

	@Override
	public IIngredientManager getIngredientManager() {
		return ingredientManager;
	}

	@Override
	public IBookmarkOverlay getBookmarkOverlay() {
		return bookmarkOverlay;
	}

	@Override
	public IJeiHelpers getJeiHelpers() {
		return jeiHelpers;
	}

	@Override
	public IRecipesGui getRecipesGui() {
		return recipesGui;
	}

	@Override
	public IJeiKeyMappings getKeyMappings() {
		return keyMappings;
	}

	@Override
	public IScreenHelper getScreenHelper() {
		return screenHelper;
	}

	@Override
	public IRecipeTransferManager getRecipeTransferManager() {
		return recipeTransferManager;
	}

	@Override
	public IEditModeConfig getEditModeConfig() {
		return editModeConfig;
	}

	@Override
	public IJeiConfigManager getConfigManager() {
		return configManager;
	}
}
