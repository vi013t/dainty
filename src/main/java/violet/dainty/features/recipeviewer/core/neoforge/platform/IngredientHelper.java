package violet.dainty.features.recipeviewer.core.neoforge.platform;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.ComposterBlock;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformIngredientHelper;
import violet.dainty.features.recipeviewer.core.common.util.RegistryUtil;

public class IngredientHelper implements IPlatformIngredientHelper {
	@Override
	public Ingredient createShulkerDyeIngredient(DyeColor color) {
		DyeItem dye = DyeItem.byColor(color);
		TagKey<Item> colorTag = color.getTag();
		Ingredient.Value colorList = new Ingredient.TagValue(colorTag);
		Registry<Item> itemRegistry = RegistryUtil.getRegistry(Registries.ITEM);
		Iterable<Holder<Item>> coloredItems = itemRegistry.getTagOrEmpty(colorTag);
		boolean contains = StreamSupport.stream(coloredItems.spliterator(), false)
			.anyMatch(h -> h.value() == dye);
		Stream<Ingredient.Value> colorIngredientStream;
		if (!contains) {
			ItemStack dyeStack = new ItemStack(dye);
			Ingredient.Value dyeList = new Ingredient.ItemValue(dyeStack);
			colorIngredientStream = Stream.of(dyeList, colorList);
		} else {
			colorIngredientStream = Stream.of(colorList);
		}
		// Shulker box special recipe allows the matching dye item or any item in the tag.
		// we need to specify both in case someone removes the dye item from the dye tag
		// as the item will still be valid for this recipe.
		return Ingredient.fromValues(colorIngredientStream);
	}

	@Override
	public List<Ingredient> getPotionContainers(PotionBrewing potionBrewing) {
		return potionBrewing.containers;
	}

	@Override
	public Stream<Ingredient> getPotionIngredients(PotionBrewing potionBrewing) {
		return Stream.concat(
			potionBrewing.containerMixes.stream(),
			potionBrewing.potionMixes.stream()
		)
			.map(PotionBrewing.Mix::ingredient);
	}

	@Override
	public float getCompostValue(ItemStack itemStack) {
		return ComposterBlock.getValue(itemStack);
	}
}
