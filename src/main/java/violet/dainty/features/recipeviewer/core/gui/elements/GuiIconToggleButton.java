package violet.dainty.features.recipeviewer.core.gui.elements;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableBlank;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

import java.util.Optional;

public abstract class GuiIconToggleButton {
	private final IDrawable offIcon;
	private final IDrawable onIcon;
	protected final GuiIconButton button;
	private ImmutableRect2i area;

	public GuiIconToggleButton(IDrawable offIcon, IDrawable onIcon) {
		this.offIcon = offIcon;
		this.onIcon = onIcon;
		this.button = new GuiIconButton(DrawableBlank.EMPTY, b -> {});
		this.area = ImmutableRect2i.EMPTY;
	}

	public void updateBounds(ImmutableRect2i area) {
		this.button.updateBounds(area);
		this.area = area;
	}

	public void updateBounds(Rect2i area) {
		this.button.updateBounds(area);
		this.area = new ImmutableRect2i(area);
	}

	public void draw(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (this.area.isEmpty()) {
			return;
		}
		boolean iconToggledOn = isIconToggledOn();
		IDrawable icon = iconToggledOn ? this.onIcon : this.offIcon;

		this.button.setForcePressed(iconToggledOn);
		this.button.setIcon(icon);
		this.button.render(guiGraphics, mouseX, mouseY, partialTicks);
	}

	public final boolean isMouseOver(double mouseX, double mouseY) {
		return this.button.visible && this.area.contains(mouseX, mouseY);
	}

	public IUserInputHandler createInputHandler() {
		return new UserInputHandler();
	}

	public void tick() {

	}

	public final void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (isMouseOver(mouseX, mouseY)) {
			JeiTooltip tooltip = new JeiTooltip();
			getTooltips(tooltip);
			tooltip.draw(guiGraphics, mouseX, mouseY);
		}
	}

	protected abstract void getTooltips(JeiTooltip tooltip);

	protected abstract boolean isIconToggledOn();

	protected abstract boolean onMouseClicked(UserInput input);

	public boolean isVisible() {
		return button.visible;
	}

	private class UserInputHandler implements IUserInputHandler {
		private final IUserInputHandler buttonInputHandler;

		public UserInputHandler() {
			this.buttonInputHandler = button.createInputHandler();
		}

		@Override
		public final Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
			return buttonInputHandler.handleUserInput(screen, input, keyBindings)
				.flatMap(handled -> {
					if (onMouseClicked(input)) {
						return Optional.of(this);
					}
					return Optional.empty();
				});
		}
	}
}
