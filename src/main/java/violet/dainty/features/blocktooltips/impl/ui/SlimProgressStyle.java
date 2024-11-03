package violet.dainty.features.blocktooltips.impl.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import violet.dainty.features.blocktooltips.api.ui.ScreenDirection;
import violet.dainty.features.blocktooltips.api.ui.ProgressStyle;
import violet.dainty.features.blocktooltips.overlay.DisplayHelper;

public class SlimProgressStyle extends ProgressStyle {

	public int color;

	@Override
	public ProgressStyle color(int color, int color2) {
		if (color != color2) {
			throw new UnsupportedOperationException();
		}
		this.color = color;
		return this;
	}

	@Override
	public ProgressStyle textColor(int color) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ProgressStyle direction(ScreenDirection direction) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void render(GuiGraphics guiGraphics, float x, float y, float w, float h, float progress, Component text) {
		if (overlay != null) {
			Vec2 size = new Vec2(w * progress, h);
			overlay.size(size);
			overlay.render(guiGraphics, x, y, size.x, size.y);
		} else {
			DisplayHelper.INSTANCE.drawGradientProgress(guiGraphics, x, y, w, h, progress, color);
		}
	}

}
