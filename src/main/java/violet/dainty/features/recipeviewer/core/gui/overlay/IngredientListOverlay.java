package violet.dainty.features.recipeviewer.core.gui.overlay;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.handlers.IGuiProperties;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientListOverlay;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.gui.GuiProperties;
import violet.dainty.features.recipeviewer.core.gui.elements.GuiIconToggleButton;
import violet.dainty.features.recipeviewer.core.gui.filter.IFilterTextSource;
import violet.dainty.features.recipeviewer.core.gui.input.GuiTextFieldFilter;
import violet.dainty.features.recipeviewer.core.gui.input.ICharTypedHandler;
import violet.dainty.features.recipeviewer.core.gui.input.IClickableIngredientInternal;
import violet.dainty.features.recipeviewer.core.gui.input.IDragHandler;
import violet.dainty.features.recipeviewer.core.gui.input.IDraggableIngredientInternal;
import violet.dainty.features.recipeviewer.core.gui.input.IRecipeFocusSource;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.MouseUtil;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.CombinedInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.NullDragHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.NullInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.ProxyDragHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.ProxyInputHandler;

public class IngredientListOverlay implements IIngredientListOverlay, IRecipeFocusSource, ICharTypedHandler {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final int BORDER_MARGIN = 6;
	private static final int INNER_PADDING = 2;
	private static final int BUTTON_SIZE = 20;
	private static final int SEARCH_HEIGHT = BUTTON_SIZE;

	private final GuiIconToggleButton configButton;
	private final IngredientGridWithNavigation contents;
	private final IClientConfig clientConfig;
	private final IClientToggleState toggleState;
	private final GuiTextFieldFilter searchField;
	private final IInternalKeyMappings keyBindings;
	private final ScreenPropertiesCache screenPropertiesCache;
	private final IFilterTextSource filterTextSource;

	public IngredientListOverlay(
		IIngredientGridSource ingredientGridSource,
		IFilterTextSource filterTextSource,
		IScreenHelper screenHelper,
		IngredientGridWithNavigation contents,
		IClientConfig clientConfig,
		IClientToggleState toggleState,
		IInternalKeyMappings keyBindings
	) {
		this.screenPropertiesCache = new ScreenPropertiesCache(screenHelper);
		this.contents = contents;
		this.clientConfig = clientConfig;
		this.toggleState = toggleState;

		this.searchField = new GuiTextFieldFilter(contents::isEmpty);
		this.keyBindings = keyBindings;
		this.filterTextSource = filterTextSource;
		this.searchField.setValue(filterTextSource.getFilterText());
		this.searchField.setFocused(false);
		this.searchField.setResponder(filterTextSource::setFilterText);
		filterTextSource.addListener(this.searchField::setValue);

		ingredientGridSource.addSourceListChangedListener(() -> {
			Minecraft minecraft = Minecraft.getInstance();
			getScreenPropertiesUpdater()
				.updateScreen(minecraft.screen)
				.update();
		});

		this.configButton = ConfigButton.create(this::isListDisplayed, toggleState, keyBindings);
	}

	@Override
	public boolean isListDisplayed() {
		// if there is no key binding to toggle it, force the overlay to display if possible
		return (toggleState.isOverlayEnabled() || keyBindings.getToggleOverlay().isUnbound()) &&
			screenPropertiesCache.hasValidScreen() &&
			contents.hasRoom();
	}

	private static ImmutableRect2i createDisplayArea(IGuiProperties guiProperties) {
		ImmutableRect2i screenRectangle = GuiProperties.getScreenRectangle(guiProperties);
		int guiRight = GuiProperties.getGuiRight(guiProperties);
		return screenRectangle.cropLeft(guiRight);
	}

	public ScreenPropertiesCache.Updater getScreenPropertiesUpdater() {
		return this.screenPropertiesCache.getUpdater(this::onScreenPropertiesChanged);
	}

	private void onScreenPropertiesChanged() {
		screenPropertiesCache.getGuiProperties()
			.ifPresentOrElse(guiProperties -> {
				try {
					ImmutableRect2i displayArea = createDisplayArea(guiProperties);
					Set<ImmutableRect2i> guiExclusionAreas = screenPropertiesCache.getGuiExclusionAreas();
					updateBounds(guiProperties, displayArea, guiExclusionAreas);
				} catch (RuntimeException e) {
					LOGGER.error("Failed to update JEI bounds for screen with properties : {}", guiProperties, e);
					this.contents.close();
					this.searchField.setFocused(false);
				}
			}, () -> {
				this.contents.close();
				this.searchField.setFocused(false);
			});
	}

	private void updateBounds(IGuiProperties guiProperties, ImmutableRect2i displayArea, Set<ImmutableRect2i> guiExclusionAreas) {
		final boolean searchBarCentered = isSearchBarCentered(this.clientConfig, guiProperties);

		final ImmutableRect2i availableContentsArea = getAvailableContentsArea(displayArea, searchBarCentered);
		this.contents.updateBounds(availableContentsArea, guiExclusionAreas, null);
		this.contents.updateLayout(false);

		final ImmutableRect2i searchAndConfigArea = getSearchAndConfigArea(displayArea, searchBarCentered, guiProperties);
		final ImmutableRect2i searchArea = searchAndConfigArea.cropRight(BUTTON_SIZE);
		final ImmutableRect2i configButtonArea = searchAndConfigArea.keepRight(BUTTON_SIZE);

		this.searchField.setValue(filterTextSource.getFilterText());
		this.searchField.updateBounds(searchArea);

		this.configButton.updateBounds(configButtonArea);
	}

