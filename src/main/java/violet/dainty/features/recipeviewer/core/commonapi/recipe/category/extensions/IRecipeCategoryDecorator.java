package violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IAdvancedRegistration;

import java.util.List;

/**
 * The {@link IRecipeCategoryDecorator} allows further customization of recipe categories.
 * It can be used to draw additional elements or tooltips on recipes, even of other mods.
 * <p>
 * Register it with {@link IAdvancedRegistration#addRecipeCategoryDecorator(RecipeType, IRecipeCategoryDecorator)}.
 *
 * @since 15.1.0
 */
public interface IRecipeCategoryDecorator<T> {
	/**
	 * Draw extras or additional info about the recipe after the {@link IRecipeCategory} that
	 * this decorator is registered to has drawn.
	 * Tooltips are handled by {@link #decorateTooltips(ITooltipBuilder, Object, IRecipeCategory, IRecipeSlotsView, double, double)}.
	 *
	 * @param recipe          the current recipe being drawn.
	 * @param recipeCategory  the recipe category of the recipe.
	 * @param recipeSlotsView a view of the current recipe slots being drawn.
	 * @param guiGraphics     the current {@link GuiGraphics} for rendering.
	 * @param mouseX          the X position of the mouse, relative to the recipe.
	 * @param mouseY          the Y position of the mouse, relative to the recipe.
	 *
	 * @see IDrawable for a simple class for drawing things.
	 * @see IGuiHelper for useful functions.
	 * @see IRecipeSlotsView for information about the ingredients that are currently being drawn.
	 *
	 * @since 15.1.0
	 */
	default void draw(T recipe, IRecipeCategory<T> recipeCategory, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

	}

	/**
	 * Allows modifying of and adding to existing tooltips added by the {@link IRecipeCategory} that
	 * this decorator is registered to.
	 * To avoid removing tooltips from the category itself, make sure to return original list with your
	 * edits and additions.
	 *
	 * @param tooltips        the existing tooltip strings.
	 * @param recipe          the current recipe being drawn.
	 * @param recipeCategory  the recipe category of the recipe.
	 * @param recipeSlotsView a view of the current recipe slots being drawn.
	 * @param mouseX          the X position of the mouse, relative to the recipe.
	 * @param mouseY          the Y position of the mouse, relative to the recipe.
	 * @return tooltip strings. If there is no tooltip at this position, return an empty list.
	 *
	 * @since 15.1.0
	 * @deprecated use {@link #decorateTooltips(ITooltipBuilder, Object, IRecipeCategory, IRecipeSlotsView, double, double)}
	 */
	@Deprecated(since = "19.5.4", forRemoval = true)
	default List<Component> decorateExistingTooltips(List<Component> tooltips, T recipe, IRecipeCategory<T> recipeCategory, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		return tooltips;
	}

	/**
	 * Allows adding to existing tooltips added by the {@link IRecipeCategory} that
	 * this decorator is registered to.
	 *
	 * @param tooltip         the existing tooltip strings.
	 * @param recipe          the current recipe being drawn.
	 * @param recipeCategory  the recipe category of the recipe.
	 * @param recipeSlotsView a view of the current recipe slots being drawn.
	 * @param mouseX          the X position of the mouse, relative to the recipe.
	 * @param mouseY          the Y position of the mouse, relative to the recipe.
	 *
	 * @since 19.5.4
	 */
	default void decorateTooltips(ITooltipBuilder tooltip, T recipe, IRecipeCategory<T> recipeCategory, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

	}
}
