package violet.dainty.features.recipeviewer.core.commonapi.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.gui.inputs.IJeiInputHandler;
import violet.dainty.features.recipeviewer.core.commonapi.gui.inputs.RecipeSlotUnderMouse;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

import java.util.Optional;

/**
 * For addons that want to draw recipe layouts somewhere themselves.
 *
 * Create an instance with {@link IRecipeManager#createRecipeLayoutDrawable(IRecipeCategory, Object, IFocusGroup)}.
 *
 * @since 3.13.2
 */
public interface IRecipeLayoutDrawable<R> {
	/**
	 * Set the position of the recipe layout in screen coordinates.
	 * To help decide on the position, you can get the width and height of this recipe from {@link #getRect()}.
	 *
	 * @since 3.13.2
	 */
	void setPosition(int posX, int posY);

	/**
	 * Draw the recipe without overlays such as item tool tips.
	 *
	 * @since 3.13.2
	 */
	void drawRecipe(GuiGraphics guiGraphics, int mouseX, int mouseY);

	/**
	 * Draw the recipe overlays such as item tool tips.
	 *
	 * @since 4.7.4
	 */
	void drawOverlays(GuiGraphics guiGraphics, int mouseX, int mouseY);

	/**
	 * Returns true if the mouse is hovering over the recipe.
	 * Hovered recipes should be drawn after other recipes to have the drawn tooltips overlap other elements properly.
	 *
	 * @since 3.13.2
	 */
	boolean isMouseOver(double mouseX, double mouseY);

	/**
	 * Returns the ItemStack currently under the mouse, if there is one.
	 *
	 * @see #getIngredientUnderMouse(int, int, IIngredientType) to get other types of ingredient.
	 *
	 * @since 11.1.1
	 */
	default Optional<ItemStack> getItemStackUnderMouse(int mouseX, int mouseY) {
		return getIngredientUnderMouse(mouseX, mouseY, VanillaTypes.ITEM_STACK);
	}

	/**
	 * Returns the ingredient currently under the mouse, if there is one.
	 * Can be an ItemStack, FluidStack, or any other ingredient type registered with JEI.
	 *
	 * @since 11.0.0
	 */
	<T> Optional<T> getIngredientUnderMouse(int mouseX, int mouseY, IIngredientType<T> ingredientType);

	/**
	 * Get the recipe slot currently under the mouse, if there is one.
	 * @since 11.5.0
	 *
	 * @deprecated use {@link #getSlotUnderMouse(double, double)}
	 */
	@Deprecated
	Optional<IRecipeSlotDrawable> getRecipeSlotUnderMouse(double mouseX, double mouseY);

	/**
	 * Get the recipe slot currently under the mouse, if there is one.
	 *
	 * @return the slot under the mouse, with an offset
	 *
	 * @since 19.6.0
	 */
	Optional<RecipeSlotUnderMouse> getSlotUnderMouse(double mouseX, double mouseY);

	/**
	 * Get position and size for the recipe in absolute screen coordinates.
	 * @since 11.5.0
	 */
	Rect2i getRect();

	/**
	 * Get position and size for the recipe, including the border drawn around it, in absolute screen coordinates.
	 * @since 19.1.1
	 */
	Rect2i getRectWithBorder();

	/**
	 * Get the position of the recipe transfer button area, relative to the recipe layout drawable.
	 * @since 11.5.0
	 */
	Rect2i getRecipeTransferButtonArea();

	/**
	 * Get the position of the recipe bookmark button area, relative to the recipe layout drawable.
	 * @since 19.1.0
	 */
	Rect2i getRecipeBookmarkButtonArea();

	/**
	 * Get a view of the recipe slots for this recipe layout.
	 * @since 11.5.0
	 */
	IRecipeSlotsView getRecipeSlotsView();

	/**
	 * Get the recipe category that this recipe layout is a part of.
	 * @since 11.5.0
	 */
	IRecipeCategory<R> getRecipeCategory();

	/**
	 * Get the recipe that this recipe layout displays.
	 * @since 11.5.0
	 */
	R getRecipe();

	/**
	 * Get the input handler for this recipe layout.
	 *
	 * @since 19.6.0
	 */
	IJeiInputHandler getInputHandler();

	/**
	 * Update the recipe layout on game tick.
	 *
	 * @since 19.7.0
	 */
	void tick();
}
