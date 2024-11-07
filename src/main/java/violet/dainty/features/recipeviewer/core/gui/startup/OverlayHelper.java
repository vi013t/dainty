package violet.dainty.features.recipeviewer.core.gui.startup;

import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.config.IIngredientFilterConfig;
import violet.dainty.features.recipeviewer.core.common.config.IIngredientGridConfig;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableNineSliceTexture;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IColorHelper;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.BookmarkList;
import violet.dainty.features.recipeviewer.core.gui.filter.IFilterTextSource;
import violet.dainty.features.recipeviewer.core.gui.overlay.IIngredientGridSource;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientGrid;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientGridWithNavigation;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientListOverlay;
import violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks.BookmarkOverlay;

public final class OverlayHelper {
	private OverlayHelper() {}

	public static IngredientGridWithNavigation createIngredientGridWithNavigation(
		String debugName,
		IIngredientGridSource ingredientFilter,
		IIngredientManager ingredientManager,
		IIngredientGridConfig ingredientGridConfig,
		DrawableNineSliceTexture background,
		DrawableNineSliceTexture slotBackground,
		IInternalKeyMappings keyMappings,
		IIngredientFilterConfig ingredientFilterConfig,
		IClientConfig clientConfig,
		IClientToggleState toggleState,
		IConnectionToServer serverConnection,
		IColorHelper colorHelper,
		IScreenHelper screenHelper,
		boolean supportsEditMode
	) {
		IngredientGrid ingredientListGrid = new IngredientGrid(
			ingredientManager,
			ingredientGridConfig,
			ingredientFilterConfig,
			clientConfig,
			toggleState,
			serverConnection,
			keyMappings,
			colorHelper,
			supportsEditMode
		);

		return new IngredientGridWithNavigation(
			debugName,
			ingredientFilter,
			ingredientListGrid,
			toggleState,
			clientConfig,
			serverConnection,
			ingredientGridConfig,
			background,
			slotBackground,
			screenHelper,
			ingredientManager
		);
	}

	public static IngredientListOverlay createIngredientListOverlay(
		IIngredientManager ingredientManager,
		IScreenHelper screenHelper,
		IIngredientGridSource ingredientFilter,
		IFilterTextSource filterTextSource,
		IInternalKeyMappings keyMappings,
		IIngredientGridConfig ingredientGridConfig,
		IClientConfig clientConfig,
		IClientToggleState toggleState,
		IConnectionToServer serverConnection,
		IIngredientFilterConfig ingredientFilterConfig,
		Textures textures,
		IColorHelper colorHelper
	) {
		IngredientGridWithNavigation ingredientListGridNavigation = createIngredientGridWithNavigation(
			"IngredientListOverlay",
			ingredientFilter,
			ingredientManager,
			ingredientGridConfig,
			textures.getIngredientListBackground(),
			textures.getIngredientListSlotBackground(),
			keyMappings,
			ingredientFilterConfig,
			clientConfig,
			toggleState,
			serverConnection,
			colorHelper,
			screenHelper,
			true
		);

		return new IngredientListOverlay(
			ingredientFilter,
			filterTextSource,
			screenHelper,
			ingredientListGridNavigation,
			clientConfig,
			toggleState,
			keyMappings
		);
	}

	public static BookmarkOverlay createBookmarkOverlay(
		IIngredientManager ingredientManager,
		IScreenHelper screenHelper,
		BookmarkList bookmarkList,
		IInternalKeyMappings keyMappings,
		IIngredientGridConfig bookmarkListConfig,
		IIngredientFilterConfig ingredientFilterConfig,
		IClientConfig clientConfig,
		IClientToggleState toggleState,
		IConnectionToServer serverConnection,
		Textures textures,
		IColorHelper colorHelper
	) {
		IngredientGridWithNavigation bookmarkListGridNavigation = createIngredientGridWithNavigation(
			"BookmarkOverlay",
			bookmarkList,
			ingredientManager,
			bookmarkListConfig,
			textures.getBookmarkListBackground(),
			textures.getBookmarkListSlotBackground(),
			keyMappings,
			ingredientFilterConfig,
			clientConfig,
			toggleState,
			serverConnection,
			colorHelper,
			screenHelper,
			false
		);

		return new BookmarkOverlay(
			bookmarkList,
			bookmarkListGridNavigation,
			toggleState,
			screenHelper,
			keyMappings
		);
	}
}
