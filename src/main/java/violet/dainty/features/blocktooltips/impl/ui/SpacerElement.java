package violet.dainty.features.blocktooltips.impl.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import violet.dainty.features.blocktooltips.api.ui.Element;

public class SpacerElement extends Element {

	private final Vec2 dimension;

	public SpacerElement(Vec2 dimension) {
		this.dimension = dimension;
	}

	@Override
	public Vec2 getSize() {
		return dimension;
	}

	@Override
	public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
	}
}
