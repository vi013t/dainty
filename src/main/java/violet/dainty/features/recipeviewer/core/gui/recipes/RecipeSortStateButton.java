package violet.dainty.features.recipeviewer.core.gui.recipes;

import java.util.Set;

import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.config.IJeiClientConfigs;
import violet.dainty.features.recipeviewer.core.common.config.RecipeSorterStage;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.gui.elements.GuiIconToggleButton;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

public class RecipeSortStateButton extends GuiIconToggleButton {
	private final RecipeSorterStage recipeSorterStage;
	private final Component disabledTooltip;
	private final Component enabledTooltip;
	private final Runnable onValueChanged;
	private boolean toggledOn;

	public RecipeSortStateButton(
		RecipeSorterStage recipeSorterStage,
		IDrawable offIcon,
		IDrawable onIcon,
		Component disabledTooltip,
		Component enabledTooltip,
		Runnable onValueChanged
	) {
		super(offIcon, onIcon);
		this.recipeSorterStage = recipeSorterStage;
		this.disabledTooltip = disabledTooltip;
		this.enabledTooltip = enabledTooltip;
		this.onValueChanged = onValueChanged;

		tick();
	}

	@Override
	protected void getTooltips(JeiTooltip tooltip) {
		if (toggledOn) {
			tooltip.add(enabledTooltip);
		} else {
			tooltip.add(disabledTooltip);
		}
	}

	@Override
	public void tick() {
		IJeiClientConfigs jeiClientConfigs = Internal.getJeiClientConfigs();
		IClientConfig clientConfig = jeiClientConfigs.getClientConfig();
		Set<RecipeSorterStage> recipeSorterStages = clientConfig.getRecipeSorterStages();
		boolean toggledOn = recipeSorterStages.contains(recipeSorterStage);
		if (toggledOn != this.toggledOn) {
			this.toggledOn = toggledOn;
			this.onValueChanged.run();
		}
	}

	@Override
	protected boolean isIconToggledOn() {
		return toggledOn;
	}

	@Override
	protected boolean onMouseClicked(UserInput input) {
		if (!input.isSimulate()) {
			IJeiClientConfigs jeiClientConfigs = Internal.getJeiClientConfigs();
			IClientConfig clientConfig = jeiClientConfigs.getClientConfig();
			if (this.toggledOn) {
				clientConfig.disableRecipeSorterStage(recipeSorterStage);
				this.toggledOn = false;
			} else {
				clientConfig.enableRecipeSorterStage(recipeSorterStage);
				this.toggledOn = true;
			}
			this.onValueChanged.run();
		}
		return true;
	}
}
