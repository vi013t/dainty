package violet.dainty.features.recipeviewer.core.gui.recipes;

import violet.dainty.features.recipeviewer.core.commonapi.gui.IRecipeLayoutDrawable;

@FunctionalInterface
public interface IRecipeLayoutWithButtonsFactory {
	<T> RecipeLayoutWithButtons<T> create(IRecipeLayoutDrawable<T> recipeLayoutDrawable);
}
