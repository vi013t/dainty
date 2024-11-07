package violet.dainty.features.recipeviewer.core.library.gui.ingredients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.gui.elements.OffsetDrawable;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformRenderHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.common.util.MathUtil;
import violet.dainty.features.recipeviewer.core.common.util.SafeIngredientUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IIngredientConsumer;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotRichTooltipCallback;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientVisibility;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiRuntime;
import violet.dainty.features.recipeviewer.core.library.gui.recipes.layout.builder.LegacyTooltipCallbackAdapter;
import violet.dainty.features.recipeviewer.core.library.ingredients.DisplayIngredientAcceptor;

public class RecipeSlot implements IRecipeSlotView, IRecipeSlotDrawable {
	private static final int MAX_DISPLAYED_INGREDIENTS = 100;

	private final RecipeIngredientRole role;
	private final ICycler cycler;
	private final List<IRecipeSlotRichTooltipCallback> tooltipCallbacks;
	private final @Nullable RendererOverrides rendererOverrides;
	private final @Nullable OffsetDrawable background;
	private final @Nullable IDrawable overlay;
	private final @Nullable String slotName;
	private ImmutableRect2i rect;

	/**
	 * All ingredients, ignoring focus and visibility
	 * null ingredients represent a "blank" drawn ingredient in the rotation.
	 */
	@Unmodifiable
	private final List<@Nullable ITypedIngredient<?>> allIngredients;

	/**
	 * Displayed ingredients, taking focus and visibility into account.
	 * null ingredients represent a "blank" drawn ingredient in the rotation.
	 */
	@Unmodifiable
	@Nullable
	private List<@Nullable ITypedIngredient<?>> displayIngredients;

	@Nullable
	private DisplayIngredientAcceptor displayOverrides;

	public RecipeSlot(
		RecipeIngredientRole role,
		ImmutableRect2i rect,
		ICycler cycler,
		List<IRecipeSlotRichTooltipCallback> tooltipCallbacks,
		List<@Nullable ITypedIngredient<?>> allIngredients,
		@Nullable List<@Nullable ITypedIngredient<?>> focusedIngredients,
		@Nullable OffsetDrawable background,
		@Nullable IDrawable overlay,
		@Nullable String slotName,
		@Nullable RendererOverrides rendererOverrides
	) {
		this.allIngredients = Collections.unmodifiableList(allIngredients);
		this.background = background;
		this.overlay = overlay;
		this.slotName = slotName;
		this.rendererOverrides = rendererOverrides;
		this.role = role;
		this.rect = rect;
		this.cycler = cycler;
		this.displayIngredients = focusedIngredients;
		this.tooltipCallbacks = tooltipCallbacks;
	}

	@Override
	public Stream<ITypedIngredient<?>> getAllIngredients() {
		return this.allIngredients.stream()
			.filter(Objects::nonNull);
	}

	@Override
	@Unmodifiable
	public List<@Nullable ITypedIngredient<?>> getAllIngredientsList() {
		return this.allIngredients;
	}

	@Override
	public boolean isEmpty() {
		return this.allIngredients.isEmpty() || this.allIngredients.stream().allMatch(Objects::isNull);
	}

	@Override
	public Optional<ITypedIngredient<?>> getDisplayedIngredient() {
		if (this.displayOverrides != null) {
			List<@Nullable ITypedIngredient<?>> overrides = this.displayOverrides.getAllIngredients();
			@Nullable ITypedIngredient<?> cycled = cycler.getCycled(overrides);
			return Optional.ofNullable(cycled);
		}
		if (this.displayIngredients == null) {
			this.displayIngredients = calculateDisplayIngredients(this.allIngredients);
		}
		@Nullable ITypedIngredient<?> cycled = cycler.getCycled(this.displayIngredients);
		return Optional.ofNullable(cycled);
	}

