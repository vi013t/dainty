package violet.dainty.features.stonefurnace;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import violet.dainty.registries.DaintyBlocks;
import violet.dainty.registries.DaintyRecipes;

public class StoneFurnaceRecipe extends AbstractCookingRecipe {

    public StoneFurnaceRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(DaintyRecipes.STONE_FURNACE.get(), group, category, ingredient, result, experience, cookingTime);
    }

	@Override
	public RecipeSerializer<?> getSerializer() {
		return DaintyRecipes.STONE_FURNACE_RECIPE_SERIALIZER.get();
	}

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(DaintyBlocks.STONE_FURNACE.get());
    }
	
}
