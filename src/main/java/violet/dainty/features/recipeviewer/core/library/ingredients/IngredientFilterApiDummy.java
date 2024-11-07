package violet.dainty.features.recipeviewer.core.library.ingredients;

import java.util.List;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientFilter;

public class IngredientFilterApiDummy implements IIngredientFilter {
	public static final IIngredientFilter INSTANCE = new IngredientFilterApiDummy();

	private IngredientFilterApiDummy() {

	}

	@Override
	public String getFilterText() {
		return "";
	}

	@Override
	public void setFilterText(String filterText) {

	}

	@Override
	public <T> List<T> getFilteredIngredients(IIngredientType<T> ingredientType) {
		return List.of();
	}
}
