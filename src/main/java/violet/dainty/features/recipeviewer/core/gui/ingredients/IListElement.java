package violet.dainty.features.recipeviewer.core.gui.ingredients;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

public interface IListElement<V> {
	ITypedIngredient<V> getTypedIngredient();

	int getSortedIndex();

	void setSortedIndex(int sortIndex);

	int getCreatedIndex();

	boolean isVisible();

	void setVisible(boolean visible);
}
