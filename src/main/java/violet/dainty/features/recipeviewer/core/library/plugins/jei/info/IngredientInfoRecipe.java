package violet.dainty.features.recipeviewer.core.library.plugins.jei.info;

import java.util.Collections;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import violet.dainty.features.recipeviewer.core.common.util.StringUtil;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiIngredientInfoRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.ingredients.TypedIngredient;

public class IngredientInfoRecipe implements IJeiIngredientInfoRecipe {
	private final List<FormattedText> description;
	private final List<ITypedIngredient<?>> ingredients;

	public static <T> IJeiIngredientInfoRecipe create(
		IIngredientManager ingredientManager,
		List<T> ingredients,
		IIngredientType<T> ingredientType,
		Component... descriptionComponents
	) {
		List<ITypedIngredient<T>> typedIngredients = TypedIngredient.createAndFilterInvalidNonnullList(ingredientManager, ingredientType, ingredients, true);
		List<FormattedText> descriptionLines = StringUtil.expandNewlines(descriptionComponents);
		return new IngredientInfoRecipe(typedIngredients, descriptionLines);
	}

	private IngredientInfoRecipe(List<? extends ITypedIngredient<?>> ingredients, List<FormattedText> description) {
		this.description = description;
		this.ingredients = Collections.unmodifiableList(ingredients);
	}

	@Override
	public List<FormattedText> getDescription() {
		return description;
	}

	@Override
	public List<ITypedIngredient<?>> getIngredients() {
		return ingredients;
	}
}
