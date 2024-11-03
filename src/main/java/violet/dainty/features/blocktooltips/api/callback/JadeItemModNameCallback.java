package violet.dainty.features.blocktooltips.api.callback;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface JadeItemModNameCallback {

	@Nullable
	String gatherItemModName(ItemStack stack);

}
