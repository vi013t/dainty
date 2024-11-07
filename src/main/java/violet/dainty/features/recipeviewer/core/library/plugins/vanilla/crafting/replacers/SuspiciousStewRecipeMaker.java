package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.replacers;

import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet.ListBacked;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import violet.dainty.features.recipeviewer.core.common.util.RegistryUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;

public final class SuspiciousStewRecipeMaker {

	public static List<RecipeHolder<CraftingRecipe>> createRecipes() {
		String group = "dainty.suspicious.stew";
		Ingredient brownMushroom = Ingredient.of(Blocks.BROWN_MUSHROOM.asItem());
		Ingredient redMushroom = Ingredient.of(Blocks.RED_MUSHROOM.asItem());
		Ingredient bowl = Ingredient.of(Items.BOWL);

		return RegistryUtil.getRegistry(Registries.ITEM)
			.getTag(ItemTags.SMALL_FLOWERS)
			.stream()
			.flatMap(ListBacked::stream)
			.map(Holder::value)
			.filter(BlockItem.class::isInstance)
			.map(item -> ((BlockItem) item).getBlock())
			.filter(FlowerBlock.class::isInstance)
			.map(FlowerBlock.class::cast)
			.map(flowerBlock -> {
				Ingredient flower = Ingredient.of(flowerBlock.asItem());
				NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, brownMushroom, redMushroom, bowl, flower);
				ItemStack output = new ItemStack(Items.SUSPICIOUS_STEW, 1);
				var effects = flowerBlock.getSuspiciousEffects();
				output.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effects);
				ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ModIds.MINECRAFT_ID, "dainty.suspicious.stew." + flowerBlock.getDescriptionId());
				CraftingRecipe recipe = new ShapelessRecipe(group, CraftingBookCategory.MISC, output, inputs);
				return new RecipeHolder<>(id, recipe);
			})
			.toList();
	}

	private SuspiciousStewRecipeMaker() {

	}
}
