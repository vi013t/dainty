package violet.dainty.features.recipeviewer.core.library.gui.recipes;

import java.util.List;
import java.util.Map;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientSupplier;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.library.gui.recipes.supplier.builder.IngredientSlotBuilder;

public class RecipeLayoutIngredientSupplier implements IIngredientSupplier {
	private final Map<RecipeIngredientRole, IngredientSlotBuilder> ingredientSlotBuilders;

	public RecipeLayoutIngredientSupplier(Map<RecipeIngredientRole, IngredientSlotBuilder> ingredientSlotBuilders) {
		this.ingredientSlotBuilders = ingredientSlotBuilders;
	}

	@Override
	public List<ITypedIngredient<?>> getIngredients(RecipeIngredientRole role) {
		IngredientSlotBuilder ingredientSlotBuilder = ingredientSlotBuilders.get(role);
		if (ingredientSlotBuilder == null) {
			return List.of();
		}
		return ingredientSlotBuilder.getAllIngredients();
	}
}
