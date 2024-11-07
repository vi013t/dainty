package violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.util.ImmutablePoint2i;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.common.util.MathUtil;
import violet.dainty.features.recipeviewer.core.common.util.SafeIngredientUtil;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.IBookmark;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.List;

public class BookmarkDrag<T> {
	private final BookmarkOverlay bookmarkOverlay;
	private final List<IBookmarkDragTarget> targets;
	private final IIngredientRenderer<T> ingredientRenderer;
	private final ITypedIngredient<T> ingredient;
	private final double mouseStartX;
	private final double mouseStartY;
	private final IBookmark bookmark;
	private final ImmutableRect2i origin;
	private final long dragCanStartTime;

	public BookmarkDrag(
		BookmarkOverlay bookmarkOverlay,
		List<IBookmarkDragTarget> targets,
		IIngredientRenderer<T> ingredientRenderer,
		ITypedIngredient<T> ingredient,
		IBookmark bookmark,
		double mouseX,
		double mouseY,
		ImmutableRect2i origin
	) {
		this.bookmarkOverlay = bookmarkOverlay;
		this.targets = targets;
		this.ingredientRenderer = ingredientRenderer;
		this.ingredient = ingredient;
		this.bookmark = bookmark;
		this.origin = origin;
		this.mouseStartX = mouseX;
		this.mouseStartY = mouseY;
		IClientConfig clientConfig = Internal.getJeiClientConfigs().getClientConfig();
		this.dragCanStartTime = System.currentTimeMillis() + clientConfig.getDragDelayMs();
	}

	public static boolean canStart(BookmarkDrag<?> drag, double mouseX, double mouseY) {
		if (System.currentTimeMillis() < drag.dragCanStartTime) {
			return false;
		}
		ImmutableRect2i origin = drag.origin;
		final Vec2 center;
		if (origin.isEmpty()) {
			center = new Vec2((float) drag.mouseStartX, (float) drag.mouseStartY);
		} else {
			if (origin.contains(mouseX, mouseY)) {
				return false;
			}
			center = new Vec2(
				origin.getX() + (origin.getWidth() / 2.0f),
				origin.getY() + (origin.getHeight() / 2.0f)
			);
		}

		double mouseXDist = center.x - mouseX;
		double mouseYDist = center.y - mouseY;
		double mouseDistSq = mouseXDist * mouseXDist + mouseYDist * mouseYDist;
		return mouseDistSq > 64.0;
	}

	public void update(int mouseX, int mouseY) {
		if (bookmark.isVisible() && !canStart(this, mouseX, mouseY)) {
			return;
		}

		bookmark.setVisible(false);
		bookmarkOverlay.getScreenPropertiesUpdater()
			.updateMouseExclusionArea(new ImmutablePoint2i(mouseX, mouseY))
			.update();
	}

	public boolean drawItem(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (bookmark.isVisible()) {
			return false;
		}

		SafeIngredientUtil.render(guiGraphics, ingredientRenderer, ingredient, mouseX - 8, mouseY - 8);
		return true;
	}

	public boolean onClick(UserInput input) {
		if (bookmark.isVisible()) {
			return false;
		}

		for (IBookmarkDragTarget target : targets) {
			ImmutableRect2i area = target.getArea();
			if (MathUtil.contains(area, input.getMouseX(), input.getMouseY())) {
				if (!input.isSimulate()) {
					target.accept(bookmark);
					stop();
					return true;
				}
			}
		}
		if (!input.isSimulate()) {
			stop();
		}
		return false;
	}

	public void stop() {
		bookmark.setVisible(true);
		bookmarkOverlay.getScreenPropertiesUpdater()
			.updateMouseExclusionArea(null)
			.update();
	}
}
