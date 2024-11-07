package violet.dainty.features.recipeviewer.core.library.gui.recipes.layout.builder;

import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotRichTooltipCallback;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;

@SuppressWarnings("removal")
public class LegacyTooltipCallbackAdapter implements IRecipeSlotRichTooltipCallback {
	private final violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotTooltipCallback callback;

	public LegacyTooltipCallbackAdapter(violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotTooltipCallback callback) {
		this.callback = callback;
	}

	@Override
	public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
		callback.onRichTooltip(recipeSlotView, tooltip);
	}
}
