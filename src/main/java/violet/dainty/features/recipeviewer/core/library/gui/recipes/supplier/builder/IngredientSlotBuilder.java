package violet.dainty.features.recipeviewer.core.library.gui.recipes.supplier.builder;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.Fluid;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotRichTooltipCallback;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.HorizontalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.VerticalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.gui.recipes.layout.builder.RecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.library.ingredients.SimpleIngredientAcceptor;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Minimal version of {@link RecipeSlotBuilder} that can only return the ingredients,
 * but doesn't bother building anything for drawing on screen.
 */
public class IngredientSlotBuilder implements IRecipeSlotBuilder {
	private final SimpleIngredientAcceptor ingredients;

	public IngredientSlotBuilder(IIngredientManager ingredientManager) {
		this.ingredients = new SimpleIngredientAcceptor(ingredientManager);
	}

	@Override
	public <I> IRecipeSlotBuilder addIngredients(IIngredientType<I> ingredientType, List<@Nullable I> ingredients) {
		this.ingredients.addIngredients(ingredientType, ingredients);
		return this;
	}

	@Override
	public <I> IRecipeSlotBuilder addIngredient(IIngredientType<I> ingredientType, I ingredient) {
		this.ingredients.addIngredient(ingredientType, ingredient);
		return this;
	}

	@Override
	public IRecipeSlotBuilder addFluidStack(Fluid fluid) {
		this.ingredients.addFluidStack(fluid);
		return this;
	}

	@Override
	public IRecipeSlotBuilder addFluidStack(Fluid fluid, long amount) {
		this.ingredients.addFluidStack(fluid, amount);
		return this;
	}

	@Override
	public IRecipeSlotBuilder addFluidStack(Fluid fluid, long amount, DataComponentPatch componentPatch) {
		this.ingredients.addFluidStack(fluid, amount, componentPatch);
		return this;
	}

	@Override
	public IRecipeSlotBuilder addIngredientsUnsafe(List<?> ingredients) {
		this.ingredients.addIngredientsUnsafe(ingredients);
		return this;
	}

	@Override
	public IRecipeSlotBuilder addTypedIngredients(List<ITypedIngredient<?>> ingredients) {
		this.ingredients.addTypedIngredients(ingredients);
		return this;
	}

	@Override
	public IRecipeSlotBuilder addOptionalTypedIngredients(List<Optional<ITypedIngredient<?>>> ingredients) {
		this.ingredients.addOptionalTypedIngredients(ingredients);
		return this;
	}

	@Override
	public IRecipeSlotBuilder setStandardSlotBackground() {
		return this;
	}

	@Override
	public IRecipeSlotBuilder setOutputSlotBackground() {
		return this;
	}

	@Override
	public IRecipeSlotBuilder setBackground(IDrawable background, int xOffset, int yOffset) {
		return this;
	}

	@Override
	public IRecipeSlotBuilder setOverlay(IDrawable overlay, int xOffset, int yOffset) {
		return this;
	}

	@Override
	public IRecipeSlotBuilder setFluidRenderer(long capacity, boolean showCapacity, int width, int height) {
		return this;
	}

	@Override
	public <T> IRecipeSlotBuilder setCustomRenderer(IIngredientType<T> ingredientType, IIngredientRenderer<T> ingredientRenderer) {
		return this;
	}

	@SuppressWarnings("removal")
	@Override
	public IRecipeSlotBuilder addTooltipCallback(violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotTooltipCallback tooltipCallback) {
		return this;
	}

	@Override
	public IRecipeSlotBuilder addRichTooltipCallback(IRecipeSlotRichTooltipCallback tooltipCallback) {
		return this;
	}

	@Override
	public IRecipeSlotBuilder setSlotName(String slotName) {
		return this;
	}

	@Override
	public int getWidth() {
		return 16;
	}

	@Override
	public int getHeight() {
		return 16;
	}

	@Override
	public IRecipeSlotBuilder setPosition(int xPos, int yPos) {
		return this;
	}

	@Override
	public IRecipeSlotBuilder setPosition(int areaX, int areaY, int areaWidth, int areaHeight, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		return this;
	}

	public List<ITypedIngredient<?>> getAllIngredients() {
		return this.ingredients.getAllIngredients();
	}
}
