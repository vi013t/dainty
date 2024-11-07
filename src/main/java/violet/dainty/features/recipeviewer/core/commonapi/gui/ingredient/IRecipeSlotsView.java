package violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient;

import org.jetbrains.annotations.Unmodifiable;

import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents all the drawn ingredients in slots that are part of a recipe.
 * Useful for implementing {@link IRecipeTransferHandler} and some other advanced cases.
 *
 * This view is meant as a source of information for recipe transfer, drawing, and tooltips.
 *
 * @since 9.3.0
 */
public interface IRecipeSlotsView {
	/**
	 * Get all slots for a recipe.
	 *
	 * @since 9.3.0
	 */
	@Unmodifiable
	List<IRecipeSlotView> getSlotViews();

	/**
	 * Get the list of slots for the given {@link RecipeIngredientRole} for a recipe.
	 *
	 * @since 9.3.0
	 */
	default List<IRecipeSlotView> getSlotViews(RecipeIngredientRole role) {
		List<IRecipeSlotView> list = new ArrayList<>();
		for (IRecipeSlotView slotView : getSlotViews()) {
			if (slotView.getRole() == role) {
				list.add(slotView);
			}
		}
		return list;
	}

	/**
	 * Get a recipe slot by its name set with {@link IRecipeSlotBuilder#setSlotName(String)}.
	 *
	 * @since 9.3.0
	 */
	default Optional<IRecipeSlotView> findSlotByName(String slotName) {
		return getSlotViews().stream()
			.filter(slot ->
				slot.getSlotName()
					.map(slotName::equals)
					.orElse(false)
			)
			.findFirst();
	}
}
