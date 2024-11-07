package violet.dainty.features.recipeviewer.core.library.load.registration;

import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRuntimeRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IBookmarkOverlay;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IEditModeConfig;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientFilter;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientListOverlay;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IRecipesGui;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.library.gui.BookmarkOverlayDummy;
import violet.dainty.features.recipeviewer.core.library.gui.IngredientListOverlayDummy;
import violet.dainty.features.recipeviewer.core.library.gui.recipes.RecipesGuiDummy;
import violet.dainty.features.recipeviewer.core.library.ingredients.IngredientFilterApiDummy;

public class RuntimeRegistration implements IRuntimeRegistration {
	private final IRecipeManager recipeManager;
	private final IJeiHelpers jeiHelpers;
	private final IEditModeConfig editModeConfig;
	private final IIngredientManager ingredientManager;
	private final IRecipeTransferManager recipeTransferManager;
	private final IScreenHelper screenHelper;

	private IIngredientListOverlay ingredientListOverlay = IngredientListOverlayDummy.INSTANCE;
	private IBookmarkOverlay bookmarkOverlay = BookmarkOverlayDummy.INSTANCE;
	private IRecipesGui recipesGui = RecipesGuiDummy.INSTANCE;
	private IIngredientFilter ingredientFilter = IngredientFilterApiDummy.INSTANCE;

	public RuntimeRegistration(
		IRecipeManager recipeManager,
		IJeiHelpers jeiHelpers,
		IEditModeConfig editModeConfig,
		IIngredientManager ingredientManager,
		IRecipeTransferManager recipeTransferManager,
		IScreenHelper screenHelper
	) {
		this.recipeManager = recipeManager;
		this.jeiHelpers = jeiHelpers;
		this.editModeConfig = editModeConfig;
		this.ingredientManager = ingredientManager;
		this.recipeTransferManager = recipeTransferManager;
		this.screenHelper = screenHelper;
	}

	@Override
	public void setIngredientListOverlay(IIngredientListOverlay ingredientListOverlay) {
		this.ingredientListOverlay = ingredientListOverlay;
	}

	@Override
	public void setBookmarkOverlay(IBookmarkOverlay bookmarkOverlay) {
		this.bookmarkOverlay = bookmarkOverlay;
	}

	@Override
	public void setRecipesGui(IRecipesGui recipesGui) {
		this.recipesGui = recipesGui;
	}

	@Override
	public void setIngredientFilter(IIngredientFilter ingredientFilter) {
		this.ingredientFilter = ingredientFilter;
	}

	@Override
	public IRecipeManager getRecipeManager() {
		return this.recipeManager;
	}

	@Override
	public IJeiHelpers getJeiHelpers() {
		return this.jeiHelpers;
	}

	@Override
	public IIngredientManager getIngredientManager() {
		return this.ingredientManager;
	}

	@Override
	public IScreenHelper getScreenHelper() {
		return this.screenHelper;
	}

	@Override
	public IRecipeTransferManager getRecipeTransferManager() {
		return this.recipeTransferManager;
	}

	@Override
	public IEditModeConfig getEditModeConfig() {
		return this.editModeConfig;
	}

	public IIngredientListOverlay getIngredientListOverlay() {
		return ingredientListOverlay;
	}

	public IBookmarkOverlay getBookmarkOverlay() {
		return bookmarkOverlay;
	}

	public IRecipesGui getRecipesGui() {
		return recipesGui;
	}

	public IIngredientFilter getIngredientFilter() {
		return this.ingredientFilter;
	}
}
