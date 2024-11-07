package violet.dainty.features.recipeviewer.core.gui.config;

import java.nio.file.Path;
import java.util.Comparator;

import violet.dainty.features.recipeviewer.core.common.config.sorting.MappedSortingConfig;
import violet.dainty.features.recipeviewer.core.common.config.sorting.serializers.SortingSerializers;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.gui.ingredients.IListElementInfo;

public class ModNameSortingConfig extends MappedSortingConfig<IListElementInfo<?>, String> {
	public ModNameSortingConfig(Path path) {
		super(path, SortingSerializers.STRING, IListElementInfo::getModNameForSorting);
	}

	@Override
	protected Comparator<String> getDefaultSortOrder() {
		Comparator<String> minecraftFirst = Comparator.comparing((String s) -> s.equals(ModIds.MINECRAFT_NAME)).reversed();
		Comparator<String> naturalOrder = Comparator.naturalOrder();
		return minecraftFirst.thenComparing(naturalOrder);
	}

}
