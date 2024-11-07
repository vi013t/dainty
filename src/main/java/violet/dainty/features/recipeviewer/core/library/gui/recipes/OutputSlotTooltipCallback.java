package violet.dainty.features.recipeviewer.core.library.gui.recipes;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotRichTooltipCallback;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IModIdHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

public class OutputSlotTooltipCallback implements IRecipeSlotRichTooltipCallback {
	private static final Logger LOGGER = LogManager.getLogger();

	private final ResourceLocation recipeName;
	private final boolean recipeFromSameModAsCategory;

	public OutputSlotTooltipCallback(ResourceLocation recipeName, RecipeType<?> recipeType) {
		this.recipeName = recipeName;
		this.recipeFromSameModAsCategory = recipeName.getNamespace().equals(recipeType.getUid().getNamespace());
	}

	@Override
	public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
		if (recipeSlotView.getRole() != RecipeIngredientRole.OUTPUT) {
			return;
		}
		Optional<ITypedIngredient<?>> displayedIngredient = recipeSlotView.getDisplayedIngredient();
		if (displayedIngredient.isEmpty()) {
			return;
		}

		addRecipeBy(tooltip, displayedIngredient.get());

		Minecraft minecraft = Minecraft.getInstance();
		boolean showAdvanced = minecraft.options.advancedItemTooltips || Screen.hasShiftDown();
		if (showAdvanced) {
			MutableComponent recipeId = Component.translatable("dainty.tooltip.recipe.id", Component.literal(recipeName.toString()));
			tooltip.add(recipeId.withStyle(ChatFormatting.DARK_GRAY));
		}
	}

	private void addRecipeBy(ITooltipBuilder tooltip, ITypedIngredient<?> displayedIngredient) {
		if (recipeFromSameModAsCategory) {
			return;
		}
		IModIdHelper modIdHelper = Internal.getJeiRuntime().getJeiHelpers().getModIdHelper();
		if (!modIdHelper.isDisplayingModNameEnabled()) {
			return;
		}
		String ingredientModId = getDisplayModId(displayedIngredient);
		if (ingredientModId == null) {
			return;
		}
		String recipeModId = recipeName.getNamespace();
		if (recipeModId.equals(ingredientModId)) {
			return;
		}
		String modName = modIdHelper.getFormattedModNameForModId(recipeModId);
		MutableComponent recipeBy = Component.translatable("dainty.tooltip.recipe.by", modName);
		tooltip.add(recipeBy.withStyle(ChatFormatting.GRAY));
	}

	private <T> @Nullable String getDisplayModId(ITypedIngredient<T> typedIngredient) {
		IIngredientManager ingredientManager = Internal.getJeiRuntime().getIngredientManager();

		IIngredientType<T> type = typedIngredient.getType();
		T ingredient = typedIngredient.getIngredient();
		IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(type);
		try {
			return ingredientHelper.getDisplayModId(ingredient);
		} catch (RuntimeException e) {
			String ingredientInfo = ErrorUtil.getIngredientInfo(ingredient, type, ingredientManager);
			LOGGER.error("Caught exception from ingredient without a resource location: {}", ingredientInfo, e);
			return null;
		}
	}
}