	private static List<@Nullable ITypedIngredient<?>> calculateDisplayIngredients(List<@Nullable ITypedIngredient<?>> allIngredients) {
		if (allIngredients.isEmpty()) {
			return List.of();
		}

		List<@Nullable ITypedIngredient<?>> visibleIngredients = List.of();
		boolean hasInvisibleIngredients = false;

		// hide invisible ingredients if there are any
		// try scanning through all the ingredients without building the list of visible ingredients.
		// if an invisible ingredient is found, start building the list of visible ingredients
		IIngredientVisibility ingredientVisibility = Internal.getJeiRuntime().getJeiHelpers().getIngredientVisibility();
		for (int i = 0; i < allIngredients.size() && visibleIngredients.size() < MAX_DISPLAYED_INGREDIENTS; i++) {
			@Nullable ITypedIngredient<?> ingredient = allIngredients.get(i);
			boolean visible = ingredient == null || ingredientVisibility.isIngredientVisible(ingredient);
			if (visible) {
				if (hasInvisibleIngredients) {
					visibleIngredients.add(ingredient);
				}
			} else if (!hasInvisibleIngredients) {
				hasInvisibleIngredients = true;
				// `i` is the first invisible ingredient, start putting visible ingredients into visibleIngredients
				visibleIngredients = new ArrayList<>(allIngredients.subList(0, i));
			}
		}

		if (!visibleIngredients.isEmpty()) {
			// some ingredients have been successfully hidden, and some are still visible
			return visibleIngredients;
		}

		// either everything is visible or everything is invisible.
		// if everything is invisible, we show them all anyway so that the recipe slot isn't blank
		if (allIngredients.size() < MAX_DISPLAYED_INGREDIENTS) {
			// re-use allIngredients to save some memory
			return allIngredients;
		} else {
			return allIngredients.subList(0, MAX_DISPLAYED_INGREDIENTS);
		}
	}

	@Override
	public Optional<String> getSlotName() {
		return Optional.ofNullable(this.slotName);
	}

	@Override
	public RecipeIngredientRole getRole() {
		return role;
	}

	@Override
	public void drawHighlight(GuiGraphics guiGraphics, int color) {
		int x = this.rect.getX();
		int y = this.rect.getY();
		int width = this.rect.getWidth();
		int height = this.rect.getHeight();

		guiGraphics.fillGradient(
			RenderType.guiOverlay(),
			x,
			y,
			x + width,
			y + height,
			color,
			color,
			0
		);
	}

	private <T> void getTooltip(ITooltipBuilder tooltip, ITypedIngredient<T> typedIngredient) {
		IIngredientManager ingredientManager = Internal.getJeiRuntime().getIngredientManager();

		IIngredientType<T> ingredientType = typedIngredient.getType();
		IIngredientRenderer<T> ingredientRenderer = getIngredientRenderer(ingredientType);
		SafeIngredientUtil.getTooltip(tooltip, ingredientManager, ingredientRenderer, typedIngredient);
		addTagNameTooltip(tooltip, ingredientManager, typedIngredient);
		addIngredientsToTooltip(tooltip, typedIngredient);
		for (IRecipeSlotRichTooltipCallback tooltipCallback : this.tooltipCallbacks) {
			tooltipCallback.onRichTooltip(this, tooltip);
		}
	}

	private <T> List<Component> legacyGetTooltip(ITypedIngredient<T> typedIngredient) {
		IIngredientManager ingredientManager = Internal.getJeiRuntime().getIngredientManager();

		IIngredientType<T> ingredientType = typedIngredient.getType();
		IIngredientRenderer<T> ingredientRenderer = getIngredientRenderer(ingredientType);

		JeiTooltip tooltip = new JeiTooltip();
		SafeIngredientUtil.getTooltip(tooltip, ingredientManager, ingredientRenderer, typedIngredient);
		addTagNameTooltip(tooltip, ingredientManager, typedIngredient);

		for (IRecipeSlotRichTooltipCallback tooltipCallback : this.tooltipCallbacks) {
			tooltipCallback.onRichTooltip(this, tooltip);
		}
		return tooltip.toLegacyToComponents();
	}

	private <T> void addTagNameTooltip(ITooltipBuilder tooltip, IIngredientManager ingredientManager, ITypedIngredient<T> ingredient) {
		IIngredientType<T> ingredientType = ingredient.getType();
		List<T> ingredients = getIngredients(ingredientType).toList();
		if (ingredients.isEmpty()) {
			return;
		}

		IClientConfig clientConfig = Internal.getJeiClientConfigs().getClientConfig();
		if (clientConfig.getHideSingleTagContentTooltipEnabled() && ingredients.size() == 1) {
			return;
		}

		IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(ingredientType);
		ingredientHelper.getTagKeyEquivalent(ingredients)
			.ifPresent(tagKeyEquivalent -> {
				tooltip.add(
					Component.translatable("dainty.tooltip.recipe.tag", "")
						.withStyle(ChatFormatting.GRAY)
				);
				IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
				Component tagName = renderHelper.getName(tagKeyEquivalent);
				tooltip.add(
					tagName.copy().withStyle(ChatFormatting.GRAY)
				);
			});
	}

