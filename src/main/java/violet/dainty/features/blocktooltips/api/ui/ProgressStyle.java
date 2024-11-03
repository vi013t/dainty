package violet.dainty.features.blocktooltips.api.ui;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public abstract class ProgressStyle {

	@Nullable
	protected IElement overlay;
	protected boolean fitContentX = true;
	protected boolean fitContentY = true;
	protected ScreenDirection direction = ScreenDirection.RIGHT;

	public ProgressStyle color(int color) {
		return color(color, color);
	}

	public abstract ProgressStyle color(int color, int color2);

	public abstract ProgressStyle textColor(int color);

	public ProgressStyle direction(ScreenDirection direction) {
		this.direction = Objects.requireNonNull(direction);
		return this;
	}

	public ScreenDirection direction() {
		return direction;
	}

	public ProgressStyle overlay(IElement overlay) {
		this.overlay = overlay;
		return this;
	}

	public ProgressStyle fitContentX(boolean fitContentX) {
		this.fitContentX = fitContentX;
		return this;
	}

	public boolean fitContentX() {
		return fitContentX;
	}

	public ProgressStyle fitContentY(boolean fitContentY) {
		this.fitContentY = fitContentY;
		return this;
	}

	public boolean fitContentY() {
		return fitContentY;
	}

	public abstract void render(GuiGraphics guiGraphics, float x, float y, float w, float h, float progress, Component text);
}
