package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.subtypes;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.LightBlock;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;

import org.jetbrains.annotations.Nullable;

public class LightSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
	public static final LightSubtypeInterpreter INSTANCE = new LightSubtypeInterpreter();

	private LightSubtypeInterpreter() {

	}

	@Override
	public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
		BlockItemStateProperties properties = ingredient.get(DataComponents.BLOCK_STATE);
		if (properties == null) {
			return null;
		}
		return properties.get(LightBlock.LEVEL);
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
		BlockItemStateProperties properties = ingredient.get(DataComponents.BLOCK_STATE);
		if (properties == null) {
			return "";
		}
		Integer level = properties.get(LightBlock.LEVEL);
		if (level == null) {
			return "";
		}
		return level.toString();
	}
}