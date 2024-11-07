package violet.dainty.features.recipeviewer.core.library.focus;

import java.util.Collection;

import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusFactory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

public class FocusFactory implements IFocusFactory {
	private final IIngredientManager ingredientManager;

	public FocusFactory(IIngredientManager ingredientManager) {
		this.ingredientManager = ingredientManager;
	}

	@Override
	public <V> IFocus<V> createFocus(RecipeIngredientRole role, IIngredientType<V> ingredientType, V ingredient) {
		ErrorUtil.checkNotNull(role, "role");
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(ingredient, "ingredient");
		return Focus.createFromApi(ingredientManager, role, ingredientType, ingredient);
	}

	@Override
	public <V> IFocus<V> createFocus(RecipeIngredientRole role, ITypedIngredient<V> typedIngredient) {
		ErrorUtil.checkNotNull(role, "role");
		ErrorUtil.checkNotNull(typedIngredient, "typedIngredient");
		return Focus.createFromApi(ingredientManager, role, typedIngredient);
	}

	@Override
	public IFocusGroup createFocusGroup(Collection<? extends IFocus<?>> focuses) {
		return FocusGroup.create(focuses, ingredientManager);
	}

	@Override
	public IFocusGroup getEmptyFocusGroup() {
		return FocusGroup.EMPTY;
	}
}
