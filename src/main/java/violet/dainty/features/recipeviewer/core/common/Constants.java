package violet.dainty.features.recipeviewer.core.common;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;

public final class Constants {
	public static final RecipeType<?> UNIVERSAL_RECIPE_TRANSFER_TYPE = RecipeType.create(ModIds.JEI_ID, "universal_recipe_transfer_handler", Object.class);
	public static final ResourceLocation LOCATION_JEI_GUI_TEXTURE_ATLAS = ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "textures/atlas/gui.png");

	private Constants() {

	}
}
