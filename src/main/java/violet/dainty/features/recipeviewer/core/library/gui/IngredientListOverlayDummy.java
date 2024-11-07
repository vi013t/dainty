package violet.dainty.features.recipeviewer.core.library.gui;

import org.jetbrains.annotations.Nullable;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientListOverlay;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class IngredientListOverlayDummy implements IIngredientListOverlay{
	public static final IIngredientListOverlay INSTANCE = new IngredientListOverlayDummy();

	private IngredientListOverlayDummy() {

	}

	@Override
	public boolean isListDisplayed() {
		return false;
	}

	@Override
	public boolean hasKeyboardFocus() {
		return false;
	}

	@Override
	public Optional<ITypedIngredient<?>> getIngredientUnderMouse() {
		return Optional.empty();
	}

	@Nullable
	@Override
	public <T> T getIngredientUnderMouse(IIngredientType<T> ingredientType) {
		return null;
	}

	@Override
	public <T> List<T> getVisibleIngredients(IIngredientType<T> ingredientType) {
		return Collections.emptyList();
	}
}
