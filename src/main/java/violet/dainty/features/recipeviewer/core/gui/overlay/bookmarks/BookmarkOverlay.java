package violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.util.ImmutablePoint2i;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.handlers.IGuiProperties;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IBookmarkOverlay;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.BookmarkList;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.IBookmark;
import violet.dainty.features.recipeviewer.core.gui.elements.GuiIconToggleButton;
import violet.dainty.features.recipeviewer.core.gui.input.IClickableIngredientInternal;
import violet.dainty.features.recipeviewer.core.gui.input.IDragHandler;
import violet.dainty.features.recipeviewer.core.gui.input.IDraggableIngredientInternal;
import violet.dainty.features.recipeviewer.core.gui.input.IPaged;
import violet.dainty.features.recipeviewer.core.gui.input.IRecipeFocusSource;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.MouseUtil;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.CombinedDragHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.CombinedInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.NullDragHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.ProxyDragHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.ProxyInputHandler;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientGridWithNavigation;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientListSlot;
import violet.dainty.features.recipeviewer.core.gui.overlay.ScreenPropertiesCache;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IElement;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class BookmarkOverlay implements IRecipeFocusSource, IBookmarkOverlay {
	private static final int BORDER_MARGIN = 6;
	private static final int INNER_PADDING = 2;
	private static final int BUTTON_SIZE = 20;

	// input
	private final BookmarkDragManager bookmarkDragManager;

	// areas
	private final ScreenPropertiesCache screenPropertiesCache;

	// display elements
	private final IngredientGridWithNavigation contents;
	private final GuiIconToggleButton bookmarkButton;

	// data
	private final BookmarkList bookmarkList;
	private final IClientToggleState toggleState;

	public BookmarkOverlay(
		BookmarkList bookmarkList,
		IngredientGridWithNavigation contents,
		IClientToggleState toggleState,
		IScreenHelper screenHelper,
		IInternalKeyMappings keyBindings
	) {
		this.bookmarkList = bookmarkList;
		this.toggleState = toggleState;
		this.bookmarkButton = BookmarkButton.create(this, bookmarkList, toggleState, keyBindings);
		this.contents = contents;
		this.screenPropertiesCache = new ScreenPropertiesCache(screenHelper);
		this.bookmarkDragManager = new BookmarkDragManager(this);
		bookmarkList.addSourceListChangedListener(() -> {
			toggleState.setBookmarkEnabled(!bookmarkList.isEmpty());
			Minecraft minecraft = Minecraft.getInstance();
			this.getScreenPropertiesUpdater()
				.updateScreen(minecraft.screen)
				.update();
		});
	}

	public boolean isListDisplayed() {
		return toggleState.isBookmarkOverlayEnabled() &&
			screenPropertiesCache.hasValidScreen() &&
			contents.hasRoom() &&
			!bookmarkList.isEmpty();
	}

	public boolean hasRoom() {
		return contents.hasRoom();
	}

	public ScreenPropertiesCache.Updater getScreenPropertiesUpdater() {
		return this.screenPropertiesCache.getUpdater(this::onScreenPropertiesChanged);
	}

	private void onScreenPropertiesChanged() {
		this.screenPropertiesCache.getGuiProperties()
			.ifPresentOrElse(this::updateBounds, this.contents::close);
	}

	private void updateBounds(IGuiProperties guiProperties) {
		ImmutableRect2i displayArea = getDisplayArea(guiProperties);
		Set<ImmutableRect2i> guiExclusionAreas = this.screenPropertiesCache.getGuiExclusionAreas();
		ImmutablePoint2i mouseExclusionArea = this.screenPropertiesCache.getMouseExclusionArea();

		ImmutableRect2i availableContentsArea = displayArea.cropBottom(BUTTON_SIZE + INNER_PADDING);
		this.contents.updateBounds(availableContentsArea, guiExclusionAreas, mouseExclusionArea);
		this.contents.updateLayout(false);

		if (contents.hasRoom()) {
			ImmutableRect2i contentsArea = this.contents.getBackgroundArea();
			ImmutableRect2i bookmarkButtonArea = displayArea
				.insetBy(BORDER_MARGIN)
				.matchWidthAndX(contentsArea)
				.keepBottom(BUTTON_SIZE)
				.keepLeft(BUTTON_SIZE);
			this.bookmarkButton.updateBounds(bookmarkButtonArea);
		} else {
			ImmutableRect2i bookmarkButtonArea = displayArea
				.insetBy(BORDER_MARGIN)
				.keepBottom(BUTTON_SIZE)
				.keepLeft(BUTTON_SIZE);
			this.bookmarkButton.updateBounds(bookmarkButtonArea);
		}
	}

	private static ImmutableRect2i getDisplayArea(IGuiProperties guiProperties) {
		int width = guiProperties.guiLeft();
		if (width <= 0) {
			width = 0;
		}
		int screenHeight = guiProperties.screenHeight();
		return new ImmutableRect2i(0, 0, width, screenHeight);
	}

	public void drawScreen(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (isListDisplayed()) {
			this.bookmarkDragManager.updateDrag(mouseX, mouseY);
			this.contents.draw(minecraft, guiGraphics, mouseX, mouseY, partialTicks);
		}
		if (this.screenPropertiesCache.hasValidScreen()) {
			this.bookmarkButton.draw(guiGraphics, mouseX, mouseY, partialTicks);
		}
	}

	public void drawTooltips(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (isListDisplayed()) {
			if (!this.bookmarkDragManager.drawDraggedItem(guiGraphics, mouseX, mouseY)) {
				this.contents.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
			}
		}
		if (this.screenPropertiesCache.hasValidScreen()) {
			bookmarkButton.drawTooltips(guiGraphics, mouseX, mouseY);
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

	@Override
	public Optional<ITypedIngredient<?>> getIngredientUnderMouse() {
		double mouseX = MouseUtil.getX();
		double mouseY = MouseUtil.getY();
		return getIngredientUnderMouse(mouseX, mouseY)
			.<ITypedIngredient<?>>map(IClickableIngredientInternal::getTypedIngredient)
			.findFirst();
	}

	@Nullable
	@Override
	public <T> T getIngredientUnderMouse(IIngredientType<T> ingredientType) {
		double mouseX = MouseUtil.getX();
		double mouseY = MouseUtil.getY();
		return getIngredientUnderMouse(mouseX, mouseY)
			.map(IClickableIngredientInternal::getTypedIngredient)
			.map(i -> i.getIngredient(ingredientType))
			.flatMap(Optional::stream)
			.findFirst()
			.orElse(null);
	}

	public IUserInputHandler createInputHandler() {
		final IUserInputHandler bookmarkButtonInputHandler = this.bookmarkButton.createInputHandler();

		final IUserInputHandler displayedInputHandler = new CombinedInputHandler(
			"BookmarkOverlay",
			this.contents.createInputHandler(),
			bookmarkButtonInputHandler
		);

		return new ProxyInputHandler(() -> {
			if (isListDisplayed()) {
				return displayedInputHandler;
			}
			return bookmarkButtonInputHandler;
		});
	}

	public IDragHandler createDragHandler() {
		final IDragHandler combinedDragHandlers = new CombinedDragHandler(
			this.contents.createDragHandler(),
			this.bookmarkDragManager.createDragHandler()
		);

		return new ProxyDragHandler(() -> {
			if (isListDisplayed()) {
				return combinedDragHandlers;
			}
			return NullDragHandler.INSTANCE;
		});
	}

	public void drawOnForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (isListDisplayed()) {
			this.contents.drawOnForeground(guiGraphics, mouseX, mouseY);
		}
	}

	public List<IBookmarkDragTarget> createBookmarkDragTargets() {
		List<DragTarget> slotTargets = this.contents.getSlots()
			.map(this::createDragTarget)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.toList();

		IBookmark firstBookmark = slotTargets.getFirst().bookmark;
		IBookmark lastBookmark = slotTargets.getLast().bookmark;

		List<IBookmarkDragTarget> bookmarkDragTargets = new ArrayList<>(slotTargets);

		IPaged pageDelegate = this.contents.getPageDelegate();
		if (pageDelegate.getPageCount() > 1) {
			// if a bookmark is dropped on the next button, put it on the next page
			bookmarkDragTargets.add(new ActionDragTarget(this.contents.getNextPageButtonArea(), lastBookmark, bookmarkList, 1, pageDelegate::nextPage));

			// if a bookmark is dropped on the back button, put it on the previous page
			bookmarkDragTargets.add(new ActionDragTarget(this.contents.getBackButtonArea(), firstBookmark, bookmarkList, -1, pageDelegate::previousPage));
		}

		// if a bookmark is dropped somewhere else in the contents area, put it at the end of the current page
		bookmarkDragTargets.add(new DragTarget(this.contents.getSlotBackgroundArea(), lastBookmark, bookmarkList, 0));

		return bookmarkDragTargets;
	}

	private Optional<DragTarget> createDragTarget(IngredientListSlot ingredientListSlot) {
		return ingredientListSlot.getOptionalElement()
			.flatMap(IElement::getBookmark)
			.map(bookmark -> new DragTarget(ingredientListSlot.getArea(), bookmark, bookmarkList, 0));
	}

	public static class ActionDragTarget extends DragTarget {
		private final Runnable action;

		public ActionDragTarget(ImmutableRect2i area, IBookmark bookmark, BookmarkList bookmarkList, int offset, Runnable action) {
			super(area, bookmark, bookmarkList, offset);
			this.action = action;
		}

		@Override
		public void accept(IBookmark bookmark) {
			super.accept(bookmark);
			action.run();
		}
	}

	public static class DragTarget implements IBookmarkDragTarget {
		private final ImmutableRect2i area;
		private final IBookmark bookmark;
		private final BookmarkList bookmarkList;
		private final int offset;

		public DragTarget(ImmutableRect2i area, IBookmark bookmark, BookmarkList bookmarkList, int offset) {
			this.area = area;
			this.bookmark = bookmark;
			this.bookmarkList = bookmarkList;
			this.offset = offset;
		}

		@Override
		public ImmutableRect2i getArea() {
			return area;
		}

		@Override
		public void accept(IBookmark bookmark) {
			bookmarkList.moveBookmark(this.bookmark, bookmark, offset);
		}
	}
}