package violet.dainty.features.recipeviewer.core.commonapi.gui.drawable;

import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

/**
 * Represents something to be drawn on screen.
 *
 * Useful for drawing miscellaneous things like in
 * {@link IRecipeCategory#draw(Object, IRecipeSlotsView, GuiGraphics, double, double)}.
 * {@link IRecipeSlotBuilder#setBackground(IDrawable, int, int)}
 * {@link IRecipeSlotBuilder#setOverlay(IDrawable, int, int)}]
 * and anywhere else things are drawn on the screen.
 *
 * @see IGuiHelper for many functions to create IDrawables.
 * @see IGuiHelper#createDrawableIngredient(IIngredientType, Object) to draw an ingredient.
 * @see IDrawableAnimated
 * @see IDrawableStatic
 */
public interface IDrawable {

	int getWidth();

	int getHeight();

	default void draw(GuiGraphics guiGraphics) {
		draw(guiGraphics, 0, 0);
	}

	void draw(GuiGraphics guiGraphics, int xOffset, int yOffset);

}
