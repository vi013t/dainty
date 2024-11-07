package violet.dainty.features.recipeviewer.core.library.ingredients;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

public class IngredientBlacklistInternal implements IIngredientManager.IIngredientListener {
	private final Set<Object> uidBlacklist = new HashSet<>();
	private WeakReference<IngredientVisibility> ingredientVisibilityRef = new WeakReference<>(null);

	public <V> void addIngredientToBlacklist(ITypedIngredient<V> typedIngredient, IIngredientHelper<V> ingredientHelper) {
		Object uid = ingredientHelper.getUid(typedIngredient, UidContext.Ingredient);
		if (uidBlacklist.add(uid)) {
			notifyListenersOfVisibilityChange(typedIngredient, false);
		}
	}

	public <V> void removeIngredientFromBlacklist(ITypedIngredient<V> typedIngredient, IIngredientHelper<V> ingredientHelper) {
		Object uid = ingredientHelper.getUid(typedIngredient, UidContext.Ingredient);
		if (uidBlacklist.remove(uid)) {
			notifyListenersOfVisibilityChange(typedIngredient, true);
		}
	}

	public <V> boolean isIngredientBlacklistedByApi(ITypedIngredient<V> typedIngredient, IIngredientHelper<V> ingredientHelper) {
		Object uid = ingredientHelper.getUid(typedIngredient, UidContext.Ingredient);
		Object uidWild = ingredientHelper.getGroupingUid(typedIngredient);

		if (uid.equals(uidWild)) {
			return uidBlacklist.contains(uid);
		}
		return uidBlacklist.contains(uid) || uidBlacklist.contains(uidWild);
	}

	public void registerListener(IngredientVisibility ingredientVisibility) {
		this.ingredientVisibilityRef = new WeakReference<>(ingredientVisibility);
	}

	@Override
	public <V> void onIngredientsAdded(IIngredientHelper<V> ingredientHelper, Collection<ITypedIngredient<V>> ingredients) {
		for (ITypedIngredient<V> ingredient : ingredients) {
			removeIngredientFromBlacklist(ingredient, ingredientHelper);
		}
	}

	@Override
	public <V> void onIngredientsRemoved(IIngredientHelper<V> ingredientHelper, Collection<ITypedIngredient<V>> ingredients) {
		for (ITypedIngredient<V> ingredient : ingredients) {
			addIngredientToBlacklist(ingredient, ingredientHelper);
		}
	}

	private <T> void notifyListenersOfVisibilityChange(ITypedIngredient<T> ingredient, boolean visible) {
		IngredientVisibility ingredientVisibility = ingredientVisibilityRef.get();
		if (ingredientVisibility != null) {
			ingredientVisibility.notifyListeners(ingredient, visible);
		}
	}
}