	private static boolean isSearchBarCentered(IClientConfig clientConfig, IGuiProperties guiProperties) {
		return clientConfig.isCenterSearchBarEnabled() &&
			GuiProperties.getGuiBottom(guiProperties) + SEARCH_HEIGHT < guiProperties.screenHeight();
	}

	private ImmutableRect2i getAvailableContentsArea(ImmutableRect2i displayArea, boolean searchBarCentered) {
		if (searchBarCentered) {
			return displayArea;
		}
		return displayArea.cropBottom(SEARCH_HEIGHT + INNER_PADDING);
	}

	private ImmutableRect2i getSearchAndConfigArea(ImmutableRect2i displayArea, boolean searchBarCentered, IGuiProperties guiProperties) {
		displayArea = displayArea.insetBy(BORDER_MARGIN);
		if (searchBarCentered) {
			ImmutableRect2i guiRectangle = GuiProperties.getGuiRectangle(guiProperties);
			return displayArea
				.keepBottom(SEARCH_HEIGHT)
				.matchWidthAndX(guiRectangle);
		} else if (this.contents.hasRoom()) {
			final ImmutableRect2i contentsArea = this.contents.getBackgroundArea();
			return displayArea
				.keepBottom(SEARCH_HEIGHT)
				.matchWidthAndX(contentsArea);
		} else {
			return displayArea.keepBottom(SEARCH_HEIGHT);
		}
	}

	public void drawScreen(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (isListDisplayed()) {
			this.searchField.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
			this.contents.draw(minecraft, guiGraphics, mouseX, mouseY, partialTicks);
		}
		if (this.screenPropertiesCache.hasValidScreen()) {
			this.configButton.draw(guiGraphics, mouseX, mouseY, partialTicks);
		}
	}

	public void drawTooltips(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (isListDisplayed()) {
			this.contents.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
		}
		if (this.screenPropertiesCache.hasValidScreen()) {
			this.configButton.drawTooltips(guiGraphics, mouseX, mouseY);
		}
	}

	public void drawOnForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (isListDisplayed()) {
			this.contents.drawOnForeground(guiGraphics, mouseX, mouseY);
		}
	}

	@Override
	public Stream<IClickableIngredientInternal<?>> getIngredientUnderMouse(double mouseX, double mouseY) {
		if (isListDisplayed()) {
			return this.contents.getIngredientUnderMouse(mouseX, mouseY);
		}
		return Stream.empty();
	}

	@Override
	public Stream<IDraggableIngredientInternal<?>> getDraggableIngredientUnderMouse(double mouseX, double mouseY) {
		if (isListDisplayed()) {
			return this.contents.getDraggableIngredientUnderMouse(mouseX, mouseY);
		}
		return Stream.empty();
	}

	public IUserInputHandler createInputHandler() {
		final IUserInputHandler displayedInputHandler = new CombinedInputHandler(
			"IngredientListOverlay",
			this.searchField.createInputHandler(),
			this.configButton.createInputHandler(),
			this.contents.createInputHandler()
		);

		final IUserInputHandler configButtonInputHandler = this.configButton.createInputHandler();

		return new ProxyInputHandler(() -> {
			if (isListDisplayed()) {
				return displayedInputHandler;
			}
			if (this.screenPropertiesCache.hasValidScreen()) {
				return configButtonInputHandler;
			}
			return NullInputHandler.INSTANCE;
		});
	}

	public IDragHandler createDragHandler() {
		final IDragHandler displayedDragHandler = this.contents.createDragHandler();

		return new ProxyDragHandler(() -> {
			if (isListDisplayed()) {
				return displayedDragHandler;
			}
			return NullDragHandler.INSTANCE;
		});
	}

	@Override
	public boolean hasKeyboardFocus() {
		return isListDisplayed() && this.searchField.isFocused();
	}

	@Override
	public boolean onCharTyped(char codePoint, int modifiers) {
		return searchField.charTyped(codePoint, modifiers);
	}

	@Override
	public Optional<ITypedIngredient<?>> getIngredientUnderMouse() {
		if (isListDisplayed()) {
			double mouseX = MouseUtil.getX();
			double mouseY = MouseUtil.getY();
			return this.contents.getIngredientUnderMouse(mouseX, mouseY)
				.<ITypedIngredient<?>>map(IClickableIngredientInternal::getTypedIngredient)
				.findFirst();
		}
		return Optional.empty();
	}

	@Nullable
	@Override
	public <T> T getIngredientUnderMouse(IIngredientType<T> ingredientType) {
		if (isListDisplayed()) {
			double mouseX = MouseUtil.getX();
			double mouseY = MouseUtil.getY();
			return this.contents.getIngredientUnderMouse(mouseX, mouseY)
				.map(IClickableIngredientInternal::getTypedIngredient)
				.map(i -> i.getIngredient(ingredientType))
				.flatMap(Optional::stream)
				.findFirst()
				.orElse(null);
		}
		return null;
	}

	@Override
	public <T> List<T> getVisibleIngredients(IIngredientType<T> ingredientType) {
		if (isListDisplayed()) {
			return this.contents.getVisibleIngredients(ingredientType)
				.toList();
		}
		return Collections.emptyList();
	}
}
