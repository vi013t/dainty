package violet.dainty.features.recipeviewer.core.library.plugins.debug;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.ISimpleRecipeManagerPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IVanillaRecipeFactory;

import java.util.List;

public class DebugSimpleRecipeManagerPlugin implements ISimpleRecipeManagerPlugin<RecipeHolder<CraftingRecipe>> {
	private final IVanillaRecipeFactory vanillaRecipeFactory;

	public DebugSimpleRecipeManagerPlugin(IJeiHelpers jeiHelpers) {
		this.vanillaRecipeFactory = jeiHelpers.getVanillaRecipeFactory();
	}

	@Override
	public boolean isHandledInput(ITypedIngredient<?> input) {
		ItemStack itemStack = input.getItemStack().orElse(ItemStack.EMPTY);
		return itemStack.is(Items.LIGHT);
	}

	@Override
	public boolean isHandledOutput(ITypedIngredient<?> output) {
		ItemStack itemStack = output.getItemStack().orElse(ItemStack.EMPTY);
		return itemStack.is(Items.DARK_PRISMARINE_SLAB);
	}

	@Override
	public List<RecipeHolder<CraftingRecipe>> getRecipesForInput(ITypedIngredient<?> input) {
		return List.of(generateRecipe());
	}

	@Override
	public List<RecipeHolder<CraftingRecipe>> getRecipesForOutput(ITypedIngredient<?> output) {
		return List.of(generateRecipe());
	}

	@Override
	public List<RecipeHolder<CraftingRecipe>> getAllRecipes() {
		return List.of(generateRecipe());
	}

	private RecipeHolder<CraftingRecipe> generateRecipe() {
		CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, List.of(new ItemStack(Items.DARK_PRISMARINE_SLAB)))
			.pattern("lll")
			.pattern("lll")
			.pattern("lll")
			.define('l', Ingredient.of(Items.LIGHT))
			.build();
		ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "debug_simple_recipe");
		return new RecipeHolder<>(resourceLocation, recipe);
	}
}
