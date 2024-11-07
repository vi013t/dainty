package violet.dainty.features.recipeviewer.core.common.gui.elements;

import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableAnimated;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableStatic;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IScalableDrawable;

public record DrawableBlank(int width, int height) implements IDrawableStatic, IDrawableAnimated, IScalableDrawable {
	public static final DrawableBlank EMPTY = new DrawableBlank(0, 0);

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
		// draws nothing
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
		// draws nothing
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int x, int y, int width, int height) {
		// draws nothing
	}
}
