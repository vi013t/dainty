package violet.dainty.features.fencejump;

import java.util.List;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent.LivingJumpEvent;
import violet.dainty.Dainty;
import violet.dainty.config.DaintyConfig;

/**
 * The event handler for the "jump on fences" feature, which allows players to jump on fences; See {@link FenceJumpEventHandler#jumpOnFences(LivingJumpEvent)}. 
 * This is automatically subscribed to the Neoforge event bus with {@link EventBusSubscriber @EventBusSubscriber}.
 */
@EventBusSubscriber(modid = Dainty.MODID)
public class FenceJumpEventHandler {

	/**
	 * Fired by the Neoforge event bus when an entity jumps; This method enables jumping on fences for players if it's enabled in the Dainty config.
	 * 
	 * @param event The {@link LivingJumpEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	public static void jumpOnFences(LivingJumpEvent event) {
		if (!DaintyConfig.ALLOW_JUMPING_ON_FENCES.get()) return;

		if (event.getEntity() instanceof LocalPlayer player && player.input.jumping && isPlayerNextToFence(player)) {
			player.setDeltaMovement(player.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
		}
	}

	/**
	 * Returns whether the player is next to a fence block.
	 * 
	 * @param player The player to check for fences nearby of
	 * 
	 * @return Whether the player is next to a fence block
	 */
	private static boolean isPlayerNextToFence(Player player) {
		BlockPos pos = player.blockPosition();

		for (int offsetX = -1; offsetX < 2; ++offsetX) {
			for (int offsetZ = -1; offsetZ < 2; ++offsetZ) {
				BlockState state = player.level().getBlockState(pos.offset(offsetX, 0, offsetZ));
				Block block = state.getBlock();
				if (block instanceof FenceBlock || block instanceof WallBlock || block instanceof FenceGateBlock || canJumpToo(block)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns whether the given block is registered as a special override that the player can also jump over, even if it's not a fence.
	 * This is used, for example, for modded fences that aren't registered as fence blocks.
	 * 
	 * @param block the block to check for
	 * 
	 * @return Whether this block can also be jumped over.
	 */
	private static boolean canJumpToo(Block block) {
		List<? extends String> jumpTheseToo = List.of("cfm:oak_upgraded_fence");
		if (jumpTheseToo.isEmpty()) return false;

		String blockName = BuiltInRegistries.BLOCK.getKey(block).toString();
		return jumpTheseToo.contains(blockName);
	}
}
