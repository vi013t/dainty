package violet.dainty.features.recipeviewer.core.library.gui.recipes.layout.builder;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.Fluid;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.elements.OffsetDrawable;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformFluidHelperInternal;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableStatic;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotRichTooltipCallback;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientTypeWithSubtypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.core.util.Pair;
import violet.dainty.features.recipeviewer.core.library.gui.ingredients.ICycler;
import violet.dainty.features.recipeviewer.core.library.gui.ingredients.RecipeSlot;
import violet.dainty.features.recipeviewer.core.library.gui.ingredients.RendererOverrides;
import violet.dainty.features.recipeviewer.core.library.ingredients.DisplayIngredientAcceptor;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RecipeSlotBuilder implements IRecipeSlotBuilder {
	private final DisplayIngredientAcceptor ingredients;
	private final RecipeIngredientRole role;
	private final List<IRecipeSlotRichTooltipCallback> tooltipCallbacks = new ArrayList<>();
	private final int slotIndex;
	private ImmutableRect2i rect;
	private @Nullable RendererOverrides rendererOverrides;
	private @Nullable OffsetDrawable background;
	private @Nullable IDrawable overlay;
	private @Nullable String slotName;
	@SuppressWarnings("removal")
	private @Nullable violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<?> assignedWidgetFactory;

	public RecipeSlotBuilder(IIngredientManager ingredientManager, int slotIndex, RecipeIngredientRole role) {
		this.ingredients = new DisplayIngredientAcceptor(ingredientManager);
		this.rect = new ImmutableRect2i(0, 0, 16, 16);
		this.role = role;
		this.slotIndex = slotIndex;
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
		IGuiHelper guiHelper = Internal.getJeiRuntime().getJeiHelpers().getGuiHelper();
		IDrawableStatic background = guiHelper.getSlotDrawable();
		this.background = new OffsetDrawable(background, -1, -1);
		return this;
	}

	@Override
	public IRecipeSlotBuilder setOutputSlotBackground() {
		IGuiHelper guiHelper = Internal.getJeiRuntime().getJeiHelpers().getGuiHelper();
		IDrawableStatic background = guiHelper.getOutputSlot();
		this.background = new OffsetDrawable(background, -5, -5);
		return this;
	}

	@Override
	public IRecipeSlotBuilder setBackground(IDrawable background, int xOffset, int yOffset) {
		ErrorUtil.checkNotNull(background, "background");

		this.background = new OffsetDrawable(background, xOffset, yOffset);
		return this;
	}

	@Override
	public IRecipeSlotBuilder setOverlay(IDrawable overlay, int xOffset, int yOffset) {
		ErrorUtil.checkNotNull(overlay, "overlay");

		this.overlay = OffsetDrawable.create(overlay, xOffset, yOffset);
		return this;
	}

	@Override
	public IRecipeSlotBuilder setFluidRenderer(long capacity, boolean showCapacity, int width, int height) {
		Preconditions.checkArgument(capacity > 0, "capacity must be > 0");

		IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
		return setFluidRenderer(fluidHelper, capacity, showCapacity, width, height);
	}

	private <T> IRecipeSlotBuilder setFluidRenderer(IPlatformFluidHelperInternal<T> fluidHelper, long capacity, boolean showCapacity, int width, int height) {
		IIngredientRenderer<T> renderer = fluidHelper.createRenderer(capacity, showCapacity, width, height);
		IIngredientTypeWithSubtypes<Fluid, T> type = fluidHelper.getFluidIngredientType();
		addRenderOverride(type, renderer);
		return this;
	}

	@Override
	public <T> IRecipeSlotBuilder setCustomRenderer(
		IIngredientType<T> ingredientType,
		IIngredientRenderer<T> ingredientRenderer
	) {
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(ingredientRenderer, "ingredientRenderer");

		addRenderOverride(ingredientType, ingredientRenderer);
		return this;
	}

	@SuppressWarnings("removal")
	@Override
	public IRecipeSlotBuilder addTooltipCallback(violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotTooltipCallback tooltipCallback) {
		ErrorUtil.checkNotNull(tooltipCallback, "tooltipCallback");

		this.tooltipCallbacks.add(new LegacyTooltipCallbackAdapter(tooltipCallback));
		return this;
	}

	@Override
	public IRecipeSlotBuilder addRichTooltipCallback(IRecipeSlotRichTooltipCallback tooltipCallback) {
		ErrorUtil.checkNotNull(tooltipCallback, "tooltipCallback");

		this.tooltipCallbacks.add(tooltipCallback);
		return this;
	}

	@Override
	public IRecipeSlotBuilder setSlotName(String slotName) {
		ErrorUtil.checkNotNull(slotName, "slotName");

		this.slotName = slotName;
		return this;
	}

	@Override
	public int getWidth() {
		return this.rect.width();
	}

	@Override
	public int getHeight() {
		return this.rect.height();
	}

	@Override
	public IRecipeSlotBuilder setPosition(int xPos, int yPos) {
		this.rect = this.rect.setPosition(xPos, yPos);
		return this;
	}

	@SuppressWarnings("removal")
	public RecipeSlotBuilder assignToWidgetFactory(violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<?> widgetFactory) {
		ErrorUtil.checkNotNull(widgetFactory, "widgetFactory");

		this.assignedWidgetFactory = widgetFactory;
		return this;
	}

	@SuppressWarnings("removal")
	@Nullable
	public violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<?> getAssignedWidget() {
		return assignedWidgetFactory;
	}

	public Pair<Integer, IRecipeSlotDrawable> build(IFocusGroup focusGroup, ICycler cycler) {
		Set<Integer> focusMatches = getMatches(focusGroup);
		return build(focusMatches, cycler);
	}

	public Pair<Integer, IRecipeSlotDrawable> build(Set<Integer> focusMatches, ICycler cycler) {
		List<@Nullable ITypedIngredient<?>> allIngredients = this.ingredients.getAllIngredients();

		List<@Nullable ITypedIngredient<?>> focusedIngredients = null;

		if (!focusMatches.isEmpty()) {
			focusedIngredients = new ArrayList<>();
			for (Integer i : focusMatches) {
				if (i < allIngredients.size()) {
					@Nullable ITypedIngredient<?> ingredient = allIngredients.get(i);
					focusedIngredients.add(ingredient);
				}
			}
		}

		RecipeSlot recipeSlot = new RecipeSlot(
			role,
			rect,
			cycler,
			tooltipCallbacks,
			allIngredients,
			focusedIngredients,
			background,
			overlay,
			slotName,
			rendererOverrides
		);
		return new Pair<>(slotIndex, recipeSlot);
	}

	public IntSet getMatches(IFocusGroup focuses) {
		return this.ingredients.getMatches(focuses, role);
	}

	public DisplayIngredientAcceptor getIngredientAcceptor() {
		return ingredients;
	}

	private <T> void addRenderOverride(
		IIngredientType<T> ingredientType,
		IIngredientRenderer<T> ingredientRenderer
	) {
		if (this.rendererOverrides == null) {
			this.rendererOverrides = new RendererOverrides();
		}
		this.rendererOverrides.addOverride(ingredientType, ingredientRenderer);
		this.rect = new ImmutableRect2i(
			this.rect.getX(),
			this.rect.getY(),
			rendererOverrides.getIngredientWidth(),
			rendererOverrides.getIngredientHeight()
		);
	}
}