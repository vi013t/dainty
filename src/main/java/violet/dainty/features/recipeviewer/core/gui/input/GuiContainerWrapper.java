package violet.dainty.features.recipeviewer.core.gui.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IClickableIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IElement;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IngredientElement;

import java.util.Optional;
import java.util.stream.Stream;

public class GuiContainerWrapper implements IRecipeFocusSource {
	private final IScreenHelper screenHelper;

	public GuiContainerWrapper(IScreenHelper screenHelper) {
		this.screenHelper = screenHelper;
	}

	@Override
	public Stream<IClickableIngredientInternal<?>> getIngredientUnderMouse(double mouseX, double mouseY) {
		Screen guiScreen = Minecraft.getInstance().screen;
		if (guiScreen == null) {
			return Stream.empty();
		}
		return screenHelper.getClickableIngredientUnderMouse(guiScreen, mouseX, mouseY)
			.flatMap(clickableSlot -> {
				return createTypedIngredient(clickableSlot)
					.map(i -> {
						ImmutableRect2i area = new ImmutableRect2i(clickableSlot.getArea());
						IElement<?> element = new IngredientElement<>(i);
						return new ClickableIngredientInternal<>(element, area::contains, false, false);
					})
					.stream();
			});
	}

	private <T> Optional<ITypedIngredient<T>> createTypedIngredient(IClickableIngredient<T> clickableIngredient) {
		IIngredientManager ingredientManager = Internal.getJeiRuntime().getIngredientManager();
		IIngredientType<T> ingredientType = clickableIngredient.getIngredientType();
		T ingredient = clickableIngredient.getIngredient();
		return ingredientManager.createTypedIngredient(ingredientType, ingredient);
	}

	@Override
	public Stream<IDraggableIngredientInternal<?>> getDraggableIngredientUnderMouse(double mouseX, double mouseY) {
		return Stream.empty();
	}
}
