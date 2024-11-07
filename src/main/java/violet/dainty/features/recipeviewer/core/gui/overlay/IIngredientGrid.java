package violet.dainty.features.recipeviewer.core.gui.overlay;

import violet.dainty.features.recipeviewer.core.gui.input.IRecipeFocusSource;

public interface IIngredientGrid extends IRecipeFocusSource {
	boolean isMouseOver(double mouseX, double mouseY);
}
