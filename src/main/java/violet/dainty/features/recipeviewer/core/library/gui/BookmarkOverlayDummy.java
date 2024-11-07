package violet.dainty.features.recipeviewer.core.library.gui;

import org.jetbrains.annotations.Nullable;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IBookmarkOverlay;

import java.util.Optional;

public class BookmarkOverlayDummy implements IBookmarkOverlay {
	public static final IBookmarkOverlay INSTANCE = new BookmarkOverlayDummy();

	private BookmarkOverlayDummy() {

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
}
