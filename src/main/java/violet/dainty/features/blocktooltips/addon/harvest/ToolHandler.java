package violet.dainty.features.blocktooltips.addon.harvest;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.features.blocktooltips.api.IJadeProvider;

public interface ToolHandler extends IJadeProvider {

	ItemStack test(BlockState state, Level world, BlockPos pos);

	List<ItemStack> getTools();

}
