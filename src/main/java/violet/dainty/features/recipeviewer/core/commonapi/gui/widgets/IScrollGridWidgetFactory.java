package violet.dainty.features.recipeviewer.core.commonapi.gui.widgets;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;

/**
 * A helper for drawing a grid of recipe ingredients in a scrolling box.
 *
 * Get an instance from {@link IGuiHelper#createScrollGridFactory(int, int)}
 *
 * @since 19.7.0
 * @deprecated use {@link IRecipeExtrasBuilder#addScrollGridWidget} instead, it's much simpler
 */
@SuppressWarnings({"DeprecatedIsStillUsed", "removal"})
@Deprecated(since = "19.19.3", forRemoval = true)
public interface IScrollGridWidgetFactory<R> extends ISlottedWidgetFactory<R> {
	/**
	 * @since 19.7.0
	 */
	void setPosition(int x, int y);
	/**
	 * @since 19.7.0
	 */
	ScreenRectangle getArea();
}