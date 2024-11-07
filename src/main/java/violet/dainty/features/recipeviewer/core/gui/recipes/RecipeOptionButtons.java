package violet.dainty.features.recipeviewer.core.gui.recipes;

import java.util.List;

import javax.annotation.Nonnegative;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.RecipeSorterStage;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableNineSliceTexture;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.gui.elements.GuiIconToggleButton;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.CombinedInputHandler;

public class RecipeOptionButtons {
	private static final int buttonSize = 16;
	private static final int buttonBorderSize = 1;
	private static final int borderSize = 5;
	private static final int overlapSize = 6;

	private final List<GuiIconToggleButton> buttons;

	private final DrawableNineSliceTexture backgroundTab;
	private ImmutableRect2i area;

	public RecipeOptionButtons(Runnable onValueChanged) {
		Textures textures = Internal.getTextures();
		GuiIconToggleButton bookmarksFirstButton = new RecipeSortStateButton(
			RecipeSorterStage.BOOKMARKED,
			textures.getBookmarksFirst(),
			textures.getBookmarksFirst(),
			Component.translatable("dainty.tooltip.recipe.sort.bookmarks.first.disabled"),
			Component.translatable("dainty.tooltip.recipe.sort.bookmarks.first.enabled"),
			onValueChanged
		);
		GuiIconToggleButton craftableFirstButton = new RecipeSortStateButton(
			RecipeSorterStage.CRAFTABLE,
			textures.getCraftableFirst(),
			textures.getCraftableFirst(),
			Component.translatable("dainty.tooltip.recipe.sort.craftable.first.disabled"),
			Component.translatable("dainty.tooltip.recipe.sort.craftable.first.enabled"),
			onValueChanged
		);

		buttons = List.of(bookmarksFirstButton, craftableFirstButton);
		backgroundTab = textures.getRecipeOptionsTab();
	}

	public void tick() {
		for (GuiIconToggleButton button : buttons) {
			button.tick();
		}
	}

	public void updateLayout(ImmutableRect2i recipeArea) {
		int width = (2 * buttonBorderSize) + (borderSize * 2) + buttonSize;
		int height = (2 * buttonBorderSize) + (borderSize * 2) + (buttons.size() * buttonSize);
		int y = recipeArea.getY() + recipeArea.getHeight() - height;
		int x = recipeArea.getX() - width + overlapSize; // overlaps the recipe gui slightly

		this.area = new ImmutableRect2i(
			x,
			y,
			width,
			height
		);

		final int buttonX = x + borderSize + buttonBorderSize;
		for (int i = 0; i < buttons.size(); i++) {
			GuiIconToggleButton button = buttons.get(i);
			int buttonY = y + borderSize + (i * buttonSize) + buttonBorderSize;
			button.updateBounds(new ImmutableRect2i(buttonX, buttonY, buttonSize, buttonSize));
		}
	}

	public ImmutableRect2i getArea() {
		return area;
	}

	public void draw(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		RenderSystem.disableDepthTest();
		{
			backgroundTab.draw(guiGraphics, this.area);
		}
		RenderSystem.enableDepthTest();

		for (GuiIconToggleButton button : buttons) {
			button.draw(guiGraphics, mouseX, mouseY, partialTicks);
		}
	}

	@Nonnegative
	public int getWidth() {
		return Math.max(0, area.getWidth() - overlapSize);
	}

	public IUserInputHandler createInputHandler() {
		List<IUserInputHandler> handlers = buttons.stream()
			.map(GuiIconToggleButton::createInputHandler)
			.toList();
		return new CombinedInputHandler("RecipeOptionButtons", handlers);
	}

	public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		for (GuiIconToggleButton button : buttons) {
			button.drawTooltips(guiGraphics, mouseX, mouseY);
		}
	}
}
