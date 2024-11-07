package violet.dainty.features.recipeviewer.core.gui.recipes.lookups;

import org.jetbrains.annotations.Unmodifiable;

import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

import java.util.List;

public record StaticFocusedRecipes<T>(
	IRecipeCategory<T> recipeCategory,
	List<T> recipes
) implements IFocusedRecipes<T> {
	@Override
	public IRecipeCategory<T> getRecipeCategory() {
		return recipeCategory;
	}

	@Override
	public @Unmodifiable List<T> getRecipes() {
		return recipes;
	}
}
