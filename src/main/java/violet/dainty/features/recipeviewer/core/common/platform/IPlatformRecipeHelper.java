package violet.dainty.features.recipeviewer.core.common.platform;

import java.util.List;

import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiBrewingRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IVanillaRecipeFactory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

public interface IPlatformRecipeHelper {
	Ingredient getBase(SmithingRecipe recipe);
	Ingredient getAddition(SmithingRecipe recipe);
	Ingredient getTemplate(SmithingRecipe recipe);

	List<IJeiBrewingRecipe> getBrewingRecipes(IIngredientManager ingredientManager, IVanillaRecipeFactory vanillaRecipeFactory, PotionBrewing potionBrewing);
}
