package violet.dainty.features.recipeviewer.core.gui.recipes.lookups;

import java.util.List;

import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipeLayoutWithButtons;
import violet.dainty.features.recipeviewer.core.gui.recipes.layouts.IRecipeLayoutList;

public interface ILookupState {
	List<IRecipeCategory<?>> getRecipeCategories();

	boolean moveToRecipeCategory(IRecipeCategory<?> recipeCategory);

	int getRecipesPerPage();

	void setRecipesPerPage(int recipesPerPage);

	int getRecipeIndex();

	IFocusGroup getFocuses();

	IFocusedRecipes<?> getFocusedRecipes();

	void nextRecipeCategory();

	void previousRecipeCategory();

	void goToFirstPage();

	void nextPage();

	void previousPage();

	int pageCount();

	default List<RecipeLayoutWithButtons<?>> getVisible(IRecipeLayoutList recipes) {
		final int recipesPerPage = getRecipesPerPage();
		final int firstRecipeIndex = getRecipeIndex() - (getRecipeIndex() % recipesPerPage);
		final int maxIndex = Math.min(recipes.size(), firstRecipeIndex + recipesPerPage);
		if (firstRecipeIndex >= maxIndex) {
			return List.of();
		}
		return recipes.subList(firstRecipeIndex, maxIndex);
	}
}
