package violet.dainty.features.recipeviewer.core.library.ingredients.subtypes;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;

@SuppressWarnings("removal")
public class LegacyInterpreterAdapter<T> implements ISubtypeInterpreter<T> {
	private final violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.IIngredientSubtypeInterpreter<T> legacyInterpreter;

	public LegacyInterpreterAdapter(violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.IIngredientSubtypeInterpreter<T> legacyInterpreter) {
		this.legacyInterpreter = legacyInterpreter;
	}

	@Override
	public Object getSubtypeData(T ingredient, UidContext context) {
		String result = legacyInterpreter.apply(ingredient, context);
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	@Override
	public String getLegacyStringSubtypeInfo(T ingredient, UidContext context) {
		return legacyInterpreter.apply(ingredient, context);
	}
}
