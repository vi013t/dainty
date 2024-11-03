package violet.dainty.features.blocktooltips.impl.ui;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import violet.dainty.features.blocktooltips.api.fluid.JadeFluidObject;
import violet.dainty.features.blocktooltips.api.ui.Element;
import violet.dainty.features.blocktooltips.overlay.DisplayHelper;

public class FluidStackElement extends Element {

	private static final Vec2 DEFAULT_SIZE = new Vec2(16, 16);
	private final JadeFluidObject fluid;

	public FluidStackElement(JadeFluidObject fluid) {
		this.fluid = fluid;
		Objects.requireNonNull(fluid);
	}

	@Override
	public Vec2 getSize() {
		return DEFAULT_SIZE;
	}

	@Override
	public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
		Vec2 size = getCachedSize();
		DisplayHelper.INSTANCE.drawFluid(guiGraphics, x, y, fluid, size.x, size.y, JadeFluidObject.bucketVolume());
	}

	@Override
	public @Nullable String getMessage() {
		return null; //TODO
	}

}
