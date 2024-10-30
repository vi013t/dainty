package violet.dainty.features.fencejump;

import java.util.List;
import java.util.function.Function;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent.LivingJumpEvent;
import violet.dainty.Dainty;
import violet.dainty.DaintyConfig;

@EventBusSubscriber(modid = Dainty.MODID)
public class FenceJumpEventHandler {

	/**
	 * Enable jumping on fences.
	 * 
	 * @param event The {@link LivingJumpEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	public static void allowJumpingOnFences(LivingJumpEvent event) {
		if (!DaintyConfig.allowJumpingOnFences()) return;

		// Checks if the block is jumpable
		Function<Block, Boolean> canJumpToo = block -> {
			List<? extends String> jumpTheseToo = List.of("cfm:oak_upgraded_fence");
			if (jumpTheseToo.isEmpty()) return false;

			String blockName = BuiltInRegistries.BLOCK.getKey(block).toString();
			return jumpTheseToo.contains(blockName);
		};

		// Checks if the player is next to a fence
		Function<LocalPlayer, Boolean> isPlayerNextToFence = player -> {
			BlockPos pos = player.blockPosition();

			for (int offsetX = -1; offsetX < 2; ++offsetX) {
				for (int offsetZ = -1; offsetZ < 2; ++offsetZ) {
					BlockState state = player.level().getBlockState(pos.offset(offsetX, 0, offsetZ));
					Block block = state.getBlock();
					if (block instanceof FenceBlock || block instanceof WallBlock || block instanceof FenceGateBlock || canJumpToo.apply(block)) {
						return true;
					}
				}
			}

			return false;
		};

		// Add some upward velocity if the player is trying to fence jump
		if (event.getEntity() instanceof LocalPlayer player && player.input.jumping && isPlayerNextToFence.apply(player)) {
			player.setDeltaMovement(player.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
		}
	}
}
