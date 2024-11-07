package violet.dainty.features.recipeviewer.core.library.gui.helpers;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableAnimated;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableBlank;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableCombined;
import violet.dainty.features.recipeviewer.core.common.gui.elements.DrawableIngredient;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.common.util.TickTimer;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ITickTimer;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableAnimated;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableStatic;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.ICraftingGridHelper;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeWidget;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IScrollBoxWidget;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.gui.elements.DrawableBuilder;
import violet.dainty.features.recipeviewer.core.library.gui.widgets.AbstractScrollWidget;
import violet.dainty.features.recipeviewer.core.library.gui.widgets.DrawableWidget;
import violet.dainty.features.recipeviewer.core.library.gui.widgets.ScrollBoxRecipeWidget;
import violet.dainty.features.recipeviewer.core.library.gui.widgets.ScrollGridWidgetFactory;

public class GuiHelper implements IGuiHelper {
	private final IIngredientManager ingredientManager;

	public GuiHelper(IIngredientManager ingredientManager) {
		this.ingredientManager = ingredientManager;
	}

	@Override
	public IDrawableBuilder drawableBuilder(ResourceLocation resourceLocation, int u, int v, int width, int height) {
		return new DrawableBuilder(resourceLocation, u, v, width, height);
	}

	@Override
	public IDrawableAnimated createAnimatedDrawable(IDrawableStatic drawable, int ticksPerCycle, IDrawableAnimated.StartDirection startDirection, boolean inverted) {
		ErrorUtil.checkNotNull(drawable, "drawable");
		ErrorUtil.checkNotNull(startDirection, "startDirection");
		return new DrawableAnimated(drawable, ticksPerCycle, startDirection, inverted);
	}

	@Override
	public IDrawableAnimated createAnimatedDrawable(IDrawableStatic drawable, ITickTimer tickTimer, IDrawableAnimated.StartDirection startDirection) {
		ErrorUtil.checkNotNull(drawable, "drawable");
		ErrorUtil.checkNotNull(tickTimer, "tickTimer");
		ErrorUtil.checkNotNull(startDirection, "startDirection");
		return new DrawableAnimated(drawable, tickTimer, startDirection);
	}

	@Override
	public IDrawableStatic getSlotDrawable() {
		Textures textures = Internal.getTextures();
		return textures.getSlot();
	}

	@Override
	public IDrawableStatic getOutputSlot() {
		Textures textures = Internal.getTextures();
		return textures.getOutputSlot();
	}

	@Override
	public IDrawableStatic getRecipeArrow() {
		Textures textures = Internal.getTextures();
		return textures.getRecipeArrow();
	}

	@Override
	public IDrawableStatic getRecipeArrowFilled() {
		Textures textures = Internal.getTextures();
		return textures.getRecipeArrowFilled();
	}

	@Override
	public IDrawableAnimated createAnimatedRecipeArrow(int ticksPerCycle) {
		IDrawableAnimated animatedFill = createAnimatedDrawable(getRecipeArrowFilled(), ticksPerCycle, IDrawableAnimated.StartDirection.LEFT, false);
		return new DrawableCombined(getRecipeArrow(), animatedFill);
	}

	@Override
	public IDrawableStatic getRecipePlusSign() {
		Textures textures = Internal.getTextures();
		return textures.getRecipePlusSign();
	}

	@Override
	public IDrawableStatic getRecipeFlameEmpty() {
		Textures textures = Internal.getTextures();
		return textures.getFlameEmptyIcon();
	}

	@Override
	public IDrawableStatic getRecipeFlameFilled() {
		Textures textures = Internal.getTextures();
		return textures.getFlameIcon();
	}

	@Override
	public IDrawableAnimated createAnimatedRecipeFlame(int ticksPerCycle) {
		IDrawableAnimated animatedFill = createAnimatedDrawable(getRecipeFlameFilled(), ticksPerCycle, IDrawableAnimated.StartDirection.TOP, true);
		return new DrawableCombined(getRecipeFlameEmpty(), animatedFill);
	}

	@Override
	public IRecipeWidget createWidgetFromDrawable(IDrawable drawable, int xPos, int yPos) {
		return new DrawableWidget(drawable, xPos, yPos);
	}

	@Override
	public IDrawableStatic createBlankDrawable(int width, int height) {
		return new DrawableBlank(width, height);
	}

	@Override
	public <V> IDrawable createDrawableIngredient(IIngredientType<V> type, V ingredient) {
		ErrorUtil.checkNotNull(type, "type");
		ErrorUtil.checkNotNull(ingredient, "ingredient");
		IIngredientRenderer<V> ingredientRenderer = ingredientManager.getIngredientRenderer(type);
		ITypedIngredient<V> typedIngredient = ingredientManager.createTypedIngredient(type, ingredient)
			.orElseThrow(() -> {
				String info = ErrorUtil.getIngredientInfo(ingredient, type, ingredientManager);
				return new IllegalArgumentException(String.format("Ingredient is invalid and cannot be used as a drawable ingredient: %s", info));
			});
		return new DrawableIngredient<>(typedIngredient, ingredientRenderer);
	}

	@Override
	public <V> IDrawable createDrawableIngredient(ITypedIngredient<V> ingredient) {
		ErrorUtil.checkNotNull(ingredient, "ingredient");
		IIngredientType<V> type = ingredient.getType();
		IIngredientRenderer<V> ingredientRenderer = ingredientManager.getIngredientRenderer(type);
		return new DrawableIngredient<>(ingredient, ingredientRenderer);
	}

	@Override
	public ICraftingGridHelper createCraftingGridHelper() {
		return CraftingGridHelper.INSTANCE;
	}

	@SuppressWarnings("removal")
	@Override
	public violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IScrollGridWidgetFactory<?> createScrollGridFactory(int columns, int visibleRows) {
		return new ScrollGridWidgetFactory<>(columns, visibleRows);
	}

	@SuppressWarnings("removal")
	@Override
	public IScrollBoxWidget createScrollBoxWidget(IDrawable contents, int visibleHeight, int xPos, int yPos) {
		ScrollBoxRecipeWidget widget = new ScrollBoxRecipeWidget(contents.getWidth() + getScrollBoxScrollbarExtraWidth(), visibleHeight, xPos, yPos);
		widget.setContents(contents);
		return widget;
	}

	@Override
	public IScrollBoxWidget createScrollBoxWidget(int width, int height, int xPos, int yPos) {
		return new ScrollBoxRecipeWidget(width, height, xPos, yPos);
	}

	@SuppressWarnings("removal")
	@Override
	public int getScrollBoxScrollbarExtraWidth() {
		return AbstractScrollWidget.getScrollBoxScrollbarExtraWidth();
	}

	@Override
	public ITickTimer createTickTimer(int ticksPerCycle, int maxValue, boolean countDown) {
		return new TickTimer(ticksPerCycle, maxValue, countDown);
	}
}
