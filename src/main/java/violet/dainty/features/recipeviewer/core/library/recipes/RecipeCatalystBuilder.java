package violet.dainty.features.recipeviewer.core.library.recipes;

import com.google.common.collect.ImmutableListMultimap;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.recipes.collect.RecipeMap;

import java.util.List;

public class RecipeCatalystBuilder {
	private final ImmutableListMultimap.Builder<IRecipeCategory<?>, ITypedIngredient<?>> recipeCategoryCatalystsBuilder = ImmutableListMultimap.builder();
	private final RecipeMap recipeCatalystMap;

	public RecipeCatalystBuilder(RecipeMap recipeCatalystMap) {
		this.recipeCatalystMap = recipeCatalystMap;
	}

	public void addCategoryCatalysts(IRecipeCategory<?> recipeCategory, List<ITypedIngredient<?>> categoryCatalystIngredients) {
		recipeCategoryCatalystsBuilder.putAll(recipeCategory, categoryCatalystIngredients);
		for (ITypedIngredient<?> catalystIngredient : categoryCatalystIngredients) {
			addCategoryCatalyst(catalystIngredient, recipeCategory);
		}
	}

	private <T> void addCategoryCatalyst(ITypedIngredient<T> catalystIngredient, IRecipeCategory<?> recipeCategory) {
		RecipeType<?> recipeType = recipeCategory.getRecipeType();
		recipeCatalystMap.addCatalystForCategory(recipeType, catalystIngredient);
	}

	public ImmutableListMultimap<IRecipeCategory<?>, ITypedIngredient<?>> buildRecipeCategoryCatalysts() {
		return recipeCategoryCatalystsBuilder.build();
	}
}
