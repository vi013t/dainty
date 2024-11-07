package violet.dainty.features.recipeviewer.core.gui.config;

import java.nio.file.Path;
import java.util.Comparator;

import violet.dainty.features.recipeviewer.core.common.config.sorting.MappedSortingConfig;
import violet.dainty.features.recipeviewer.core.common.config.sorting.serializers.SortingSerializers;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.gui.ingredients.IListElementInfo;

public class IngredientTypeSortingConfig extends MappedSortingConfig<IListElementInfo<?>, String> {
	public IngredientTypeSortingConfig(Path path) {
		super(path, SortingSerializers.STRING, IngredientTypeSortingConfig::getIngredientTypeString);
	}

	public static String getIngredientTypeString(IListElementInfo<?> info) {
		ITypedIngredient<?> typedIngredient = info.getTypedIngredient();
		return getIngredientTypeString(typedIngredient.getType());
	}

	public static String getIngredientTypeString(IIngredientType<?> ingredientType) {
		return ingredientType.getIngredientClass().getName();
	}

	@Override
	protected Comparator<String> getDefaultSortOrder() {
		String itemStackIngredientType = getIngredientTypeString(VanillaTypes.ITEM_STACK);
		Comparator<String> itemStackFirst = Comparator.comparing((String s) -> s.equals(itemStackIngredientType)).reversed();
		Comparator<String> naturalOrder = Comparator.naturalOrder();
		return itemStackFirst.thenComparing(naturalOrder);
	}

}
