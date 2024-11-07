package violet.dainty.features.recipeviewer.core.gui.recipes;

import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.gui.input.IUserInputHandler;

public abstract class RecipeGuiTab implements IUserInputHandler {
	public static final int TAB_HEIGHT = 24;
	public static final int TAB_WIDTH = 24;

	protected final ImmutableRect2i area;

	public RecipeGuiTab(int x, int y) {
		this.area = new ImmutableRect2i(x, y, TAB_WIDTH, TAB_HEIGHT);
	}

	public boolean isMouseOver(double mouseX, double mouseY) {
		return area.contains(mouseX, mouseY);
	}

	public abstract boolean isSelected(IRecipeCategory<?> selectedCategory);

	public void draw(boolean selected, GuiGraphics guiGraphics, int mouseX, int mouseY) {
		Textures textures = Internal.getTextures();
		IDrawable tab = selected ? textures.getTabSelected() : textures.getTabUnselected();

		tab.draw(guiGraphics, area.x(), area.y());
	}

	public abstract JeiTooltip getTooltip();
}
