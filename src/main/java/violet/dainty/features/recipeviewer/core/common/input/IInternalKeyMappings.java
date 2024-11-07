package violet.dainty.features.recipeviewer.core.common.input;

import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiKeyMapping;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiKeyMappings;

public interface IInternalKeyMappings extends IJeiKeyMappings {
	IJeiKeyMapping getToggleOverlay();
	IJeiKeyMapping getFocusSearch();
	IJeiKeyMapping getToggleCheatMode();
	IJeiKeyMapping getToggleEditMode();

	IJeiKeyMapping getToggleCheatModeConfigButton();

	IJeiKeyMapping getRecipeBack();
	IJeiKeyMapping getPreviousCategory();
	IJeiKeyMapping getNextCategory();
	IJeiKeyMapping getPreviousRecipePage();
	IJeiKeyMapping getNextRecipePage();

	IJeiKeyMapping getPreviousPage();
	IJeiKeyMapping getNextPage();

	IJeiKeyMapping getCloseRecipeGui();

	IJeiKeyMapping getBookmark();
	IJeiKeyMapping getToggleBookmarkOverlay();

	@Override
	IJeiKeyMapping getShowRecipe();

	@Override
	IJeiKeyMapping getShowUses();

	IJeiKeyMapping getTransferRecipeBookmark();
	IJeiKeyMapping getMaxTransferRecipeBookmark();

	IJeiKeyMapping getCheatOneItem();
	IJeiKeyMapping getCheatItemStack();

	IJeiKeyMapping getToggleHideIngredient();
	IJeiKeyMapping getToggleWildcardHideIngredient();

	IJeiKeyMapping getHoveredClearSearchBar();
	IJeiKeyMapping getPreviousSearch();
	IJeiKeyMapping getNextSearch();

	IJeiKeyMapping getCopyRecipeId();

	// internal only, unregistered and can't be changed because they match vanilla Minecraft hard-coded keys:
	IJeiKeyMapping getEscapeKey();
	IJeiKeyMapping getLeftClick();
	IJeiKeyMapping getRightClick();
	IJeiKeyMapping getEnterKey();
}
