package violet.dainty.features.recipeviewer.core.gui.recipes;

import java.util.Optional;

import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableText;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

public class RecipeCategoryIconUtil {
	public static <T> IDrawable create(
		IRecipeCategory<T> recipeCategory,
		IRecipeManager recipeManager,
		IGuiHelper guiHelper
	) {
		IDrawable icon = recipeCategory.getIcon();
		if (icon != null) {
			return icon;
		}
		RecipeType<T> recipeType = recipeCategory.getRecipeType();
		Optional<ITypedIngredient<?>> firstCatalyst = recipeManager.createRecipeCatalystLookup(recipeType)
			.get()
			.findFirst();

		if (firstCatalyst.isPresent()) {
			ITypedIngredient<?> ingredient = firstCatalyst.get();
			return guiHelper.createDrawableIngredient(ingredient);
		} else {
			Component title = recipeCategory.getTitle();
			String text = title.getString().substring(0, 2);
			return new DrawableText(text, 16, 16, 0xE0E0E0);
		}
	}
}
