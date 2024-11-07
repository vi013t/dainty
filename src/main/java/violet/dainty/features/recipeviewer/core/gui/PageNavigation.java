package violet.dainty.features.recipeviewer.core.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.common.util.MathUtil;
import violet.dainty.features.recipeviewer.core.gui.elements.GuiIconButton;
import violet.dainty.features.recipeviewer.core.gui.input.IPaged;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;
import violet.dainty.features.recipeviewer.core.gui.input.handlers.CombinedInputHandler;

public class PageNavigation {
	private final IPaged paged;
	private final GuiIconButton nextButton;
	private final GuiIconButton backButton;
	private final boolean hideOnSinglePage;
	private String pageNumDisplayString = "1/1";
	private ImmutableRect2i area = ImmutableRect2i.EMPTY;

	public PageNavigation(IPaged paged, boolean hideOnSinglePage) {
		this.paged = paged;
		Textures textures = Internal.getTextures();
		this.nextButton = new GuiIconButton(textures.getArrowNext(), b -> paged.nextPage());
		this.backButton = new GuiIconButton(textures.getArrowPrevious(), b -> paged.previousPage());
		this.hideOnSinglePage = hideOnSinglePage;
	}

	private boolean isVisible() {
		if (area.isEmpty()) {
			return false;
		}
		return !hideOnSinglePage || this.paged.hasNext() || this.paged.hasPrevious();
	}

	public void updateBounds(ImmutableRect2i area) {
		this.area = area;
		int buttonSize = Math.min(area.getHeight(), area.width() / 2);

		ImmutableRect2i backArea = area.keepLeft(buttonSize);
		this.backButton.updateBounds(backArea);

		ImmutableRect2i nextArea = area.keepRight(buttonSize);
		this.nextButton.updateBounds(nextArea);
	}

	public void updatePageNumber() {
		int pageNum = this.paged.getPageNumber();
		int pageCount = this.paged.getPageCount();
		this.pageNumDisplayString = String.format("%d/%d", pageNum + 1, pageCount);
	}

	public void draw(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (isVisible()) {
			guiGraphics.fill(
				RenderType.gui(),
				backButton.getX() + backButton.getWidth(),
				backButton.getY(),
				nextButton.getX(),
				nextButton.getY() + nextButton.getHeight(),
				0x30000000
			);

			int availableWidth = this.area.width() - backButton.getWidth() - nextButton.getWidth();
			Font font = minecraft.font;
			ImmutableRect2i centerArea = MathUtil.centerTextArea(this.area, font, this.pageNumDisplayString);
			if (centerArea.width() <= availableWidth) {
				guiGraphics.drawString(font, pageNumDisplayString, centerArea.getX(), centerArea.getY(), 0xFFFFFFFF);
			}
			nextButton.render(guiGraphics, mouseX, mouseY, partialTicks);
			backButton.render(guiGraphics, mouseX, mouseY, partialTicks);
		}
	}

	public ImmutableRect2i getNextButtonArea() {
		return nextButton.getArea();
	}

	public ImmutableRect2i getBackButtonArea() {
		return backButton.getArea();
	}

	public IUserInputHandler createInputHandler() {
		return new CombinedInputHandler(
			"PageNavigation",
			this.nextButton.createInputHandler(),
			this.backButton.createInputHandler()
		);
	}

}