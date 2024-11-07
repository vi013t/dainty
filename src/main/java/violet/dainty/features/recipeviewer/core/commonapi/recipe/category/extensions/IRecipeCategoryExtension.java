package violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.ICraftingGridHelper;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotRichTooltipCallback;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.gui.inputs.IJeiInputHandler;
import violet.dainty.features.recipeviewer.core.commonapi.gui.inputs.IJeiUserInput;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

import java.util.Collections;
import java.util.List;

/**
 * An extension to a recipe category with methods that allow JEI to make sense of it.
 * Plugins implement these for recipe categories that support it, for each type of recipe they have.
 *
 * @apiNote Since 16.0.0, extensions have the recipe passed to them in each method,
 * so they can be singleton instances instead of creating many of them to wrap recipes.
 */
public interface IRecipeCategoryExtension<T> {
	/**
	 * Draw additional info about the recipe.
	 * Use the mouse position for things like button highlights.
	 * Tooltips are handled by {@link #getTooltipStrings(Object, double, double)}
	 *
	 * @param mouseX the X position of the mouse, relative to the recipe.
	 * @param mouseY the Y position of the mouse, relative to the recipe.
	 * @see IDrawable for a simple class for drawing things.
	 * @see IGuiHelper for useful functions.
	 * @since 16.0.0
	 */
	default void drawInfo(T recipe, int recipeWidth, int recipeHeight, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		drawInfo(recipeWidth, recipeHeight, guiGraphics, mouseX, mouseY);
	}

	/**
	 * Get the tooltip for whatever is under the mouse.
	 * ItemStack and fluid tooltips are already handled by JEI, this is for anything else.
	 *
	 * To add to ingredient tooltips, see {@link IRecipeSlotBuilder#addRichTooltipCallback(IRecipeSlotRichTooltipCallback)}
	 * To add tooltips for a recipe category, see {@link IRecipeCategory#getTooltip}
	 *
	 * @param mouseX the X position of the mouse, relative to the recipe.
	 * @param mouseY the Y position of the mouse, relative to the recipe.
	 * @since 19.5.4
	 */
	default void getTooltip(ITooltipBuilder tooltip, T recipe, double mouseX, double mouseY) {
		List<Component> tooltipStrings = getTooltipStrings(recipe, mouseX, mouseY);
		tooltip.addAll(tooltipStrings);
	}

	/**
	 * Get the tooltip for whatever is under the mouse.
	 * ItemStack and fluid tooltips are already handled by JEI, this is for anything else.
	 *
	 * To add to ingredient tooltips, see {@link IRecipeSlotBuilder#addRichTooltipCallback(IRecipeSlotRichTooltipCallback)}
	 * To add tooltips for a recipe category, see {@link IRecipeCategory#getTooltip}
	 *
	 * @param mouseX the X position of the mouse, relative to the recipe.
	 * @param mouseY the Y position of the mouse, relative to the recipe.
	 * @return tooltip strings. If there is no tooltip at this position, return an empty list.
	 * @since 16.0.0
	 * @deprecated use {@link #getTooltip}
	 */
	@SuppressWarnings("DeprecatedIsStillUsed")
	@Deprecated(since = "19.5.4", forRemoval = true)
	default List<Component> getTooltipStrings(T recipe, double mouseX, double mouseY) {
		return getTooltipStrings(mouseX, mouseY);
	}

