package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking.fuel;

import java.text.NumberFormat;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.HorizontalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.VerticalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.AbstractRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiFuelingRecipe;

public class FurnaceFuelCategory extends AbstractRecipeCategory<IJeiFuelingRecipe> {
	public FurnaceFuelCategory(Textures textures) {
		super(
			RecipeTypes.FUELING,
			Component.translatable("gui.dainty.category.fuel"),
			textures.getFlameIcon(),
			getMaxWidth(),
			34
		);
	}

	private static int getMaxWidth() {
		// width of the recipe depends on the text, which is different in each language
		Minecraft minecraft = Minecraft.getInstance();
		Font fontRenderer = minecraft.font;
		Component maxSmeltCountText = createSmeltCountText(10000000 * 200);
		int maxStringWidth = fontRenderer.width(maxSmeltCountText.getString());
		int textPadding = 20;
		return 18 + textPadding + maxStringWidth;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IJeiFuelingRecipe recipe, IFocusGroup focuses) {
		builder.addInputSlot(1, 17)
			.setStandardSlotBackground()
			.addItemStacks(recipe.getInputs());
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, IJeiFuelingRecipe recipe, IFocusGroup focuses) {
		int burnTime = recipe.getBurnTime();
		builder.addAnimatedRecipeFlame(burnTime)
			.setPosition(1, 0);

		Component smeltCountText = createSmeltCountText(burnTime);
		builder.addText(smeltCountText, getWidth() - 20, getHeight())
			.setPosition(20, 0)
			.setTextAlignment(HorizontalAlignment.CENTER)
			.setTextAlignment(VerticalAlignment.CENTER)
			.setColor(0xFF808080);
	}

	public static Component createSmeltCountText(int burnTime) {
		if (burnTime == 200) {
			return Component.translatable("gui.dainty.category.fuel.smeltCount.single");
		} else {
			NumberFormat numberInstance = NumberFormat.getNumberInstance();
			numberInstance.setMaximumFractionDigits(2);
			String smeltCount = numberInstance.format(burnTime / 200f);
			return Component.translatable("gui.dainty.category.fuel.smeltCount", smeltCount);
		}
	}

	@Override
	public @Nullable ResourceLocation getRegistryName(IJeiFuelingRecipe recipe) {
		return null;
	}
}
