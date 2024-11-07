package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.replacers;

import java.util.List;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import violet.dainty.features.recipeviewer.core.common.util.RegistryUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IVanillaRecipeFactory;

public final class TippedArrowRecipeMaker {

	public static List<RecipeHolder<CraftingRecipe>> createRecipes(IJeiHelpers jeiHelpers) {
		IVanillaRecipeFactory vanillaRecipeFactory = jeiHelpers.getVanillaRecipeFactory();

		String group = "dainty.tipped.arrow";
		ItemStack arrowStack = new ItemStack(Items.ARROW);
		Ingredient arrowIngredient = Ingredient.of(arrowStack);

		Registry<Potion> potionRegistry = RegistryUtil.getRegistry(Registries.POTION);
		return potionRegistry.holders()
			.map(potion -> {
				ItemStack input = PotionContents.createItemStack(Items.LINGERING_POTION, potion);
				ItemStack output = PotionContents.createItemStack(Items.TIPPED_ARROW, potion);
				output.setCount(8);

				Ingredient potionIngredient = Ingredient.of(input);
				ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ModIds.MINECRAFT_ID, "dainty.tipped.arrow." + output.getDescriptionId());
				CraftingRecipe recipe = vanillaRecipeFactory.createShapedRecipeBuilder(CraftingBookCategory.MISC, List.of(output))
					.group(group)
					.define('a', arrowIngredient)
					.define('p', potionIngredient)
					.pattern("aaa")
					.pattern("apa")
					.pattern("aaa")
					.build();
				return new RecipeHolder<>(id, recipe);
			})
			.toList();
	}

	private TippedArrowRecipeMaker() {

	}
}
