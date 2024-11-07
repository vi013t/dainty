package violet.dainty.features.recipeviewer.core.common.gui.elements;

import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.IPlaceable;

/**
 * Draws with a built-in offset.
 */
public class OffsetDrawable implements IDrawable, IPlaceable<OffsetDrawable> {
	public static IDrawable create(IDrawable drawable, int xOffset, int yOffset) {
		if (xOffset == 0 && yOffset == 0) {
			return drawable;
		}
		return new OffsetDrawable(drawable, xOffset, yOffset);
	}

	private final IDrawable drawable;
	private int xOffset;
	private int yOffset;

	public OffsetDrawable(IDrawable drawable, int xOffset, int yOffset) {
		this.drawable = drawable;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	@Override
	public int getWidth() {
		return drawable.getWidth();
	}

	@Override
	public int getHeight() {
		return drawable.getHeight();
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
		this.drawable.draw(
			guiGraphics,
			this.xOffset + xOffset,
			this.yOffset + yOffset
		);
	}

	@Override
	public void draw(GuiGraphics guiGraphics) {
		this.drawable.draw(guiGraphics, this.xOffset, this.yOffset);
	}

	@Override
	public OffsetDrawable setPosition(int xPos, int yPos) {
		this.xOffset = xPos;
		this.yOffset = yPos;
		return this;
	}

	public ImmutableRect2i getArea() {
		return new ImmutableRect2i(xOffset, yOffset, getWidth(), getHeight());
	}
}
