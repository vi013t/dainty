package violet.dainty.features.blocktooltips.api.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface CustomEnchantPower {

	float getEnchantPowerBonus(BlockState state, Level world, BlockPos pos);

}
