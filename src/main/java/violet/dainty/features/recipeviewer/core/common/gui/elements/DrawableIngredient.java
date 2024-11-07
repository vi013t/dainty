package violet.dainty.features.recipeviewer.core.common.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.recipeviewer.core.common.util.SafeIngredientUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

public class DrawableIngredient<V> implements IDrawable {
	private final ITypedIngredient<V> typedIngredient;
	private final IIngredientRenderer<V> ingredientRenderer;

	public DrawableIngredient(ITypedIngredient<V> typedIngredient, IIngredientRenderer<V> ingredientRenderer) {
		this.typedIngredient = typedIngredient;
		this.ingredientRenderer = ingredientRenderer;
	}

	@Override
	public int getWidth() {
		return this.ingredientRenderer.getWidth();
	}

	@Override
	public int getHeight() {
		return this.ingredientRenderer.getHeight();
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
		RenderSystem.enableDepthTest();
		SafeIngredientUtil.render(guiGraphics, ingredientRenderer, typedIngredient, xOffset, yOffset);
		RenderSystem.disableDepthTest();
	}
}
