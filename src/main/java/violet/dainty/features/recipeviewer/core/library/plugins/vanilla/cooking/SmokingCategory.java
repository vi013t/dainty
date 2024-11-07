package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking;

import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.block.Blocks;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;

public class SmokingCategory extends AbstractCookingCategory<SmokingRecipe> {
	public SmokingCategory(IGuiHelper guiHelper) {
		super(guiHelper, RecipeTypes.SMOKING, Blocks.SMOKER, "gui.dainty.category.smoking", 100);
	}
}
