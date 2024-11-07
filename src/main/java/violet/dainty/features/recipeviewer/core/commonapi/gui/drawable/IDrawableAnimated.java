package violet.dainty.features.recipeviewer.core.commonapi.gui.drawable;

import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ITickTimer;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

/**
 * An animated {@link IDrawable}, useful for showing a gui animation like furnace flames or progress arrows.
 *
 * Useful for drawing miscellaneous things in
 * {@link IRecipeCategory#draw(Object, IRecipeSlotsView, GuiGraphics, double, double)}.
 *
 * To create an instance, call
 * {@link IGuiHelper#createAnimatedDrawable(IDrawableStatic, int, StartDirection, boolean)}
 *
 * Internally, these use an {@link ITickTimer} to simulate tick-driven animations.
 */
public interface IDrawableAnimated extends IDrawable {
	/**
	 * The direction that the animation starts from.
	 *
	 * @see IGuiHelper#createAnimatedDrawable(IDrawableStatic, int, StartDirection, boolean)
	 */
	enum StartDirection {
		TOP, BOTTOM, LEFT, RIGHT
	}
}
