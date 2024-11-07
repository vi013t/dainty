package violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient;

import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to add tooltips to ingredients drawn on a recipe.
 *
 * Implement a tooltip callback and add it with
 * {@link IRecipeSlotBuilder#addTooltipCallback(IRecipeSlotTooltipCallback)}
 *
 * @since 9.3.0
 * @deprecated use {@link IRecipeSlotRichTooltipCallback}
 */
@SuppressWarnings({"removal", "DeprecatedIsStillUsed"})
@Deprecated(since = "19.8.5", forRemoval = true)
@FunctionalInterface
public interface IRecipeSlotTooltipCallback {
	/**
	 * Change the tooltip for an ingredient.
	 *
	 * @since 9.3.0
	 * @deprecated in favor of {@link IRecipeSlotRichTooltipCallback}
	 */
	@Deprecated(since = "19.5.4", forRemoval = true)
	void onTooltip(IRecipeSlotView recipeSlotView, List<Component> tooltip);

	/**
	 * Add to the tooltip for an ingredient.
	 *
	 * @since 19.5.4
	 * @deprecated in favor of {@link IRecipeSlotRichTooltipCallback}
	 */
	@Deprecated(since = "19.8.5", forRemoval = true)
	@SuppressWarnings({"removal", "DeprecatedIsStillUsed"})
	default void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
		List<Component> components = tooltip.toLegacyToComponents();
		List<Component> changedComponents = new ArrayList<>(components);
		onTooltip(recipeSlotView, changedComponents);
		if (!components.equals(changedComponents)) {
			tooltip.removeAll(components);
			tooltip.addAll(changedComponents);
		}
	}
}
