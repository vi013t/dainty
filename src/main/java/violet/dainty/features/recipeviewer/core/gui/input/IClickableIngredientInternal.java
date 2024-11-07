package violet.dainty.features.recipeviewer.core.gui.input;

import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IElement;


public interface IClickableIngredientInternal<T> {
	ITypedIngredient<T> getTypedIngredient();

	IElement<T> getElement();

	boolean isMouseOver(double mouseX, double mouseY);

	/**
	 * Returns an ItemStack if this clickable slot allows players to cheat ingredients from it
	 * (when the server has granted them permission to cheat).
	 *
	 * Returns an empty ItemStack if cheating is not allowed.
	 *
	 * This is generally only active in the JEI ingredient list and bookmark list.
	 */
	ItemStack getCheatItemStack(IIngredientManager ingredientManager);

	/**
	 * Most GUIs shouldn't allow JEI to click to set the focus,
	 * because it would conflict with their normal behavior.
	 *
	 * JEI's recipe GUI has clickable slots that do allow click to focus,
	 * in order to let players navigate recipes.
	 */
	boolean canClickToFocus();
}
