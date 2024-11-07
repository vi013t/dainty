package violet.dainty.features.recipeviewer.core.library.config;

import java.nio.file.Path;
import java.util.Comparator;

import violet.dainty.features.recipeviewer.core.common.config.sorting.MappedSortingConfig;
import violet.dainty.features.recipeviewer.core.common.config.sorting.serializers.SortingSerializers;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;

public class RecipeCategorySortingConfig extends MappedSortingConfig<RecipeType<?>, String> {
	public RecipeCategorySortingConfig(Path path) {
		super(path, SortingSerializers.STRING, r -> r.getUid().toString());
	}

	@Override
	protected Comparator<String> getDefaultSortOrder() {
		Comparator<String> minecraftCraftingFirst = Comparator.comparing((String s) -> {
			String vanillaCrafting = RecipeTypes.CRAFTING.getUid().toString();
			return s.equals(vanillaCrafting);
		}).reversed();
		Comparator<String> minecraftFirst = Comparator.comparing((String s) -> s.startsWith(ModIds.MINECRAFT_ID)).reversed();
		Comparator<String> naturalOrder = Comparator.naturalOrder();
		return minecraftCraftingFirst.thenComparing(minecraftFirst).thenComparing(naturalOrder);
	}

}
