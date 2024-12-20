package violet.dainty.features.recipeviewer.core.library.focus;

import org.jetbrains.annotations.Nullable;

import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.ingredients.TypedIngredient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class Focus<V> implements IFocus<V>, IFocusGroup {
	private final RecipeIngredientRole role;
	private final ITypedIngredient<V> value;

	public Focus(RecipeIngredientRole role, ITypedIngredient<V> value) {
		ErrorUtil.checkNotNull(role, "focus role");
		ErrorUtil.checkNotNull(value, "focus value");
		this.role = role;
		this.value = value;
	}

	@Override
	public ITypedIngredient<V> getTypedValue() {
		return value;
	}

	@Override
	public RecipeIngredientRole getRole() {
		return role;
	}

	@Override
	public <T> Optional<IFocus<T>> checkedCast(IIngredientType<T> ingredientType) {
		if (value.getType() == ingredientType) {
			@SuppressWarnings("unchecked")
			Focus<T> cast = (Focus<T>) this;
			return Optional.of(cast);
		}
		return Optional.empty();
	}

	/**
	 * Make sure any IFocus coming in through API calls is validated and turned into JEI's Focus.
	 */
	public static <V> Focus<V> checkOne(IFocus<V> focus, IIngredientManager ingredientManager) {
		if (focus instanceof Focus) {
			return (Focus<V>) focus;
		}
		ErrorUtil.checkNotNull(focus, "focus");

		ITypedIngredient<V> value = focus.getTypedValue();
		ErrorUtil.checkNotNull(value, "focus typed value");

		IIngredientType<V> type = value.getType();
		ErrorUtil.checkNotNull(type, "focus type");

		V ingredient = value.getIngredient();
		ErrorUtil.checkNotNull(type, "focus ingredient");

		RecipeIngredientRole role = focus.getRole();
		ErrorUtil.checkNotNull(role, "focus typed value role");

		return createFromApi(ingredientManager, role, type, ingredient);
	}

	public static <V> Focus<V> createFromApi(IIngredientManager ingredientManager, RecipeIngredientRole role, IIngredientType<V> ingredientType, V value) {
		@Nullable ITypedIngredient<V> typedIngredient = TypedIngredient.createAndFilterInvalid(ingredientManager, ingredientType, value, false);

		if (typedIngredient == null) {
			throw new IllegalArgumentException("Focus value is invalid: " + ErrorUtil.getIngredientInfo(value, ingredientType, ingredientManager));
		}
		return new Focus<>(role, typedIngredient);
	}

	public static <V> Focus<V> createFromApi(IIngredientManager ingredientManager, RecipeIngredientRole role, ITypedIngredient<V> typedIngredient) {
		@Nullable ITypedIngredient<V> typedIngredientCopy = TypedIngredient.defensivelyCopyTypedIngredientFromApi(ingredientManager, typedIngredient);
		if (typedIngredientCopy == null) {
			throw new IllegalArgumentException("Focus value is invalid: " + ErrorUtil.getIngredientInfo(typedIngredient.getIngredient(), typedIngredient.getType(), ingredientManager));
		}
		return new Focus<>(role, typedIngredientCopy);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public List<IFocus<?>> getAllFocuses() {
		return List.of(this);
	}

	@Override
	public Stream<IFocus<?>> getFocuses(RecipeIngredientRole role) {
		if (role == this.role) {
			return Stream.of(this);
		}
		return Stream.empty();
	}

	@Override
	public <T> Stream<IFocus<T>> getFocuses(IIngredientType<T> ingredientType) {
		return checkedCast(ingredientType).stream();
	}

	@Override
	public <T> Stream<IFocus<T>> getFocuses(IIngredientType<T> ingredientType, RecipeIngredientRole role) {
		if (role == this.role) {
			return getFocuses(ingredientType);
		}
		return Stream.empty();
	}
}
