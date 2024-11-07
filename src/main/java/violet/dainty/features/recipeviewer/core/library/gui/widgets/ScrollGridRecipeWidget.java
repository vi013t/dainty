package violet.dainty.features.recipeviewer.core.library.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableSize2i;
import violet.dainty.features.recipeviewer.core.common.util.MathUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.inputs.IJeiInputHandler;
import violet.dainty.features.recipeviewer.core.commonapi.gui.inputs.RecipeSlotUnderMouse;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IScrollGridWidget;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedRecipeWidget;

import java.util.List;
import java.util.Optional;

public class ScrollGridRecipeWidget extends AbstractScrollWidget implements IScrollGridWidget, ISlottedRecipeWidget, IJeiInputHandler {
	private final IDrawable slotBackground;
	private final int columns;
	private final int visibleRows;
	private final int hiddenRows;
	private final List<IRecipeSlotDrawable> slots;

	public static ImmutableSize2i calculateSize(int columns, int visibleRows) {
		IDrawable slotBackground = Internal.getTextures().getSlot();
		return new ImmutableSize2i(
			columns * slotBackground.getWidth() + getScrollBoxScrollbarExtraWidth(),
			visibleRows * slotBackground.getHeight()
		);
	}

	public static ScrollGridRecipeWidget create(List<IRecipeSlotDrawable> slots, int columns, int visibleRows) {
		ImmutableSize2i size = calculateSize(columns, visibleRows);
		ImmutableRect2i area = new ImmutableRect2i(0, 0, size.width(), size.height());
		return new ScrollGridRecipeWidget(area, columns, visibleRows, slots);
	}

	public ScrollGridRecipeWidget(ImmutableRect2i area, int columns, int visibleRows, List<IRecipeSlotDrawable> slots) {
		super(area);
		this.slots = slots;
		this.slotBackground = Internal.getTextures().getSlot();

		this.columns = columns;
		this.visibleRows = visibleRows;
		int totalRows = MathUtil.divideCeil(slots.size(), columns);
		this.hiddenRows = Math.max(totalRows - visibleRows, 0);
	}

	@Override
	public ScrollGridRecipeWidget setPosition(int xPos, int yPos) {
		this.area = area.setPosition(xPos, yPos);
		return this;
	}

	@Override
	public int getWidth() {
		return area.width();
	}

	@Override
	public int getHeight() {
		return area.height();
	}

	@Override
	public ScreenRectangle getScreenRectangle() {
		return area.toScreenRectangle();
	}

	@Override
	protected int getVisibleAmount() {
		return visibleRows;
	}

	@Override
	protected int getHiddenAmount() {
		return hiddenRows;
	}

	@Override
	protected void drawContents(GuiGraphics guiGraphics, double mouseX, double mouseY, float scrollOffsetY) {
		final int totalSlots = slots.size();
		final int firstRow = getRowIndexForScroll(hiddenRows, getScrollOffsetY());
		final int firstIndex = columns * firstRow;

		final int slotWidth = slotBackground.getWidth();
		final int slotHeight = slotBackground.getHeight();

		for (int row = 0; row < visibleRows; row++) {
			final int y = row * slotHeight;
			for (int column = 0; column < columns; column++) {
				final int x = column * slotWidth;
				final int slotIndex = firstIndex + (row * columns) + column;
				slotBackground.draw(guiGraphics, x, y);
				if (slotIndex < totalSlots) {
					IRecipeSlotDrawable slot = slots.get(slotIndex);
					slot.setPosition(x + 1, y + 1);
					slot.draw(guiGraphics);
				}
			}
		}
	}

	@Override
	public Optional<RecipeSlotUnderMouse> getSlotUnderMouse(double mouseX, double mouseY) {
		final int firstRow = getRowIndexForScroll(hiddenRows, getScrollOffsetY());
		final int startIndex = firstRow * columns;
		final int endIndex = Math.min(startIndex + (visibleRows * columns), slots.size());
		for (int i = startIndex; i < endIndex; i++) {
			IRecipeSlotDrawable slot = slots.get(i);
			if (slot.isMouseOver(mouseX, mouseY)) {
				return Optional.of(new RecipeSlotUnderMouse(slot, getPosition()));
			}
		}
		return Optional.empty();
	}

	private int getRowIndexForScroll(int hiddenRows, float scrollOffset) {
		int rowIndex = (int) ((double) (scrollOffset * (float) hiddenRows) + 0.5D);
		return Math.max(rowIndex, 0);
	}

	@Override
	protected float calculateScrollAmount(double scrollDeltaY) {
		int hiddenRows = getHiddenAmount();
		return (float) (scrollDeltaY / (double) hiddenRows);
	}
}
