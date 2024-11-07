package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking;

import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.Blocks;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;

public class FurnaceSmeltingCategory extends AbstractCookingCategory<SmeltingRecipe> {
	public FurnaceSmeltingCategory(IGuiHelper guiHelper) {
		super(guiHelper, RecipeTypes.SMELTING, Blocks.FURNACE, "gui.dainty.category.smelting", 200);
	}
}
