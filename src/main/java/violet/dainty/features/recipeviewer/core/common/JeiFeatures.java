package violet.dainty.features.recipeviewer.core.common;

import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiFeatures;

public class JeiFeatures implements IJeiFeatures {
	private boolean inventoryEffectRendererGuiHandlerEnabled = true;

	@Override
	public void disableInventoryEffectRendererGuiHandler() {
		inventoryEffectRendererGuiHandlerEnabled = false;
	}

	public boolean getInventoryEffectRendererGuiHandlerEnabled() {
		return inventoryEffectRendererGuiHandlerEnabled;
	}
}
