package violet.dainty.features.recipeviewer.core.commonapi.gui.widgets;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.IPlaceable;

/**
 * A scrolling area for ingredients with a scrollbar.
 * Modeled after the vanilla creative menu.
 *
 * Create one with {@link IRecipeExtrasBuilder#addScrollGridWidget}.
 * @since 19.19.3
 */
public interface IScrollGridWidget extends ISlottedRecipeWidget, IPlaceable<IScrollGridWidget> {
	/**
	 * Get the position and size of this widget, relative to its parent element.
	 *
	 * @since 19.19.3
	 */
	ScreenRectangle getScreenRectangle();
}
