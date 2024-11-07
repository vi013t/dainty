package violet.dainty.features.recipeviewer.core.gui.ingredients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import violet.dainty.features.recipeviewer.core.commonapi.helpers.IModIdHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

public final class IngredientListElementFactory {
	private static final Logger LOGGER = LogManager.getLogger();

	private IngredientListElementFactory() {
	}

	public static List<IListElementInfo<?>> createBaseList(IIngredientManager ingredientManager, IModIdHelper modIdHelper) {
		List<IListElementInfo<?>> ingredientListElements = new ArrayList<>();

		for (IIngredientType<?> ingredientType : ingredientManager.getRegisteredIngredientTypes()) {
			addToBaseList(ingredientListElements, ingredientManager, ingredientType, modIdHelper);
		}

		return ingredientListElements;
	}

	public static <V> List<IListElementInfo<V>> createTestList(IIngredientManager ingredientManager, IIngredientType<V> ingredientType, Collection<V> ingredients, IModIdHelper modIdHelper) {
		return ingredients.stream()
			.map(i -> ingredientManager.createTypedIngredient(ingredientType, i))
			.flatMap(Optional::stream)
			.map(i -> ListElementInfo.create(i, ingredientManager, modIdHelper))
			.filter(Objects::nonNull)
			.toList();
	}

	public static List<IListElementInfo<?>> rebuildList(IIngredientManager ingredientManager, Collection<IListElement<?>> elements, IModIdHelper modIdHelper) {
		List<IListElementInfo<?>> results = new ArrayList<>();

		for (IListElement<?> element : elements) {
			IListElementInfo<?> orderedElement = ListElementInfo.createFromElement(element, ingredientManager, modIdHelper);
			if (orderedElement != null) {
				results.add(orderedElement);
			}
		}

		return results;
	}

	private static <V> void addToBaseList(List<IListElementInfo<?>> baseList, IIngredientManager ingredientManager, IIngredientType<V> ingredientType, IModIdHelper modIdHelper) {
		Collection<V> ingredients = ingredientManager.getAllIngredients(ingredientType);
		LOGGER.debug("Registering ingredients: {}", ingredientType.getIngredientClass().getSimpleName());
		for (V ingredient : ingredients) {
			Optional<ITypedIngredient<V>> typedIngredient = ingredientManager.createTypedIngredient(ingredientType, ingredient);
			if (typedIngredient.isPresent()) {
				IListElementInfo<V> orderedElement = ListElementInfo.create(typedIngredient.get(), ingredientManager, modIdHelper);
				if (orderedElement != null) {
					baseList.add(orderedElement);
				}
			}
		}
	}

}
