package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.compostable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformIngredientHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiCompostingRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class CompostingRecipeMaker {
	public static List<IJeiCompostingRecipe> getRecipes(IIngredientManager ingredientManager) {
		Collection<ItemStack> allIngredients = ingredientManager.getAllItemStacks();
		IIngredientHelper<ItemStack> ingredientHelper = ingredientManager.getIngredientHelper(VanillaTypes.ITEM_STACK);
		IPlatformIngredientHelper platformIngredientHelper = Services.PLATFORM.getIngredientHelper();

		return allIngredients.stream()
			.<IJeiCompostingRecipe>mapMulti((itemStack, consumer) -> {
				float compostValue = platformIngredientHelper.getCompostValue(itemStack);
				if (compostValue > 0) {
					ResourceLocation resourceLocation = ingredientHelper.getResourceLocation(itemStack);
					String ingredientUidPath = resourceLocation.getPath();
					ResourceLocation recipeUid = ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, ingredientUidPath);
					CompostingRecipe recipe = new CompostingRecipe(itemStack, compostValue, recipeUid);
					consumer.accept(recipe);
				}
			})
			.sorted(Comparator.comparingDouble(IJeiCompostingRecipe::getChance))
			.toList();
	}
}
