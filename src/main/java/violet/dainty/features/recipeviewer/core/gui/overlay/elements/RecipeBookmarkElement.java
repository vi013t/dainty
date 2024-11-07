package violet.dainty.features.recipeviewer.core.gui.overlay.elements;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.BookmarkTooltipFeature;
import violet.dainty.features.recipeviewer.core.common.config.IClientConfig;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.transfer.RecipeTransferUtil;
import violet.dainty.features.recipeviewer.core.common.util.SafeIngredientUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.IRecipeLayoutDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IScalableDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IModIdHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusFactory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferError;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiKeyMapping;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiRuntime;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IRecipesGui;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.IBookmark;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.RecipeBookmark;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;
import violet.dainty.features.recipeviewer.core.gui.overlay.IngredientGridTooltipHelper;
import violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks.IngredientsTooltipComponent;
import violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks.PreviewTooltipComponent;
import violet.dainty.features.recipeviewer.core.gui.recipes.RecipeCategoryIconUtil;
import violet.dainty.features.recipeviewer.core.gui.util.FocusUtil;

public class RecipeBookmarkElement<R, I> implements IElement<I> {
	private final RecipeBookmark<R, I> recipeBookmark;
	private final IClientConfig clientConfig;
	private final EnumMap<BookmarkTooltipFeature, TooltipComponent> cache = new EnumMap<>(BookmarkTooltipFeature.class);
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private @Nullable Optional<IRecipeLayoutDrawable<R>> cachedLayoutDrawable;

	public RecipeBookmarkElement(RecipeBookmark<R, I> recipeBookmark) {
		this.recipeBookmark = recipeBookmark;
		this.clientConfig = Internal.getJeiClientConfigs().getClientConfig();
	}

	@Override
	public ITypedIngredient<I> getTypedIngredient() {
		return recipeBookmark.getDisplayIngredient();
	}

	@Override
	public Optional<IBookmark> getBookmark() {
		return Optional.of(recipeBookmark);
	}

	@Override
	public IDrawable createRenderOverlay() {
		IRecipeCategory<R> recipeCategory = recipeBookmark.getRecipeCategory();
		return new RecipeBookmarkIcon(recipeCategory);
	}

	@Override
	public boolean handleClick(UserInput input, IInternalKeyMappings keyBindings) {
		boolean transferOnce = input.is(keyBindings.getTransferRecipeBookmark());
		boolean transferMax = input.is(keyBindings.getMaxTransferRecipeBookmark());
		if (transferOnce || transferMax) {
			Minecraft minecraft = Minecraft.getInstance();
			Screen screen = minecraft.screen;
			Player player = minecraft.player;
			if (player != null && screen instanceof AbstractContainerScreen<?> containerScreen) {
				IRecipeLayoutDrawable<R> recipeLayout = getRecipeLayoutDrawable().orElse(null);
				if (recipeLayout == null) {
					return false;
				}

				IRecipeTransferManager recipeTransferManager = Internal.getJeiRuntime().getRecipeTransferManager();
				AbstractContainerMenu container = containerScreen.getMenu();
				if (input.isSimulate()) {
					IRecipeTransferError recipeTransferError = RecipeTransferUtil.getTransferRecipeError(recipeTransferManager, container, recipeLayout, player).orElse(null);
					return recipeTransferError == null || recipeTransferError.getType().allowsTransfer;
				} else {
					return RecipeTransferUtil.transferRecipe(recipeTransferManager, container, recipeLayout, player, transferMax);
				}
			}
		}
		return false;
	}

	@Override
	public void show(IRecipesGui recipesGui, FocusUtil focusUtil, List<RecipeIngredientRole> roles) {
		// ignore roles, always display the bookmarked recipe if it's clicked

		IRecipeCategory<R> recipeCategory = recipeBookmark.getRecipeCategory();
		R recipe = recipeBookmark.getRecipe();
		ITypedIngredient<?> ingredient = getTypedIngredient();
		List<IFocus<?>> focuses = focusUtil.createFocuses(ingredient, List.of(RecipeIngredientRole.OUTPUT));
		recipesGui.showRecipes(recipeCategory, List.of(recipe), focuses);
	}

	@Override
	public void getTooltip(JeiTooltip tooltip, IngredientGridTooltipHelper tooltipHelper, IIngredientRenderer<I> ingredientRenderer, IIngredientHelper<I> ingredientHelper) {
		ITypedIngredient<I> displayIngredient = recipeBookmark.getDisplayIngredient();
		R recipe = recipeBookmark.getRecipe();

		IRecipeCategory<R> recipeCategory = recipeBookmark.getRecipeCategory();
		tooltip.add(Component.translatable("dainty.tooltip.bookmarks.recipe", recipeCategory.getTitle()));

		addBookmarkTooltipFeaturesIfEnabled(tooltip);

		if (recipeBookmark.isDisplayIsOutput()) {
			IJeiRuntime jeiRuntime = Internal.getJeiRuntime();
			IIngredientManager ingredientManager = jeiRuntime.getIngredientManager();
			IModIdHelper modIdHelper = jeiRuntime.getJeiHelpers().getModIdHelper();

			ResourceLocation recipeName = recipeCategory.getRegistryName(recipe);
			if (recipeName != null) {
				String recipeModId = recipeName.getNamespace();
				ResourceLocation ingredientName = ingredientHelper.getResourceLocation(displayIngredient.getIngredient());
				String ingredientModId = ingredientName.getNamespace();
				if (!recipeModId.equals(ingredientModId)) {
					String modName = modIdHelper.getFormattedModNameForModId(recipeModId);
					MutableComponent recipeBy = Component.translatable("dainty.tooltip.recipe.by", modName);
					tooltip.add(recipeBy.withStyle(ChatFormatting.GRAY));
				}
			}

			tooltip.add(Component.empty());

			SafeIngredientUtil.getTooltip(tooltip, ingredientManager, ingredientRenderer, displayIngredient);
		}
	}