	/**
	 * Sets the extras for the recipe category, like input handlers and recipe widgets.
	 *
	 * Recipe Widgets persist as long as a recipe layout is on screen,
	 * so they can be used for caching and displaying recipe-specific
	 * information more easily than from the recipe category directly.
	 *
	 * @since 19.19.0
	 * @deprecated use {@link #createRecipeExtras(Object, IRecipeExtrasBuilder, ICraftingGridHelper, IFocusGroup)}, the recipe slots are in {@link IRecipeExtrasBuilder#getRecipeSlots()} now.
	 */
	@Deprecated(since = "19.19.3", forRemoval = true)
	default void createRecipeExtras(T recipe, IRecipeExtrasBuilder builder, IRecipeSlotsView recipeSlotsView, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {

	}

	/**
	 * Sets the extras for the recipe category, like input handlers and recipe widgets.
	 *
	 * Recipe Widgets persist as long as a recipe layout is on screen,
	 * so they can be used for caching and displaying recipe-specific
	 * information more easily than from the recipe category directly.
	 *
	 * @since 19.6.0
	 */
	@SuppressWarnings("RedundantUnmodifiable")
	default void createRecipeExtras(T recipe, IRecipeExtrasBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		createRecipeExtras(recipe, builder, () -> Collections.unmodifiableList(builder.getRecipeSlots().getSlots()), craftingGridHelper, focuses);
	}

	/**
	 * Called when a player inputs while hovering over the recipe.
	 * Useful for implementing buttons, hyperlinks, and other interactions to your recipe.
	 *
	 * @param mouseX the X position of the mouse, relative to the recipe.
	 * @param mouseY the Y position of the mouse, relative to the recipe.
	 * @param input  the current input from the player.
	 * @return true if the input was handled, false otherwise
	 * @since 16.0.0
	 *
	 * @deprecated create a {@link IJeiInputHandler} to handle inputs using {@link IRecipeExtrasBuilder#addInputHandler}, then
	 * use {@link IJeiInputHandler#handleInput(double, double, IJeiUserInput)}
	 */
	@SuppressWarnings("DeprecatedIsStillUsed")
	@Deprecated(since = "19.6.0", forRemoval = true)
	default boolean handleInput(T recipe, double mouseX, double mouseY, InputConstants.Key input) {
		return handleInput(mouseX, mouseY, input);
	}

	/**
	 * @return true if the given recipe can be handled by this category extension.
	 * @since 16.0.0
	 */
	default boolean isHandled(T recipe) {
		return true;
	}

	/**
	 * Draw additional info about the recipe.
	 * Use the mouse position for things like button highlights.
	 * Tooltips are handled by {@link #getTooltipStrings(Object, double, double)}
	 *
	 * @param mouseX the X position of the mouse, relative to the recipe.
	 * @param mouseY the Y position of the mouse, relative to the recipe.
	 * @see IDrawable for a simple class for drawing things.
	 * @see IGuiHelper for useful functions.
	 * @deprecated use {@link #drawInfo(Object, int, int, GuiGraphics, double, double)}
	 */
	@SuppressWarnings("DeprecatedIsStillUsed")
	@Deprecated(since = "16.0.0", forRemoval = true)
	default void drawInfo(int recipeWidth, int recipeHeight, GuiGraphics guiGraphics, double mouseX, double mouseY) {

	}

	/**
	 * Get the tooltip for whatever is under the mouse.
	 * ItemStack and fluid tooltips are already handled by JEI, this is for anything else.
	 *
	 * To add to ingredient tooltips, see {@link IRecipeSlotBuilder#addRichTooltipCallback(IRecipeSlotRichTooltipCallback)}
	 * To add tooltips for a recipe category, see {@link IRecipeCategory#getTooltip}
	 *
	 * @param mouseX the X position of the mouse, relative to the recipe.
	 * @param mouseY the Y position of the mouse, relative to the recipe.
	 * @return tooltip strings. If there is no tooltip at this position, return an empty list.
	 * @deprecated use {@link #getTooltipStrings(Object, double, double)}
	 */
	@Deprecated(since = "16.0.0", forRemoval = true)
	default List<Component> getTooltipStrings(double mouseX, double mouseY) {
		return Collections.emptyList();
	}

	/**
	 * Called when a player inputs while hovering over the recipe.
	 * Useful for implementing buttons, hyperlinks, and other interactions to your recipe.
	 *
	 * @param mouseX the X position of the mouse, relative to the recipe.
	 * @param mouseY the Y position of the mouse, relative to the recipe.
	 * @param input  the current input from the player.
	 * @return true if the input was handled, false otherwise
	 * @since 8.3.0
	 *
	 * @deprecated create a {@link IJeiInputHandler} to handle inputs using {@link IRecipeExtrasBuilder#addInputHandler}, then
	 * use {@link IJeiInputHandler#handleInput(double, double, IJeiUserInput)}
	 */
	@Deprecated(since = "16.0.0", forRemoval = true)
	default boolean handleInput(double mouseX, double mouseY, InputConstants.Key input) {
		return false;
	}
}