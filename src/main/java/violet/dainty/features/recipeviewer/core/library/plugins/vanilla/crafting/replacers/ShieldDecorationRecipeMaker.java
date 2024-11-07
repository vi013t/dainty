package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.replacers;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import violet.dainty.features.recipeviewer.core.common.util.RegistryUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;

public final class ShieldDecorationRecipeMaker {
	public static List<RecipeHolder<CraftingRecipe>> createRecipes() {
		Iterable<Holder<Item>> banners = RegistryUtil.getRegistry(Registries.ITEM).getTagOrEmpty(ItemTags.BANNERS);

		Set<DyeColor> colors = EnumSet.noneOf(DyeColor.class);

		return StreamSupport.stream(banners.spliterator(), false)
			.filter(Holder::isBound)
			.map(Holder::value)
			.filter(BannerItem.class::isInstance)
			.map(BannerItem.class::cast)
			.filter(item -> colors.add(item.getColor()))
			.map(ShieldDecorationRecipeMaker::createRecipe)
			.toList();
	}

	private static RecipeHolder<CraftingRecipe> createRecipe(BannerItem banner) {
		NonNullList<Ingredient> inputs = NonNullList.of(
			Ingredient.EMPTY,
			Ingredient.of(Items.SHIELD),
			Ingredient.of(banner)
		);

		ItemStack output = createOutput(banner);

		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ModIds.MINECRAFT_ID, "dainty.shield.decoration." + output.getDescriptionId());
		CraftingRecipe recipe = new ShapelessRecipe("dainty.shield.decoration", CraftingBookCategory.MISC, output, inputs);
		return new RecipeHolder<>(id, recipe);
	}

	private static ItemStack createOutput(BannerItem banner) {
		DyeColor color = banner.getColor();
		ItemStack output = new ItemStack(Items.SHIELD);
		CompoundTag tag = new CompoundTag();
		tag.putInt("Base", color.getId());
		BlockItem.setBlockEntityData(output, BlockEntityType.BANNER, tag);
		return output;
	}

	private ShieldDecorationRecipeMaker() {

	}
}
