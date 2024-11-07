package violet.dainty.features.recipeviewer.core.library.plugins.debug;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;

public class ObnoxiouslyLargeRecipe {
	private static int count = 0;

	private final ResourceLocation recipeId;

	public ObnoxiouslyLargeRecipe() {
		recipeId = ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "number_" + count);
		count++;
	}

	public ResourceLocation getRecipeId() {
		return recipeId;
	}
}
