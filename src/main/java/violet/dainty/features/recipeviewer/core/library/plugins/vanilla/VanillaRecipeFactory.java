package violet.dainty.features.recipeviewer.core.library.plugins.vanilla;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiAnvilRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiBrewingRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiShapedRecipeBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IVanillaRecipeFactory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.anvil.AnvilRecipe;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.brewing.BrewingRecipeUtil;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.brewing.JeiBrewingRecipe;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.JeiShapedRecipeBuilder;

import java.util.List;

public class VanillaRecipeFactory implements IVanillaRecipeFactory {
	private final BrewingRecipeUtil brewingRecipeUtil;

	public VanillaRecipeFactory(IIngredientManager ingredientManager) {
		IIngredientHelper<ItemStack> ingredientHelper = ingredientManager.getIngredientHelper(VanillaTypes.ITEM_STACK);
		this.brewingRecipeUtil = new BrewingRecipeUtil(ingredientHelper);
	}

	@Override
	public IJeiAnvilRecipe createAnvilRecipe(ItemStack leftInput, List<ItemStack> rightInputs, List<ItemStack> outputs, ResourceLocation uid) {
		ErrorUtil.checkNotEmpty(leftInput, "leftInput");
		ErrorUtil.checkNotNull(rightInputs, "rightInputs");
		ErrorUtil.checkNotEmpty(outputs, "outputs");
		ErrorUtil.checkNotNull(uid, "uid");

		return new AnvilRecipe(List.of(leftInput), List.copyOf(rightInputs), List.copyOf(outputs), uid);
	}

	@Override
	public AnvilRecipe createAnvilRecipe(ItemStack leftInput, List<ItemStack> rightInputs, List<ItemStack> outputs) {
		ErrorUtil.checkNotEmpty(leftInput, "leftInput");
		ErrorUtil.checkNotNull(rightInputs, "rightInputs");
		ErrorUtil.checkNotEmpty(outputs, "outputs");

		return new AnvilRecipe(List.of(leftInput), List.copyOf(rightInputs), List.copyOf(outputs), null);
	}

	@Override
	public AnvilRecipe createAnvilRecipe(List<ItemStack> leftInputs, List<ItemStack> rightInputs, List<ItemStack> outputs, ResourceLocation uid) {
		ErrorUtil.checkNotEmpty(leftInputs, "leftInput");
		ErrorUtil.checkNotNull(rightInputs, "rightInputs");
		ErrorUtil.checkNotEmpty(outputs, "outputs");
		ErrorUtil.checkNotNull(uid, "uid");

		return new AnvilRecipe(List.copyOf(leftInputs), List.copyOf(rightInputs), List.copyOf(outputs), uid);
	}

	@Override
	public AnvilRecipe createAnvilRecipe(List<ItemStack> leftInputs, List<ItemStack> rightInputs, List<ItemStack> outputs) {
		ErrorUtil.checkNotEmpty(leftInputs, "leftInput");
		ErrorUtil.checkNotNull(rightInputs, "rightInputs");
		ErrorUtil.checkNotEmpty(outputs, "outputs");

		return new AnvilRecipe(List.copyOf(leftInputs), List.copyOf(rightInputs), List.copyOf(outputs), null);
	}

	@Override
	public IJeiBrewingRecipe createBrewingRecipe(List<ItemStack> ingredients, ItemStack potionInput, ItemStack potionOutput, ResourceLocation uid) {
		ErrorUtil.checkNotEmpty(ingredients, "ingredients");
		ErrorUtil.checkNotEmpty(potionInput, "potionInput");
		ErrorUtil.checkNotEmpty(potionOutput, "potionOutput");
		ErrorUtil.checkNotNull(uid, "uid");

		List<ItemStack> potionInputs = List.of(potionInput);
		return new JeiBrewingRecipe(ingredients, potionInputs, potionOutput, uid, brewingRecipeUtil);
	}

	@Override
	public IJeiBrewingRecipe createBrewingRecipe(List<ItemStack> ingredients, ItemStack potionInput, ItemStack potionOutput) {
		ErrorUtil.checkNotEmpty(ingredients, "ingredients");
		ErrorUtil.checkNotEmpty(potionInput, "potionInput");
		ErrorUtil.checkNotEmpty(potionOutput, "potionOutput");

		List<ItemStack> potionInputs = List.of(potionInput);
		return new JeiBrewingRecipe(ingredients, potionInputs, potionOutput, null, brewingRecipeUtil);
	}

	@Override
	public IJeiBrewingRecipe createBrewingRecipe(List<ItemStack> ingredients, List<ItemStack> potionInputs, ItemStack potionOutput, ResourceLocation uid) {
		ErrorUtil.checkNotEmpty(ingredients, "ingredients");
		ErrorUtil.checkNotEmpty(potionInputs, "potionInputs");
		ErrorUtil.checkNotEmpty(potionOutput, "potionOutput");
		ErrorUtil.checkNotNull(uid, "uid");

		return new JeiBrewingRecipe(ingredients, potionInputs, potionOutput, uid, brewingRecipeUtil);
	}

	@Override
	public IJeiBrewingRecipe createBrewingRecipe(List<ItemStack> ingredients, List<ItemStack> potionInputs, ItemStack potionOutput) {
		ErrorUtil.checkNotEmpty(ingredients, "ingredients");
		ErrorUtil.checkNotEmpty(potionInputs, "potionInputs");
		ErrorUtil.checkNotEmpty(potionOutput, "potionOutput");

		return new JeiBrewingRecipe(ingredients, potionInputs, potionOutput, null, brewingRecipeUtil);
	}

	@Override
	public IJeiShapedRecipeBuilder createShapedRecipeBuilder(CraftingBookCategory category, List<ItemStack> results) {
		return new JeiShapedRecipeBuilder(category, results);
	}
}
