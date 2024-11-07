package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking;

import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.level.block.Blocks;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;

public class BlastingCategory extends AbstractCookingCategory<BlastingRecipe> {
	public BlastingCategory(IGuiHelper guiHelper) {
		super(guiHelper, RecipeTypes.BLASTING, Blocks.BLAST_FURNACE, "gui.dainty.category.blasting", 100);
	}
}