	private void addBookmarkTooltipFeaturesIfEnabled(JeiTooltip tooltip) {
		JeiTooltip transferComponents = createTransferComponents();

		if (clientConfig.getBookmarkTooltipFeatures().isEmpty() && transferComponents.isEmpty()) {
			return;
		}

		if (clientConfig.isHoldShiftToShowBookmarkTooltipFeaturesEnabled()) {
			if (Screen.hasShiftDown()) {
				addBookmarkTooltipFeatures(tooltip);
				tooltip.addAll(transferComponents);
			} else {
				tooltip.addKeyUsageComponent(
					"dainty.tooltip.bookmarks.tooltips.usage",
					Component.keybind("dainty.key.shift")
				);
			}
		} else {
			addBookmarkTooltipFeatures(tooltip);
			tooltip.addAll(transferComponents);
		}
	}

	private void addBookmarkTooltipFeatures(JeiTooltip tooltip) {
		for (BookmarkTooltipFeature feature : clientConfig.getBookmarkTooltipFeatures()) {
			TooltipComponent component = cache.get(feature);
			if (component == null) {
				IRecipeLayoutDrawable<R> recipeLayout = getRecipeLayoutDrawable().orElse(null);
				if (recipeLayout == null) {
					break;
				}

				component = switch (feature) {
					case PREVIEW -> new PreviewTooltipComponent<>(recipeLayout);
					case INGREDIENTS -> new IngredientsTooltipComponent(recipeLayout);
				};
				cache.put(feature, component);
			}
			tooltip.add(component);
		}
	}

	private JeiTooltip createTransferComponents() {
		JeiTooltip results = new JeiTooltip();

		Minecraft minecraft = Minecraft.getInstance();
		Screen screen = minecraft.screen;
		Player player = minecraft.player;
		if (player != null && screen instanceof AbstractContainerScreen<?> containerScreen) {
			IRecipeTransferError recipeTransferError = getRecipeLayoutDrawable()
				.flatMap(recipeLayout -> {
					IJeiRuntime jeiRuntime = Internal.getJeiRuntime();
					IRecipeTransferManager recipeTransferManager = jeiRuntime.getRecipeTransferManager();
					AbstractContainerMenu container = containerScreen.getMenu();
					return RecipeTransferUtil.getTransferRecipeError(recipeTransferManager, container, recipeLayout, player);
				})
				.orElse(null);

			if (recipeTransferError == null || recipeTransferError.getType().allowsTransfer) {
				IInternalKeyMappings keyMappings = Internal.getKeyMappings();
				IJeiKeyMapping transferRecipeBookmark = keyMappings.getTransferRecipeBookmark();
				if (!transferRecipeBookmark.isUnbound()) {
					results.addKeyUsageComponent(
						"dainty.tooltip.bookmarks.tooltips.transfer.usage",
						transferRecipeBookmark
					);
				}

				IJeiKeyMapping maxTransferRecipeBookmark = keyMappings.getMaxTransferRecipeBookmark();
				if (!maxTransferRecipeBookmark.isUnbound()) {
					results.addKeyUsageComponent(
						"dainty.tooltip.bookmarks.tooltips.transfer.max.usage",
						maxTransferRecipeBookmark
					);
				}
			}
		}
		return results;
	}

	private Optional<IRecipeLayoutDrawable<R>> getRecipeLayoutDrawable() {
		//noinspection OptionalAssignedToNull
		if (cachedLayoutDrawable == null) {
			IJeiRuntime jeiRuntime = Internal.getJeiRuntime();
			IRecipeManager recipeManager = jeiRuntime.getRecipeManager();
			IFocusFactory focusFactory = jeiRuntime.getJeiHelpers().getFocusFactory();
			IScalableDrawable recipePreviewBackground = Internal.getTextures().getRecipePreviewBackground();

			cachedLayoutDrawable = recipeManager.createRecipeLayoutDrawable(
				recipeBookmark.getRecipeCategory(),
				recipeBookmark.getRecipe(),
				focusFactory.getEmptyFocusGroup(),
				recipePreviewBackground,
				4
			);
		}
		return cachedLayoutDrawable;
	}

	@Override
	public boolean isVisible() {
		return recipeBookmark.isVisible();
	}

	private static class RecipeBookmarkIcon implements IDrawable {
		private final IDrawable icon;

		public RecipeBookmarkIcon(IRecipeCategory<?> recipeCategory) {
			IJeiRuntime jeiRuntime = Internal.getJeiRuntime();
			IRecipeManager recipeManager = jeiRuntime.getRecipeManager();
			IJeiHelpers jeiHelpers = jeiRuntime.getJeiHelpers();
			IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
			icon = RecipeCategoryIconUtil.create(
				recipeCategory,
				recipeManager,
				guiHelper
			);
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
		public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
			var poseStack = guiGraphics.pose();
			poseStack.pushPose();
			{
				// this z level seems to be the sweet spot so that
				// 2D icons draw above the items, and
				// 3D icons draw still draw under tooltips.
				poseStack.translate(8 + xOffset, 8 + yOffset, 200);
				poseStack.scale(0.5f, 0.5f, 0.5f);
				icon.draw(guiGraphics);
			}
			poseStack.popPose();
		}
	}
}
