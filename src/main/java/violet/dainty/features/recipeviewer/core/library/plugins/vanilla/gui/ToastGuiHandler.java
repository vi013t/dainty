package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.gui;

import net.minecraft.client.renderer.Rect2i;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformScreenHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.handlers.IGlobalGuiHandler;

import java.util.Collection;
import java.util.List;

public class ToastGuiHandler implements IGlobalGuiHandler {
	@Override
	public Collection<Rect2i> getGuiExtraAreas() {
		IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
		ImmutableRect2i toastsArea = screenHelper.getToastsArea();
		if (toastsArea.isEmpty()) {
			return List.of();
		}
		return List.of(toastsArea.toMutable());
	}
}
