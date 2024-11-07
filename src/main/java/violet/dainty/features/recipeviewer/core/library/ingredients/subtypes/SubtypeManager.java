package violet.dainty.features.recipeviewer.core.library.ingredients.subtypes;

import org.jetbrains.annotations.Nullable;

import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientTypeWithSubtypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeManager;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;

public class SubtypeManager implements ISubtypeManager {
	private final SubtypeInterpreters interpreters;

	public SubtypeManager(SubtypeInterpreters interpreters) {
		this.interpreters = interpreters;
	}

	@Override
	@Nullable
	public <T> Object getSubtypeData(IIngredientTypeWithSubtypes<?, T> ingredientType, T ingredient, UidContext context) {
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(ingredient, "ingredient");
		ErrorUtil.checkNotNull(context, "type");

		ISubtypeInterpreter<T> interpreter = interpreters.get(ingredientType, ingredient);
		if (interpreter == null) {
			return null;
		}
		return interpreter.getSubtypeData(ingredient, context);
	}

	@Override
	public @Nullable <B, T> Object getSubtypeData(IIngredientTypeWithSubtypes<B, T> ingredientType, ITypedIngredient<T> typedIngredient, UidContext context) {
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(typedIngredient, "typedIngredient");
		ErrorUtil.checkNotNull(context, "type");

		B ingredientBase = typedIngredient.getBaseIngredient(ingredientType);
		ISubtypeInterpreter<T> interpreter = interpreters.getFromBase(ingredientType, ingredientBase);
		if (interpreter == null) {
			return null;
		}
		T ingredient = typedIngredient.getIngredient();
		return interpreter.getSubtypeData(ingredient, context);
	}

	@SuppressWarnings({"removal", "deprecation"})
	@Override
	public <T> String getSubtypeInfo(IIngredientTypeWithSubtypes<?, T> ingredientType, T ingredient, UidContext context) {
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(ingredient, "ingredient");
		ErrorUtil.checkNotNull(context, "context");

		ISubtypeInterpreter<T> interpreter = interpreters.get(ingredientType, ingredient);
		if (interpreter == null) {
			return violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.IIngredientSubtypeInterpreter.NONE;
		}
		return interpreter.getLegacyStringSubtypeInfo(ingredient, context);
	}

	@Override
	public <T, B> boolean hasSubtypes(IIngredientTypeWithSubtypes<B, T> ingredientType, T ingredient) {
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(ingredient, "ingredient");

		return interpreters.contains(ingredientType, ingredient);
	}
}
