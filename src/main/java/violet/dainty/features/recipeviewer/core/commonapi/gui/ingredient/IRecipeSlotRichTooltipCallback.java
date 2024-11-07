package violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient;

import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;


/**
 * Used to add tooltips to ingredients drawn on a recipe.
 *
 * Implement a tooltip callback and add it with
 * {@link IRecipeSlotBuilder#addRichTooltipCallback(IRecipeSlotRichTooltipCallback)}
 *
 * @since 19.8.5
 */
@FunctionalInterface
public interface IRecipeSlotRichTooltipCallback {
	/**
	 * Add to the tooltip for an ingredient.
	 *
	 * @since 19.8.5
	 */
	void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip);
}
