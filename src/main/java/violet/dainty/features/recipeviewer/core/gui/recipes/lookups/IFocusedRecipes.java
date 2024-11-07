package violet.dainty.features.recipeviewer.core.gui.recipes.lookups;

import org.jetbrains.annotations.Unmodifiable;

import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

import java.util.List;

public interface IFocusedRecipes<T> {
	IRecipeCategory<T> getRecipeCategory();

	@Unmodifiable
	List<T> getRecipes();
}
