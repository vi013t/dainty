package violet.dainty.features.recipeviewer.core.gui.startup;

import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.config.IIngredientFilterConfig;
import violet.dainty.features.recipeviewer.core.common.config.IIngredientGridConfig;
import violet.dainty.features.recipeviewer.core.common.config.IJeiClientConfigs;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.network.IConnectionToServer;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.ICodecHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IColorHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IModIdHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusFactory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRuntimeRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IEditModeConfig;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientFilter;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientVisibility;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.core.util.LoggedTimer;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.BookmarkList;
import violet.dainty.features.recipeviewer.core.gui.config.IBookmarkConfig;
import violet.dainty.features.recipeviewer.core.gui.config.IngredientTypeSortingConfig;
import violet.dainty.features.recipeviewer.core.gui.config.ModNameSortingConfig;
import violet.dainty.features.recipeviewer.core.gui.events.GuiEventHandler;
import violet.dainty.features.recipeviewer.core.gui.filter.FilterTextSource;
import violet.dainty.features.recipeviewer.core.gui.filter.IFilterTextSource;
import violet.dainty.features.recipeviewer.core.gui.ingredients.IListElement;
import violet.dainty.features.recipeviewer.core.gui.ingredients.IListElementInfo;
import violet.dainty.features.recipeviewer.core.gui.ingredients.IngredientFilter;
import violet.dainty.features.recipeviewer.core.gui.ingredients.IngredientFilterApi;
import violet.dainty.features.recipeviewer.core.gui.ingredients.IngredientListElementFactory;
import violet.dainty.features.recipeviewer.core.gui.ingredients.IngredientSorter;
import violet.dainty.features.recipeviewer.core.gui.input.ClientInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.CombinedRecipeFocusSource;
import violet.dainty.features.recipeviewer.core.gui.input.GuiContainerWrapper;
import violet.dainty.features.recipeviewer.core.gui.input.ICharTypedHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.BookmarkInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.DragRouter;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.EditInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.FocusInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.GlobalInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.GuiAreaInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.UserInputRouter;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientListOverlay;
import violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks.BookmarkOverlay;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipesGui;
import violet.dainty.features.recipeviewer.core.gui.util.FocusUtil;

public class JeiGuiStarter {
	private static final Logger LOGGER = LogManager.getLogger();

