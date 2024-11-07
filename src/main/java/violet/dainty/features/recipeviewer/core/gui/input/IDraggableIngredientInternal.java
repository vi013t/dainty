package violet.dainty.features.recipeviewer.core.gui.input;

import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IElement;


public interface IDraggableIngredientInternal<T> {
	ITypedIngredient<T> getTypedIngredient();

	IElement<T> getElement();

	ImmutableRect2i getArea();
}
