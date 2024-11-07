package violet.dainty.features.recipeviewer.core.library.recipes;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Stream;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.IRecipeManagerPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.focus.Focus;
import violet.dainty.features.recipeviewer.core.library.recipes.collect.RecipeMap;
import violet.dainty.features.recipeviewer.core.library.recipes.collect.RecipeTypeData;
import violet.dainty.features.recipeviewer.core.library.recipes.collect.RecipeTypeDataMap;

public class InternalRecipeManagerPlugin implements IRecipeManagerPlugin {
	private final IIngredientManager ingredientManager;
	private final RecipeTypeDataMap recipeCategoriesMap;
	private final EnumMap<RecipeIngredientRole, RecipeMap> recipeMaps;

	public InternalRecipeManagerPlugin(
		IIngredientManager ingredientManager,
		RecipeTypeDataMap recipeCategoriesMap,
		EnumMap<RecipeIngredientRole, RecipeMap> recipeMaps
	) {
		this.ingredientManager = ingredientManager;
		this.recipeCategoriesMap = recipeCategoriesMap;
		this.recipeMaps = recipeMaps;
	}

	@Override
	public <V> List<RecipeType<?>> getRecipeTypes(IFocus<V> focus) {
		focus = Focus.checkOne(focus, ingredientManager);
		ITypedIngredient<V> ingredient = focus.getTypedValue();
		RecipeIngredientRole role = focus.getRole();
		RecipeMap recipeMap = this.recipeMaps.get(role);
		return recipeMap.getRecipeTypes(ingredient)
			.toList();
	}

	@Override
	public <T, V> List<T> getRecipes(IRecipeCategory<T> recipeCategory, IFocus<V> focus) {
		focus = Focus.checkOne(focus, ingredientManager);
		ITypedIngredient<V> ingredient = focus.getTypedValue();
		RecipeIngredientRole role = focus.getRole();

		RecipeMap recipeMap = this.recipeMaps.get(role);
		RecipeType<T> recipeType = recipeCategory.getRecipeType();
		List<T> recipes = recipeMap.getRecipes(recipeType, ingredient);
		if (recipeMap.isCatalystForRecipeCategory(recipeType, ingredient)) {
			List<T> recipesForCategory = getRecipes(recipeCategory);
			return Stream.concat(recipes.stream(), recipesForCategory.stream())
				.distinct()
				.toList();
		}
		return recipes;
	}

	@Override
	public <T> List<T> getRecipes(IRecipeCategory<T> recipeCategory) {
		RecipeType<T> recipeType = recipeCategory.getRecipeType();
		RecipeTypeData<T> recipeTypeData = recipeCategoriesMap.get(recipeType);
		return recipeTypeData.getRecipes();
	}
}
