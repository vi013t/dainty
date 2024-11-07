package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.subtypes;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;

import org.jetbrains.annotations.Nullable;

public class OminousBottleSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
	public static final OminousBottleSubtypeInterpreter INSTANCE = new OminousBottleSubtypeInterpreter();

	private OminousBottleSubtypeInterpreter() {

	}

	@Override
	public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
		return ingredient.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack itemStack, UidContext context) {
		Integer amplifier = itemStack.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
		if (amplifier == null) {
			return "";
		}
		return amplifier.toString();
	}
}
