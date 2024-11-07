package violet.dainty.features.recipeviewer.core.gui.input.handlers;

import java.util.Optional;
import java.util.Set;

import net.minecraft.client.gui.screens.Screen;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IEditModeConfig;
import violet.dainty.features.recipeviewer.core.gui.input.CombinedRecipeFocusSource;
import violet.dainty.features.recipeviewer.core.gui.input.IClickableIngredientInternal;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

public class EditInputHandler implements IUserInputHandler {
	private final CombinedRecipeFocusSource focusSource;
	private final IClientToggleState toggleState;
	private final IEditModeConfig editModeConfig;

	public EditInputHandler(CombinedRecipeFocusSource focusSource, IClientToggleState toggleState, IEditModeConfig editModeConfig) {
		this.focusSource = focusSource;
		this.toggleState = toggleState;
		this.editModeConfig = editModeConfig;
	}

	@Override
	public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
		if (!toggleState.isEditModeEnabled()) {
			return Optional.empty();
		}

		if (input.is(keyBindings.getToggleHideIngredient())) {
			return handle(input, keyBindings, IEditModeConfig.HideMode.SINGLE);
		}

		if (input.is(keyBindings.getToggleWildcardHideIngredient())) {
			return handle(input, keyBindings, IEditModeConfig.HideMode.WILDCARD);
		}

		return Optional.empty();
	}

	private Optional<IUserInputHandler> handle(UserInput input, IInternalKeyMappings keyBindings, IEditModeConfig.HideMode hideMode) {
		return focusSource.getIngredientUnderMouse(input, keyBindings)
			.findFirst()
			.map(clicked -> {
				if (!input.isSimulate()) {
					execute(clicked, hideMode);
				}
				return new SameElementInputHandler(this, clicked::isMouseOver);
			});
	}

	private <V> void execute(IClickableIngredientInternal<V> clicked, IEditModeConfig.HideMode hideMode) {
		ITypedIngredient<?> typedIngredient = clicked.getTypedIngredient();
		Set<IEditModeConfig.HideMode> hideModes = editModeConfig.getIngredientHiddenUsingConfigFile(typedIngredient);
		if (hideModes.contains(hideMode)) {
			editModeConfig.showIngredientUsingConfigFile(typedIngredient, hideMode);
		} else {
			editModeConfig.hideIngredientUsingConfigFile(typedIngredient, hideMode);
		}
	}
}
