package violet.dainty.features.recipeviewer.core.library.recipes;

import java.util.Optional;
import java.util.stream.Stream;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeCatalystLookup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;

public class RecipeCatalystLookup implements IRecipeCatalystLookup {
	private final RecipeType<?> recipeType;
	private final RecipeManagerInternal recipeManager;
	private boolean includeHidden;

	public RecipeCatalystLookup(RecipeType<?> recipeType, RecipeManagerInternal recipeManager) {
		this.recipeType = recipeType;
		this.recipeManager = recipeManager;
	}

	@Override
	public IRecipeCatalystLookup includeHidden() {
		this.includeHidden = true;
		return this;
	}

	@Override
	public Stream<ITypedIngredient<?>> get() {
		return recipeManager.getRecipeCatalystStream(recipeType, includeHidden);
	}

	@Override
	public <V> Stream<V> get(IIngredientType<V> ingredientType) {
		return get()
			.map(i -> i.getIngredient(ingredientType))
			.flatMap(Optional::stream);
	}
}
