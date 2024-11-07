package violet.dainty.features.recipeviewer.core.commonapi.gui.handlers;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IGuiHandlerRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IModIngredientRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IClickableIngredient;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Allows plugins to change how JEI is displayed next to guis.
 * This is for mods that display next to all GUIs, like JEI does, so they can draw together correctly.
 * For handling modded GUIs, you should use {@link IGuiContainerHandler} instead.
 *
 * Register your implementation with {@link IGuiHandlerRegistration#addGlobalGuiHandler(IGlobalGuiHandler)}.
 *
 * @see IGuiContainerHandler
 */
public interface IGlobalGuiHandler {
	/**
	 * Give JEI information about extra space that your mod takes up.
	 * Used for moving JEI out of the way of extra things like gui buttons.
	 *
	 * @return the space that the gui takes up besides the normal rectangle defined by {@link AbstractContainerScreen}.
	 */
	default Collection<Rect2i> getGuiExtraAreas() {
		return Collections.emptyList();
	}

	/**
	 * Return a clickable ingredient under the mouse that JEI could not normally detect, used for JEI recipe lookups.
	 * <p>
	 * This is useful for guis that don't have normal slots (which is how JEI normally detects items under the mouse).
	 * <p>
	 * This can also be used to let JEI look up liquids in tanks directly, by returning a FluidStack.
	 * Works with any ingredient type that has been registered with {@link IModIngredientRegistration}.
	 *
	 * @param mouseX the current X position of the mouse in screen coordinates.
	 * @param mouseY the current Y position of the mouse in screen coordinates.
	 *
	 * @since 11.5.0
	 */
	default Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse(double mouseX, double mouseY) {
		return Optional.empty();
	}
}
