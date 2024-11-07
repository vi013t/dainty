package violet.dainty.features.recipeviewer.core.gui.startup;

import java.nio.file.Path;

import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.gui.config.BookmarkJsonConfig;
import violet.dainty.features.recipeviewer.core.gui.config.IBookmarkConfig;
import violet.dainty.features.recipeviewer.core.gui.config.IngredientTypeSortingConfig;
import violet.dainty.features.recipeviewer.core.gui.config.ModNameSortingConfig;

public record GuiConfigData(
	IBookmarkConfig bookmarkConfig,
	ModNameSortingConfig modNameSortingConfig,
	IngredientTypeSortingConfig ingredientTypeSortingConfig
) {
	public static GuiConfigData create() {
		Path configDir = Services.PLATFORM.getConfigHelper().createJeiConfigDir();

		IBookmarkConfig bookmarkConfig = new BookmarkJsonConfig(configDir);
		ModNameSortingConfig ingredientModNameSortingConfig = new ModNameSortingConfig(configDir.resolve("ingredient-list-mod-sort-order.ini"));
		IngredientTypeSortingConfig ingredientTypeSortingConfig = new IngredientTypeSortingConfig(configDir.resolve("ingredient-list-type-sort-order.ini"));

		return new GuiConfigData(
			bookmarkConfig,
			ingredientModNameSortingConfig,
			ingredientTypeSortingConfig
		);
	}
}
