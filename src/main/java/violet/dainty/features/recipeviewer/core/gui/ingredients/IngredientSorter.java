package violet.dainty.features.recipeviewer.core.gui.ingredients;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.config.IngredientSortStage;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.gui.config.IngredientTypeSortingConfig;
import violet.dainty.features.recipeviewer.core.gui.config.ModNameSortingConfig;

public final class IngredientSorter {
	private static final Comparator<IListElement<?>> COMPARE_SORT_INDEX =
		Comparator.comparing(IListElement::getSortedIndex);

	public static Comparator<IListElement<?>> sortIngredients(
		IClientConfig clientConfig,
		ModNameSortingConfig modNameSortingConfig,
		IngredientTypeSortingConfig ingredientTypeSortingConfig,
		IIngredientManager ingredientManager,
		List<IListElementInfo<?>> ingredients
	) {
		Set<String> modNames = ingredients.stream()
			.map(IListElementInfo::getModNameForSorting)
			.collect(Collectors.toSet());

		IngredientSorterComparators comparators = new IngredientSorterComparators(ingredientManager, modNameSortingConfig, ingredientTypeSortingConfig, modNames);

		List<IngredientSortStage> ingredientSorterStages = clientConfig.getIngredientSorterStages();

		Comparator<IListElementInfo<?>> completeComparator = comparators.getComparator(ingredientSorterStages);

		// Get all of the items sorted with our custom comparator.
		ingredients.sort(completeComparator);

		// Go through all of the items and set their sorted index.
		final int size = ingredients.size();
		for (int i = 0; i < size; i++) {
			IListElementInfo<?> elementInfo = ingredients.get(i);
			IListElement<?> element = elementInfo.getElement();
			element.setSortedIndex(i);
		}

		//Now the comparator just uses that index value to order everything.
		return COMPARE_SORT_INDEX;
	}

}
