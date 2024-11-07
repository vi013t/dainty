package violet.dainty.features.recipeviewer.core.commonapi.gui.widgets;

import java.util.List;

import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;

/**
 * A widget factory that creates a slotted widget instance that handles specific slots.
 *
 * Assign it slots with {@link IRecipeLayoutBuilder#addSlotToWidget(RecipeIngredientRole, ISlottedWidgetFactory)}
 * and then JEI will call {@link #createWidgetForSlots} after all the slots are built.
 *
 * @since 19.7.0
 * @deprecated there are easier ways to create slotted widgets now. Use {@link IRecipeExtrasBuilder#addSlottedWidget}.
 */
@SuppressWarnings({"DeprecatedIsStillUsed", "removal"})
@Deprecated(since = "19.19.3", forRemoval = true)
@FunctionalInterface
public interface ISlottedWidgetFactory<R> {
	/**
	 * Create a widget instance for the given recipe and slots.
	 * This will be called when the slots are built and ready.
	 *
	 * @param recipe the recipe to be used by
	 * @param slots created and assigned to this widget factory with
	 * 			{@link IRecipeLayoutBuilder#addSlotToWidget(RecipeIngredientRole, ISlottedWidgetFactory)}
	 *
	 * @since 19.7.0
	 */
	@Deprecated(since = "19.19.3", forRemoval = true)
	@SuppressWarnings("removal")
	void createWidgetForSlots(IRecipeExtrasBuilder builder, R recipe, List<IRecipeSlotDrawable> slots);
}
