package violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.gui.input.IDragHandler;
import violet.dainty.features.recipeviewer.core.gui.input.IDraggableIngredientInternal;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IElement;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BookmarkDragManager {
	private final BookmarkOverlay bookmarkOverlay;
	private @Nullable BookmarkDrag<?> bookmarkDrag;

	public BookmarkDragManager(BookmarkOverlay bookmarkOverlay) {
		this.bookmarkOverlay = bookmarkOverlay;
	}

	public void updateDrag(int mouseX, int mouseY) {
		if (bookmarkDrag != null) {
			bookmarkDrag.update(mouseX, mouseY);
		}
	}

	public boolean drawDraggedItem(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (bookmarkDrag != null) {
			return bookmarkDrag.drawItem(guiGraphics, mouseX, mouseY);
		}
		return false;
	}

	public void stopDrag() {
		if (this.bookmarkDrag != null) {
			this.bookmarkDrag.stop();
			this.bookmarkDrag = null;
		}
	}

	private <V> boolean handleClickIngredient(IDraggableIngredientInternal<V> clicked, UserInput input) {
		IElement<V> element = clicked.getElement();
		return element
			.getBookmark()
			.map(bookmark -> {
				ITypedIngredient<V> ingredient = clicked.getTypedIngredient();
				IIngredientType<V> type = ingredient.getType();

				List<IBookmarkDragTarget> targets = bookmarkOverlay.createBookmarkDragTargets();
				IIngredientManager ingredientManager = Internal.getJeiRuntime().getIngredientManager();
				IIngredientRenderer<V> ingredientRenderer = ingredientManager.getIngredientRenderer(type);
				ImmutableRect2i clickedArea = clicked.getArea();
				this.bookmarkDrag = new BookmarkDrag<>(
					bookmarkOverlay,
					targets,
					ingredientRenderer,
					ingredient,
					bookmark,
					input.getMouseX(),
					input.getMouseY(),
					clickedArea
				);
				return true;
			})
			.orElse(false);
	}

	public IDragHandler createDragHandler() {
		return new DragHandler();
	}

	private class DragHandler implements IDragHandler {
		@Override
		public Optional<IDragHandler> handleDragStart(Screen screen, UserInput input) {
			IClientConfig clientConfig = Internal.getJeiClientConfigs().getClientConfig();
			if (!clientConfig.isDragToRearrangeBookmarksEnabled()) {
				stopDrag();
				return Optional.empty();
			}

			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer player = minecraft.player;
			if (player == null) {
				return Optional.empty();
			}

			return bookmarkOverlay.getDraggableIngredientUnderMouse(input.getMouseX(), input.getMouseY())
				.findFirst()
				.flatMap(clicked -> {
					ItemStack mouseItem = player.containerMenu.getCarried();
					if (mouseItem.isEmpty() &&
						handleClickIngredient(clicked, input)) {
						return Optional.of(this);
					}
					return Optional.empty();
				});
		}

		@Override
		public boolean handleDragComplete(Screen screen, UserInput input) {
			if (bookmarkDrag == null) {
				return false;
			}
			boolean success = bookmarkDrag.onClick(input);
			bookmarkDrag = null;
			return success;
		}

		@Override
		public void handleDragCanceled() {
			stopDrag();
		}
	}
}
