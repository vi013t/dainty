package violet.dainty.features.recipeviewer.core.common.config;

import violet.dainty.features.recipeviewer.core.core.search.SearchMode;

public interface IIngredientFilterConfig {
	SearchMode getModNameSearchMode();

	SearchMode getTooltipSearchMode();

	SearchMode getTagSearchMode();

	SearchMode getColorSearchMode();

	SearchMode getResourceLocationSearchMode();

	SearchMode getCreativeTabSearchMode();

	boolean getSearchAdvancedTooltips();

	boolean getSearchModIds();

	boolean getSearchModAliases();

	boolean getSearchIngredientAliases();

	boolean getSearchShortModNames();
}
