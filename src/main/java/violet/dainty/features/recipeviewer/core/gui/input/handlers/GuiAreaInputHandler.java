package violet.dainty.features.recipeviewer.core.gui.input.handlers;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformScreenHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusFactory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IRecipesGui;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IScreenHelper;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.Optional;

public class GuiAreaInputHandler implements IUserInputHandler {
	private final IFocusFactory focusFactory;
	private final IScreenHelper screenHelper;
	private final IRecipesGui recipesGui;

	public GuiAreaInputHandler(IScreenHelper screenHelper, IRecipesGui recipesGui, IFocusFactory focusFactory) {
		this.focusFactory = focusFactory;
		this.screenHelper = screenHelper;
		this.recipesGui = recipesGui;
	}

	@Override
	public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
		if (input.is(keyBindings.getLeftClick())) {
			if (screen instanceof AbstractContainerScreen<?> guiContainer) {
				IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
				final int guiLeft = screenHelper.getGuiLeft(guiContainer);
				final int guiTop = screenHelper.getGuiTop(guiContainer);
				final double guiMouseX = input.getMouseX() - guiLeft;
				final double guiMouseY = input.getMouseY() - guiTop;
				return this.screenHelper.getGuiClickableArea(guiContainer, guiMouseX, guiMouseY)
					.findFirst()
					.map(clickableArea -> {
						if (!input.isSimulate()) {
							clickableArea.onClick(focusFactory, recipesGui);
						}

						ImmutableRect2i screenArea = new ImmutableRect2i(clickableArea.getArea())
							.addOffset(guiLeft, guiTop);
						return new SameElementInputHandler(this, screenArea::contains);
					});
			}
		}

		return Optional.empty();
	}

}
