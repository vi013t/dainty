package violet.dainty.features.recipeviewer.core.gui.ingredients;

import java.util.List;

import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientFilter;
import violet.dainty.features.recipeviewer.core.gui.filter.IFilterTextSource;

public class IngredientFilterApi implements IIngredientFilter {
	private final IngredientFilter ingredientFilter;
	private final IFilterTextSource filterTextSource;

	public IngredientFilterApi(IngredientFilter ingredientFilter, IFilterTextSource filterTextSource) {
		this.ingredientFilter = ingredientFilter;
		this.filterTextSource = filterTextSource;
	}

	@Override
	public String getFilterText() {
		return filterTextSource.getFilterText();
	}

	@Override
	public void setFilterText(String filterText) {
		ErrorUtil.checkNotNull(filterText, "filterText");
		filterTextSource.setFilterText(filterText);
	}

	@Override
	public <T> List<T> getFilteredIngredients(IIngredientType<T> ingredientType) {
		return ingredientFilter.getFilteredIngredients(ingredientType);
	}
}
