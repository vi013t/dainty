package violet.dainty.features.recipeviewer.core.library.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeWidget;

public class DrawableWidget implements IRecipeWidget {
	private final IDrawable drawable;
	private final ScreenPosition position;

	public DrawableWidget(IDrawable drawable, int xPos, int yPos) {
		this.drawable = drawable;
		this.position = new ScreenPosition(xPos, yPos);
	}
	@Override
	public ScreenPosition getPosition() {
		return position;
	}

	@Override
	public void drawWidget(GuiGraphics guiGraphics, double mouseX, double mouseY) {
		drawable.draw(guiGraphics);
	}
}
