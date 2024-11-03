package violet.dainty.features.blocktooltips.api.callback;

import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.ui.IBoxElement;
import violet.dainty.features.blocktooltips.api.ui.TooltipRect;

@FunctionalInterface
public interface JadeAfterRenderCallback {

	void afterRender(IBoxElement rootElement, TooltipRect rect, GuiGraphics guiGraphics, Accessor<?> accessor);

}
