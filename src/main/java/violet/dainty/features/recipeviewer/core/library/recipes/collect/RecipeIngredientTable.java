package violet.dainty.features.recipeviewer.core.library.recipes.collect;

import org.jetbrains.annotations.UnmodifiableView;

import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeIngredientTable {
	private final Map<RecipeType<?>, IngredientToRecipesMap<?>> map = new HashMap<>();

	public <V> void add(V recipe, RecipeType<V> recipeType, Collection<Object> ingredientUids) {
		@SuppressWarnings("unchecked")
		IngredientToRecipesMap<V> ingredientToRecipesMap = (IngredientToRecipesMap<V>) this.map.computeIfAbsent(recipeType, k -> new IngredientToRecipesMap<>());
		ingredientToRecipesMap.add(recipe, ingredientUids);
	}

	@UnmodifiableView
	public <V> List<V> get(RecipeType<V> recipeType, Object ingredientUid) {
		@SuppressWarnings("unchecked")
		IngredientToRecipesMap<V> ingredientToRecipesMap = (IngredientToRecipesMap<V>) this.map.get(recipeType);
		if (ingredientToRecipesMap == null) {
			return List.of();
		}
		return ingredientToRecipesMap.get(ingredientUid);
	}

	public void compact() {
		map.values().forEach(IngredientToRecipesMap::compact);
	}
}