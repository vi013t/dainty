package violet.dainty.features.recipeviewer.core.library.load.registration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientTypeWithSubtypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.commonapi.registration.ISubtypeRegistration;
import violet.dainty.features.recipeviewer.core.library.ingredients.subtypes.SubtypeInterpreters;

public class SubtypeRegistration implements ISubtypeRegistration {
	private static final Logger LOGGER = LogManager.getLogger();

	private final SubtypeInterpreters interpreters = new SubtypeInterpreters();

	@Override
	public <B, I> void registerSubtypeInterpreter(IIngredientTypeWithSubtypes<B, I> type, B base, ISubtypeInterpreter<I> interpreter) {
		ErrorUtil.checkNotNull(type, "type");
		ErrorUtil.checkNotNull(base, "base");
		ErrorUtil.checkNotNull(interpreter, "interpreter");
		Class<? extends B> ingredientBaseClass = type.getIngredientBaseClass();
		if (!ingredientBaseClass.isInstance(base)) {
			throw new IllegalArgumentException(String.format("base (%s) must be an instance of %s", base.getClass(), ingredientBaseClass));
		}
		if (!this.interpreters.addInterpreter(type, base, interpreter)) {
			LOGGER.error("An interpreter is already registered for this: {}", base, new IllegalArgumentException());
		}
	}

	@SuppressWarnings("removal")
	@Override
	public <B, I> void registerSubtypeInterpreter(IIngredientTypeWithSubtypes<B, I> type, B base, violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.IIngredientSubtypeInterpreter<I> interpreter) {
		ErrorUtil.checkNotNull(type, "type");
		ErrorUtil.checkNotNull(base, "base");
		ErrorUtil.checkNotNull(interpreter, "interpreter");
		Class<? extends B> ingredientBaseClass = type.getIngredientBaseClass();
		if (!ingredientBaseClass.isInstance(base)) {
			throw new IllegalArgumentException(String.format("base (%s) must be an instance of %s", base.getClass(), ingredientBaseClass));
		}
		if (!this.interpreters.addInterpreter(type, base, interpreter)) {
			LOGGER.error("An interpreter is already registered for this: {}", base, new IllegalArgumentException());
		}
	}

	public SubtypeInterpreters getInterpreters() {
		return interpreters;
	}
}
