package violet.dainty.features.recipeviewer.core.library.focus;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

public class FocusGroup implements IFocusGroup {
	public static final IFocusGroup EMPTY = new FocusGroup(List.of());

	/**
	 * Make sure any IFocus coming in through API calls is validated
	 */
	public static IFocusGroup create(Collection<? extends IFocus<?>> focuses, IIngredientManager ingredientManager) {
		List<Focus<?>> checkedFocuses = focuses.stream()
			.filter(Objects::nonNull)
			.<Focus<?>>map(f -> Focus.checkOne(f, ingredientManager))
			.toList();
		if (checkedFocuses.isEmpty()) {
			return EMPTY;
		}
		return new FocusGroup(checkedFocuses);
	}

	private final List<IFocus<?>> focuses;

	private FocusGroup(List<Focus<?>> focuses) {
		this.focuses = List.copyOf(focuses);
	}

	@Override
	public boolean isEmpty() {
		return focuses.isEmpty();
	}

	@Override
	public List<IFocus<?>> getAllFocuses() {
		return focuses;
	}

	@Override
	public Stream<IFocus<?>> getFocuses(RecipeIngredientRole role) {
		return focuses.stream()
			.filter(focus -> focus.getRole() == role);
	}

	@Override
	public <T> Stream<IFocus<T>> getFocuses(IIngredientType<T> ingredientType) {
		return focuses.stream()
			.map(focus -> focus.checkedCast(ingredientType))
			.flatMap(Optional::stream);
	}

	@Override
	public <T> Stream<IFocus<T>> getFocuses(IIngredientType<T> ingredientType, RecipeIngredientRole role) {
		return getFocuses(role)
			.map(focus -> focus.checkedCast(ingredientType))
			.flatMap(Optional::stream);
	}
}
