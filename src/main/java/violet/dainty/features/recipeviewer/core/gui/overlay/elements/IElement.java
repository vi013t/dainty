package violet.dainty.features.recipeviewer.core.gui.overlay.elements;

import org.jetbrains.annotations.Nullable;

import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IRecipesGui;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.IBookmark;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientGridTooltipHelper;
import violet.dainty.features.recipeviewer.core.gui.util.FocusUtil;

import java.util.List;
import java.util.Optional;

public interface IElement<T> {
	ITypedIngredient<T> getTypedIngredient();

	/**
	 * @return the bookmark if this element represents an existing bookmark.
	 */
	Optional<IBookmark> getBookmark();

	@Nullable
	IDrawable createRenderOverlay();

	void show(IRecipesGui recipesGui, FocusUtil focusUtil, List<RecipeIngredientRole> roles);

	void getTooltip(JeiTooltip tooltip, IngredientGridTooltipHelper tooltipHelper, IIngredientRenderer<T> ingredientRenderer, IIngredientHelper<T> ingredientHelper);

	boolean isVisible();

	default boolean handleClick(UserInput input, IInternalKeyMappings keyBindings) {
		return false;
	}
}
