package violet.dainty.features.recipeviewer.core.gui.overlay;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.config.IIngredientFilterConfig;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.util.SafeIngredientUtil;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IColorHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.core.search.SearchMode;

public final class IngredientGridTooltipHelper {
	private final IIngredientManager ingredientManager;
	private final IIngredientFilterConfig ingredientFilterConfig;
	private final IClientToggleState toggleState;
	private final IInternalKeyMappings keyBindings;
	private final IColorHelper colorHelper;

	public IngredientGridTooltipHelper(
		IIngredientManager ingredientManager,
		IIngredientFilterConfig ingredientFilterConfig,
		IClientToggleState toggleState,
		IInternalKeyMappings keyBindings,
		IColorHelper colorHelper
	) {
		this.ingredientManager = ingredientManager;
		this.ingredientFilterConfig = ingredientFilterConfig;
		this.toggleState = toggleState;
		this.keyBindings = keyBindings;
		this.colorHelper = colorHelper;
	}

	public <T> void getIngredientTooltip(
		JeiTooltip tooltip,
		ITypedIngredient<T> typedIngredient,
		IIngredientRenderer<T> ingredientRenderer,
		IIngredientHelper<T> ingredientHelper
	) {
		SafeIngredientUtil.getTooltip(tooltip, ingredientManager, ingredientRenderer, typedIngredient);

		if (ingredientFilterConfig.getColorSearchMode() != SearchMode.DISABLED) {
			addColorSearchInfoToTooltip(tooltip, typedIngredient, ingredientHelper);
		}

		if (ingredientFilterConfig.getSearchIngredientAliases()) {
			addIngredientAliasesToTooltip(tooltip, typedIngredient, ingredientManager);
		}

		if (toggleState.isEditModeEnabled()) {
			addEditModeInfoToTooltip(tooltip, keyBindings);
		}
	}

	private <T> void addIngredientAliasesToTooltip(JeiTooltip tooltip, ITypedIngredient<T> typedIngredient, IIngredientManager ingredientManager) {
		Collection<String> aliases = ingredientManager.getIngredientAliases(typedIngredient);
		if (aliases.isEmpty()) {
			return;
		}
		tooltip.add(Component.empty());
		tooltip.add(
			Component.translatable("dainty.tooltip.item.search.aliases")
				.withStyle(ChatFormatting.GRAY)
		);
		for (String alias : aliases) {
			tooltip.add(
				Component.literal("• " + alias)
					.withStyle(ChatFormatting.GRAY)
			);
		}
	}

	private <T> void addColorSearchInfoToTooltip(JeiTooltip tooltip, ITypedIngredient<T> typedIngredient, IIngredientHelper<T> ingredientHelper) {
		Iterable<Integer> colors = ingredientHelper.getColors(typedIngredient.getIngredient());
		String colorNamesString = StreamSupport.stream(colors.spliterator(), false)
			.map(colorHelper::getClosestColorName)
			.collect(Collectors.joining(", "));
		if (!colorNamesString.isEmpty()) {
			Component colorTranslation = Component.translatable("dainty.tooltip.item.colors", colorNamesString)
				.withStyle(ChatFormatting.GRAY);
			tooltip.add(colorTranslation);
		}
	}

	private static void addEditModeInfoToTooltip(JeiTooltip tooltip, IInternalKeyMappings keyBindings) {
		tooltip.add(CommonComponents.EMPTY);
		tooltip.add(
			Component.translatable("gui.dainty.editMode.description")
				.withStyle(ChatFormatting.DARK_GREEN)
		);
		tooltip.addKeyUsageComponent(
			"gui.dainty.editMode.description.hide",
			keyBindings.getToggleHideIngredient()
		);
		tooltip.addKeyUsageComponent(
			"gui.dainty.editMode.description.hide.wild",
			keyBindings.getToggleWildcardHideIngredient()
		);
	}
}
