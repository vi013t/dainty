package violet.dainty.features.recipeviewer.core.gui.overlay.elements;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IRecipesGui;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.IBookmark;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.IngredientBookmark;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientGridTooltipHelper;
import violet.dainty.features.recipeviewer.core.gui.util.FocusUtil;

public class IngredientBookmarkElement<T> implements IElement<T> {
	private final IngredientBookmark<T> bookmark;

	public IngredientBookmarkElement(IngredientBookmark<T> bookmark) {
		this.bookmark = bookmark;
	}

	@Override
	public ITypedIngredient<T> getTypedIngredient() {
		return bookmark.getIngredient();
	}

	@Override
	public Optional<IBookmark> getBookmark() {
		return Optional.of(bookmark);
	}

	@Override
	public @Nullable IDrawable createRenderOverlay() {
		return null;
	}

	@Override
	public void show(IRecipesGui recipesGui, FocusUtil focusUtil, List<RecipeIngredientRole> roles) {
		ITypedIngredient<?> ingredient = getTypedIngredient();
		List<IFocus<?>> focuses = focusUtil.createFocuses(ingredient, roles);
		recipesGui.show(focuses);
	}

	@Override
	public void getTooltip(JeiTooltip tooltip, IngredientGridTooltipHelper tooltipHelper, IIngredientRenderer<T> ingredientRenderer, IIngredientHelper<T> ingredientHelper) {
		ITypedIngredient<T> ingredient = bookmark.getIngredient();
		tooltipHelper.getIngredientTooltip(tooltip, ingredient, ingredientRenderer, ingredientHelper);
	}

	@Override
	public boolean isVisible() {
		return bookmark.isVisible();
	}
}
