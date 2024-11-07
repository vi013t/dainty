package violet.dainty.features.recipeviewer.core.gui.bookmarks;

import java.util.Objects;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IElement;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IngredientBookmarkElement;

public class IngredientBookmark<T> implements IBookmark {
	private final IElement<T> element;
	private final Object uid;
	private final ITypedIngredient<T> typedIngredient;
	private boolean visible = true;

	public static <T> IngredientBookmark<T> create(ITypedIngredient<T> typedIngredient, IIngredientManager ingredientManager) {
		IIngredientType<T> type = typedIngredient.getType();
		typedIngredient = ingredientManager.normalizeTypedIngredient(typedIngredient);
		IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(type);
		Object uniqueId = ingredientHelper.getUid(typedIngredient, UidContext.Ingredient);
		return new IngredientBookmark<>(typedIngredient, uniqueId);
	}

	private IngredientBookmark(ITypedIngredient<T> typedIngredient, Object uid) {
		this.typedIngredient = typedIngredient;
		this.uid = uid;
		this.element = new IngredientBookmarkElement<>(this);
	}

	@Override
	public BookmarkType getType() {
		return BookmarkType.INGREDIENT;
	}

	public ITypedIngredient<T> getIngredient() {
		return typedIngredient;
	}

	@Override
	public IElement<?> getElement() {
		return element;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uid, typedIngredient.getType());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof IngredientBookmark<?> ingredientBookmark) {
			return ingredientBookmark.uid.equals(uid) &&
				ingredientBookmark.typedIngredient.getType().equals(typedIngredient.getType());
		}
		return false;
	}

	@Override
	public String toString() {
		return "IngredientBookmark{" +
			"uid=" + uid +
			", typedIngredient=" + typedIngredient +
			", visible=" + visible +
			'}';
	}
}
