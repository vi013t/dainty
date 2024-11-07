package violet.dainty.features.recipeviewer.core.gui.input;

import java.util.stream.Stream;

public interface IRecipeFocusSource {
	Stream<IClickableIngredientInternal<?>> getIngredientUnderMouse(double mouseX, double mouseY);
	Stream<IDraggableIngredientInternal<?>> getDraggableIngredientUnderMouse(double mouseX, double mouseY);
}