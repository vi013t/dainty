package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking;

import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.library.util.RecipeUtil;

public class CampfireCookingCategory extends AbstractCookingCategory<CampfireCookingRecipe> {
	public CampfireCookingCategory(IGuiHelper guiHelper) {
		super(guiHelper, RecipeTypes.CAMPFIRE_COOKING, Blocks.CAMPFIRE, "gui.dainty.category.campfire", 400, 82, 44);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CampfireCookingRecipe> recipeHolder, IFocusGroup focuses) {
		CampfireCookingRecipe recipe = recipeHolder.value();
		builder.addInputSlot(1, 1)
			.setStandardSlotBackground()
			.addIngredients(recipe.getIngredients().getFirst());

		builder.addOutputSlot(61, 9)
			.setOutputSlotBackground()
			.addItemStack(RecipeUtil.getResultItem(recipe));
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<CampfireCookingRecipe> recipeHolder, IFocusGroup focuses) {
		CampfireCookingRecipe recipe = recipeHolder.value();
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = regularCookTime;
		}
		builder.addAnimatedRecipeArrow(cookTime)
			.setPosition(26, 7);
		builder.addAnimatedRecipeFlame(300)
			.setPosition(1, 20);

		addCookTime(builder, recipeHolder);
	}
}
