package violet.dainty.features.blockreverting;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ConversionTool<T extends Item> {
	public abstract Map<BlockState, BlockState> getReverseConversions();	
	public abstract Class<T> getToolClass();
	public abstract SoundEvent getConversionSound();

	/**
	 * Performs any extra steps that should take place after converting a block. For example, when "un-tilling"
	 * farmland into dirt, this will break the block above if it is a crop and drop the items. This is called on
	 * both the server and client sides, so make the appropriate checks when necessary.
	 * 
	 * <br/><br/>
	 * 
	 * See {@link violet.dainty.features.blockreverting.HoeConversion#performExtraConversionSteps(Level, Player, BlockState, BlockPos)
	 * the hoe implementation} for an example.
	 * 
	 * @param level The level the block is being converted on
	 * @param player The player converting the block
	 * @param block The block being converted; i.e., the block state <i>before</i> the conversion takes place
	 * @param position The position of the converted block
	 */
	public default void performExtraConversionSteps(Level level, Player player, BlockState block, BlockPos position) {}
}
