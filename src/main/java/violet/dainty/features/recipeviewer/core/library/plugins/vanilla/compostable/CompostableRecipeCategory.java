package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.compostable;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.HorizontalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.VerticalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.AbstractRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiCompostingRecipe;

public class CompostableRecipeCategory extends AbstractRecipeCategory<IJeiCompostingRecipe> {
	public CompostableRecipeCategory(IGuiHelper guiHelper) {
		super(
			RecipeTypes.COMPOSTING,
			Component.translatable("gui.dainty.category.compostable"),
			guiHelper.createDrawableItemLike(Blocks.COMPOSTER),
			120,
			18
		);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IJeiCompostingRecipe recipe, IFocusGroup focuses) {
		builder.addInputSlot(1, 1)
			.setStandardSlotBackground()
			.addItemStacks(recipe.getInputs());
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, IJeiCompostingRecipe recipe, IFocusGroup focuses) {
		float chance = recipe.getChance();
		int chancePercent = (int) Math.floor(chance * 100);
		Component text = Component.translatable("gui.dainty.category.compostable.chance", chancePercent);
		builder.addText(text, getWidth() - 24, getHeight())
			.setPosition(24, 0)
			.setTextAlignment(HorizontalAlignment.CENTER)
			.setTextAlignment(VerticalAlignment.CENTER)
			.setColor(0xFF808080);
	}

	@Override
	public ResourceLocation getRegistryName(IJeiCompostingRecipe recipe) {
		return recipe.getUid();
	}
}
