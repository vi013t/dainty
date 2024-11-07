package violet.dainty.features.recipeviewer.core.gui.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnegative;

import com.mojang.blaze3d.systems.RenderSystem;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableNineSliceTexture;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.common.util.MathUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.gui.input.ClickableIngredientInternal;
import violet.dainty.features.recipeviewer.core.gui.input.IClickableIngredientInternal;
import violet.dainty.features.recipeviewer.core.gui.input.IDraggableIngredientInternal;
import violet.dainty.features.recipeviewer.core.gui.input.IRecipeFocusSource;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IElement;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IngredientElement;

/**
 * The area drawn on left side of the {@link RecipesGui} that shows which items can craft the current recipe category.
 */
public class RecipeCatalysts implements IRecipeFocusSource {
	private static final int ingredientSize = 16;
	private static final int ingredientBorderSize = 1;
	private static final int borderSize = 5;
	private static final int overlapSize = 6;

	private final DrawableNineSliceTexture backgroundTab;

	private final List<IRecipeSlotDrawable> recipeSlots;
	private final DrawableNineSliceTexture slotBackground;
	private final IRecipeManager recipeManager;
	private int left = 0;
	private int top = 0;
	private int width = 0;
	private int height = 0;

	public RecipeCatalysts(IRecipeManager recipeManager) {
		this.recipeManager = recipeManager;
		recipeSlots = new ArrayList<>();
		Textures textures = Internal.getTextures();
		backgroundTab = textures.getCatalystTab();
		slotBackground = textures.getRecipeCatalystSlotBackground();
	}

	public boolean isEmpty() {
		return this.recipeSlots.isEmpty();
	}

	@Nonnegative
	public int getWidth() {
		return Math.max(0, width - overlapSize);
	}

	public void updateLayout(List<ITypedIngredient<?>> ingredients, ImmutableRect2i recipeArea, ImmutableRect2i optionButtonsArea) {
		this.recipeSlots.clear();

		if (!ingredients.isEmpty()) {
			int availableHeight = recipeArea.getHeight() - optionButtonsArea.getHeight() - 8;
			int borderHeight = (2 * borderSize) + (2 * ingredientBorderSize);
			int maxIngredientsPerColumn = (availableHeight - borderHeight) / ingredientSize;
			int columnCount = MathUtil.divideCeil(ingredients.size(), maxIngredientsPerColumn);
			maxIngredientsPerColumn = MathUtil.divideCeil(ingredients.size(), columnCount);

			width = (2 * ingredientBorderSize) + (borderSize * 2) + (columnCount * ingredientSize);
			height = (2 * ingredientBorderSize) + (borderSize * 2) + (maxIngredientsPerColumn * ingredientSize);
			top = recipeArea.getY();
			left = recipeArea.getX() - width + overlapSize; // overlaps the recipe gui slightly

			for (int i = 0; i < ingredients.size(); i++) {
				ITypedIngredient<?> ingredientForSlot = ingredients.get(i);
				IRecipeSlotDrawable recipeSlot = createSlot(ingredientForSlot, i, maxIngredientsPerColumn);
				this.recipeSlots.add(recipeSlot);
			}
		}
	}

	private <T> IRecipeSlotDrawable createSlot(ITypedIngredient<T> typedIngredient, int index, int maxIngredientsPerColumn) {
		int column = index / maxIngredientsPerColumn;
		int row = index % maxIngredientsPerColumn;
		IRecipeSlotDrawable recipeSlotDrawable = recipeManager.createRecipeSlotDrawable(
			RecipeIngredientRole.CATALYST,
			List.of(Optional.of(typedIngredient)),
			IntSet.of(0),
			0
		);
		recipeSlotDrawable.setPosition(
			left + borderSize + (column * ingredientSize) + ingredientBorderSize,
			top + borderSize + (row * ingredientSize) + ingredientBorderSize
		);
		return recipeSlotDrawable;
	}

	public Optional<IRecipeSlotDrawable> draw(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		int ingredientCount = recipeSlots.size();
		if (ingredientCount > 0) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

			RenderSystem.disableDepthTest();
			{
				int slotWidth = width - (2 * borderSize);
				int slotHeight = height - (2 * borderSize);
				backgroundTab.draw(guiGraphics, this.left, this.top, width, height);
				slotBackground.draw(guiGraphics, this.left + borderSize, this.top + borderSize, slotWidth, slotHeight);
			}
			RenderSystem.enableDepthTest();

			IRecipeSlotDrawable hovered = null;
			for (IRecipeSlotDrawable recipeSlot : this.recipeSlots) {
				if (recipeSlot.isMouseOver(mouseX, mouseY)) {
					hovered = recipeSlot;
				}
				recipeSlot.draw(guiGraphics);
			}
			return Optional.ofNullable(hovered);
		}
		return Optional.empty();
	}

	private Stream<IRecipeSlotDrawable> getHovered(double mouseX, double mouseY) {
		return this.recipeSlots.stream()
			.filter(recipeSlot -> recipeSlot.isMouseOver(mouseX, mouseY));
	}

	@Override
	public Stream<IClickableIngredientInternal<?>> getIngredientUnderMouse(double mouseX, double mouseY) {
		return getHovered(mouseX, mouseY)
			.map(recipeSlot ->
				recipeSlot.getDisplayedIngredient()
					.map(i -> {
						IElement<?> element = new IngredientElement<>(i);
						return new ClickableIngredientInternal<>(element, recipeSlot::isMouseOver, false, true);
					}))
			.flatMap(Optional::stream);
	}

	@Override
	public Stream<IDraggableIngredientInternal<?>> getDraggableIngredientUnderMouse(double mouseX, double mouseY) {
		return Stream.empty();
	}
}