	public static JeiEventHandlers start(IRuntimeRegistration registration) {
		LOGGER.info("Starting JEI GUI");
		LoggedTimer timer = new LoggedTimer();

		IConnectionToServer serverConnection = Internal.getServerConnection();
		Textures textures = Internal.getTextures();
		IInternalKeyMappings keyMappings = Internal.getKeyMappings();

		IScreenHelper screenHelper = registration.getScreenHelper();
		IRecipeTransferManager recipeTransferManager = registration.getRecipeTransferManager();
		IRecipeManager recipeManager = registration.getRecipeManager();
		IIngredientManager ingredientManager = registration.getIngredientManager();
		IEditModeConfig editModeConfig = registration.getEditModeConfig();

		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IIngredientVisibility ingredientVisibility = jeiHelpers.getIngredientVisibility();
		IColorHelper colorHelper = jeiHelpers.getColorHelper();
		IModIdHelper modIdHelper = jeiHelpers.getModIdHelper();
		IFocusFactory focusFactory = jeiHelpers.getFocusFactory();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		ICodecHelper codecHelper = jeiHelpers.getCodecHelper();

		IFilterTextSource filterTextSource = new FilterTextSource();
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		ErrorUtil.checkNotNull(level, "minecraft.level");

		RegistryAccess registryAccess = level.registryAccess();

		timer.start("Building ingredient list");
		List<IListElementInfo<?>> ingredientList = IngredientListElementFactory.createBaseList(ingredientManager, modIdHelper);
		timer.stop();

		timer.start("Building ingredient filter");
		GuiConfigData configData = GuiConfigData.create();

		ModNameSortingConfig modNameSortingConfig = configData.modNameSortingConfig();
		IngredientTypeSortingConfig ingredientTypeSortingConfig = configData.ingredientTypeSortingConfig();
		IClientToggleState toggleState = Internal.getClientToggleState();
		IBookmarkConfig bookmarkConfig = configData.bookmarkConfig();

		IJeiClientConfigs jeiClientConfigs = Internal.getJeiClientConfigs();
		IClientConfig clientConfig = jeiClientConfigs.getClientConfig();
		IIngredientGridConfig ingredientListConfig = jeiClientConfigs.getIngredientListConfig();
		IIngredientGridConfig bookmarkListConfig = jeiClientConfigs.getBookmarkListConfig();
		IIngredientFilterConfig ingredientFilterConfig = jeiClientConfigs.getIngredientFilterConfig();

		Comparator<IListElement<?>> ingredientComparator = IngredientSorter.sortIngredients(
			clientConfig,
			modNameSortingConfig,
			ingredientTypeSortingConfig,
			ingredientManager,
			ingredientList
		);

		IngredientFilter ingredientFilter = new IngredientFilter(
			filterTextSource,
			clientConfig,
			ingredientFilterConfig,
			ingredientManager,
			ingredientComparator,
			ingredientList,
			modIdHelper,
			ingredientVisibility,
			colorHelper,
			toggleState
		);
		ingredientManager.registerIngredientListener(ingredientFilter);
		ingredientVisibility.registerListener(ingredientFilter);
		timer.stop();

		IIngredientFilter ingredientFilterApi = new IngredientFilterApi(ingredientFilter, filterTextSource);
		registration.setIngredientFilter(ingredientFilterApi);

		IngredientListOverlay ingredientListOverlay = OverlayHelper.createIngredientListOverlay(
			ingredientManager,
			screenHelper,
			ingredientFilter,
			filterTextSource,
			keyMappings,
			ingredientListConfig,
			clientConfig,
			toggleState,
			serverConnection,
			ingredientFilterConfig,
			textures,
			colorHelper
		);
		registration.setIngredientListOverlay(ingredientListOverlay);

		BookmarkList bookmarkList = new BookmarkList(recipeManager, focusFactory, ingredientManager, registryAccess, bookmarkConfig, clientConfig, guiHelper, codecHelper);
		bookmarkConfig.loadBookmarks(recipeManager, focusFactory, guiHelper, ingredientManager, registryAccess, bookmarkList, codecHelper);

		BookmarkOverlay bookmarkOverlay = OverlayHelper.createBookmarkOverlay(
			ingredientManager,
			screenHelper,
			bookmarkList,
			keyMappings,
			bookmarkListConfig,
			ingredientFilterConfig,
			clientConfig,
			toggleState,
			serverConnection,
			textures,
			colorHelper
		);
		registration.setBookmarkOverlay(bookmarkOverlay);

		GuiEventHandler guiEventHandler = new GuiEventHandler(
			screenHelper,
			bookmarkOverlay,
			ingredientListOverlay
		);

		RecipesGui recipesGui = new RecipesGui(
			recipeManager,
			recipeTransferManager,
			keyMappings,
			focusFactory,
			bookmarkList,
			guiHelper
		);
		registration.setRecipesGui(recipesGui);

		CombinedRecipeFocusSource recipeFocusSource = new CombinedRecipeFocusSource(
			recipesGui,
			ingredientListOverlay,
			bookmarkOverlay,
			new GuiContainerWrapper(screenHelper)
		);

		List<ICharTypedHandler> charTypedHandlers = List.of(
			ingredientListOverlay
		);

		FocusUtil focusUtil = new FocusUtil(focusFactory, clientConfig, ingredientManager);

		UserInputRouter userInputRouter = new UserInputRouter(
			"JEIGlobal",
			new EditInputHandler(recipeFocusSource, toggleState, editModeConfig),
			ingredientListOverlay.createInputHandler(),
			bookmarkOverlay.createInputHandler(),
			new FocusInputHandler(recipeFocusSource, recipesGui, focusUtil, clientConfig, ingredientManager, toggleState, serverConnection),
			new BookmarkInputHandler(recipeFocusSource, bookmarkList),
			new GlobalInputHandler(toggleState),
			new GuiAreaInputHandler(screenHelper, recipesGui, focusFactory)
		);

		DragRouter dragRouter = new DragRouter(
			ingredientListOverlay.createDragHandler(),
			bookmarkOverlay.createDragHandler()
		);
		ClientInputHandler clientInputHandler = new ClientInputHandler(
			charTypedHandlers,
			userInputRouter,
			dragRouter,
			keyMappings
		);
		ResourceReloadHandler resourceReloadHandler = new ResourceReloadHandler(
			ingredientListOverlay,
			ingredientFilter
		);

		return new JeiEventHandlers(
			guiEventHandler,
			clientInputHandler,
			resourceReloadHandler
		);
	}
}