	private <T> void addIngredientsToTooltip(ITooltipBuilder tooltip, ITypedIngredient<T> displayed) {
		IClientConfig clientConfig = Internal.getJeiClientConfigs().getClientConfig();
		if (clientConfig.isTagContentTooltipEnabled()) {
			IIngredientType<T> type = displayed.getType();

			IJeiRuntime jeiRuntime = Internal.getJeiRuntime();
			IIngredientManager ingredientManager = jeiRuntime.getIngredientManager();
			IIngredientRenderer<T> renderer = ingredientManager.getIngredientRenderer(type);

			List<T> ingredients = getIngredients(type).toList();

			if (ingredients.size() > 1) {
				tooltip.add(new TagContentTooltipComponent<>(renderer, ingredients));
			}
		}
	}

	@SuppressWarnings("removal")
	@Override
	public void addTooltipCallback(violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotTooltipCallback tooltipCallback) {
		this.tooltipCallbacks.add(new LegacyTooltipCallbackAdapter(tooltipCallback));
	}

	private <T> IIngredientRenderer<T> getIngredientRenderer(IIngredientType<T> ingredientType) {
		return Optional.ofNullable(rendererOverrides)
			.flatMap(r -> r.getIngredientRenderer(ingredientType))
			.orElseGet(() -> {
				IIngredientManager ingredientManager = Internal.getJeiRuntime().getIngredientManager();
				return ingredientManager.getIngredientRenderer(ingredientType);
			});
	}

	@Override
	public void draw(GuiGraphics guiGraphics) {
		final int x = this.rect.getX();
		final int y = this.rect.getY();

		if (background != null) {
			background.draw(guiGraphics, x, y);
		}

		RenderSystem.enableBlend();

		getDisplayedIngredient()
			.ifPresent(ingredient -> drawIngredient(guiGraphics, ingredient, x, y));

		if (overlay != null) {
			RenderSystem.enableBlend();

			var poseStack = guiGraphics.pose();
			poseStack.pushPose();
			{
				poseStack.translate(0, 0, 200);
				overlay.draw(guiGraphics, x, y);
			}
			poseStack.popPose();
		}

		RenderSystem.disableBlend();
	}

	private <T> void drawIngredient(GuiGraphics guiGraphics, ITypedIngredient<T> typedIngredient, int xPos, int yPos) {
		IIngredientType<T> ingredientType = typedIngredient.getType();
		IIngredientRenderer<T> ingredientRenderer = getIngredientRenderer(ingredientType);

		SafeIngredientUtil.render(guiGraphics, ingredientRenderer, typedIngredient, xPos, yPos);
	}

	@Override
	public void drawHoverOverlays(GuiGraphics guiGraphics) {
		drawHighlight(guiGraphics, 0x80FFFFFF);
	}

	@Override
	public List<Component> getTooltip() {
		return getDisplayedIngredient()
			.map(this::legacyGetTooltip)
			.orElseGet(List::of);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltipBuilder) {
		getDisplayedIngredient()
			.ifPresent(ingredient -> {
				getTooltip(tooltipBuilder, ingredient);
			});
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return this.rect.contains(mouseX, mouseY);
	}

	@Override
	public void setPosition(int x, int y) {
		this.rect = this.rect.setPosition(x, y);
	}

	@Override
	public void clearDisplayOverrides() {
		this.displayOverrides = null;
	}

	@Override
	public IIngredientConsumer createDisplayOverrides() {
		if (displayOverrides == null) {
			IIngredientManager ingredientManager = Internal.getJeiRuntime().getIngredientManager();
			displayOverrides = new DisplayIngredientAcceptor(ingredientManager);
		}
		return displayOverrides;
	}

	@SuppressWarnings("removal")
	@Override
	public Rect2i getRect() {
		return rect.toMutable();
	}

	@Override
	public Rect2i getAreaIncludingBackground() {
		if (background == null) {
			return rect.toMutable();
		}
		return MathUtil.union(rect, background.getArea()).toMutable();
	}

	@Override
	public String toString() {
		return "RecipeSlot{" +
			"rect=" + rect +
			'}';
	}
}
