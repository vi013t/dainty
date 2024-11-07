package violet.dainty.features.recipeviewer.core.library.load.registration;

import java.util.List;

import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiIngredientInfoRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IVanillaRecipeFactory;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.plugins.jei.info.IngredientInfoRecipe;
import violet.dainty.features.recipeviewer.core.library.recipes.RecipeManagerInternal;

public class RecipeRegistration implements IRecipeRegistration {
	private final IJeiHelpers jeiHelpers;
	private final IIngredientManager ingredientManager;
	private final RecipeManagerInternal recipeManager;

	public RecipeRegistration(
		IJeiHelpers jeiHelpers,
		IIngredientManager ingredientManager,
		RecipeManagerInternal recipeManager
	) {
		this.jeiHelpers = jeiHelpers;
		this.ingredientManager = ingredientManager;
		this.recipeManager = recipeManager;
	}

	@Override
	public IJeiHelpers getJeiHelpers() {
		return jeiHelpers;
	}

	@Override
	public IIngredientManager getIngredientManager() {
		return ingredientManager;
	}

	@Override
	public IVanillaRecipeFactory getVanillaRecipeFactory() {
		return jeiHelpers.getVanillaRecipeFactory();
	}

	@Override
	public <T> void addRecipes(RecipeType<T> recipeType, List<T> recipes) {
		ErrorUtil.checkNotNull(recipeType, "recipeType");
		ErrorUtil.checkNotNull(recipes, "recipes");
		this.recipeManager.addRecipes(recipeType, recipes);
	}

	@Override
	public <T> void addIngredientInfo(T ingredient, IIngredientType<T> ingredientType, Component... descriptionComponents) {
		ErrorUtil.checkNotNull(ingredient, "ingredient");
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotEmpty(descriptionComponents, "descriptionComponents");

		addIngredientInfo(List.of(ingredient), ingredientType, descriptionComponents);
	}

	@Override
	public <T> void addIngredientInfo(List<T> ingredients, IIngredientType<T> ingredientType, Component... descriptionComponents) {
		ErrorUtil.checkNotEmpty(ingredients, "ingredients");
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotEmpty(descriptionComponents, "descriptionComponents");

		IJeiIngredientInfoRecipe recipe = IngredientInfoRecipe.create(ingredientManager, ingredients, ingredientType, descriptionComponents);
		addRecipes(RecipeTypes.INFORMATION, List.of(recipe));
	}
}
