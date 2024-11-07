package violet.dainty.features.recipeviewer.core.library.gui.recipes;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;

public class ShapelessIcon {
	private final IDrawable icon;
	private final ImmutableRect2i area;

	public ShapelessIcon(IDrawable icon, int x, int y) {
		this.icon = icon;
		this.area = new ImmutableRect2i(x, y, icon.getWidth(), icon.getHeight());
	}

	public void draw(GuiGraphics guiGraphics) {
		icon.draw(guiGraphics, area.getX(), area.getY());
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		return area.contains(mouseX, mouseY);
	}

	public void addTooltip(JeiTooltip tooltip) {
		tooltip.add(Component.translatable("dainty.tooltip.shapeless.recipe"));
	}
}
