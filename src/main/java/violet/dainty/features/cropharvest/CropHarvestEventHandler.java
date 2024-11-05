package violet.dainty.features.cropharvest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import violet.dainty.Dainty;
import violet.dainty.config.DaintyConfig;

/**
 * The event handler for the right-click crop harvesting feature. This will listen for player right-clicking on blocks and handle crop harvesting
 * if appropriate; See {@link CropHarvestEventHandler#rightClickHarvest(PlayerInteractEvent.RightClickBlock)}. This is automatically subscribed
 * to the Neoforge event bus with {@link EventBusSubscriber @EventBusSubscriber}.
 */
@EventBusSubscriber(modid = Dainty.MODID)
public class CropHarvestEventHandler {

	/**
	 * Called by the Neoforge event bus when a player right-clicks a block. This will handle right-click harvesting if applicable, and if so,
	 * the crop will be harvested and replanted, and the drops will be dropped.
	 * 
	 * @param event The {@code RightClickBlock} event fired by the Neoforge event bus.
	 */
	@SubscribeEvent
	public static void rightClickHarvest(PlayerInteractEvent.RightClickBlock event) {

		// Guard checks
		if (!DaintyConfig.ENABLE_RIGHT_CLICK_CROP_HARVEST.get() || event.getLevel().isClientSide() || event.getEntity().isSpectator() || event.getEntity().isCrouching() || event.getHand() != InteractionHand.MAIN_HAND) return;

		// Define variables
        BlockState state = event.getLevel().getBlockState(event.getPos());
        ItemStack stackInHand = event.getItemStack();
		BlockHitResult hitResult = event.getHitVec();

		// Single block crops
        if (state.getBlock() instanceof CocoaBlock || state.getBlock() instanceof CropBlock || state.getBlock() instanceof NetherWartBlock) {
            if (isMature(state)) {
                harvestCrop(event.getLevel(), state, hitResult.getBlockPos(), event.getEntity(), event.getHand(), stackInHand, true, () -> event.getLevel().setBlockAndUpdate(hitResult.getBlockPos(), getReplantState(state)));
			}
        } 
		
		// Multi-block crops - cactus, sugar cane
		else if (state.getBlock() instanceof SugarCaneBlock || state.getBlock() instanceof CactusBlock) {
            if (hitResult.getDirection() == Direction.UP && ((stackInHand.getItem() == Items.SUGAR_CANE && state.getBlock() instanceof SugarCaneBlock) || (stackInHand.getItem() == Items.CACTUS && state.getBlock() instanceof CactusBlock))) {
				return;
            }

            Block lookingFor = state.getBlock() instanceof SugarCaneBlock ? Blocks.SUGAR_CANE : Blocks.CACTUS;
            BlockPos bottom = hitResult.getBlockPos();
            while (event.getLevel().getBlockState(bottom.below()).is(lookingFor)) {
                bottom = bottom.below();
            }

            // Only one block tall
            if (!event.getLevel().getBlockState(bottom.above()).is(lookingFor)) {
				return;
            }

            final BlockPos breakPos = bottom.above(1);
            harvestCrop(event.getLevel(), state, breakPos, event.getEntity(), event.getHand(), stackInHand, false, () -> event.getLevel().removeBlock(breakPos, false));
        }

    }

	/**
	 * Harvests and replants the given crop, assuming a replant function was provided.
	 * 
	 * @param level The level the crop exists in
	 * @param state The crop block state
	 * @param pos The crop block position
	 * @param player The player harvesting the crop
	 * @param hand The hand the player harvested the crop with
	 * @param stackInHand The item stack in the player's hand when harvesting the crop
	 * @param removeReplant Whether to remove the replanted item
	 * @param replantAction The replant action as a {@link Runnable}
	 */
	private static void harvestCrop(Level level, BlockState state, BlockPos pos, Player player, InteractionHand hand, ItemStack stackInHand, boolean removeReplant, Runnable replantAction) {
		// Server - harvest and replant, drop items, cause food exhaustion
        if (!level.isClientSide) {
            dropStacks(state, (ServerLevel) level, pos, player, player.getItemInHand(hand), removeReplant);
            replantAction.run();
            player.causeFoodExhaustion(0.008f);
        } 
		
		// Client - play sound
		else {
            player.playSound(state.getBlock() instanceof NetherWartBlock ? SoundEvents.NETHER_WART_PLANTED : SoundEvents.CROP_PLANTED, 1.0f, 1.0f);
        }
    }

	/**
	 * Returns whether the given crop is fully mature and ready to be harvested.
	 * 
	 * @param state The crop to check the maturity of.
	 * 
	 * @return Whether the given crop block state is fully mature.
	 */
	private static boolean isMature(BlockState state) {
        if (state.getBlock() instanceof CocoaBlock) return state.getValue(CocoaBlock.AGE) >= CocoaBlock.MAX_AGE;
        else if (state.getBlock() instanceof CropBlock cropBlock) return cropBlock.isMaxAge(state);
        else if (state.getBlock() instanceof NetherWartBlock) return state.getValue(NetherWartBlock.AGE) >= NetherWartBlock.MAX_AGE;
        return false;
    }

	/**
	 * Returns the {@link BlockState} of the given crop as a freshly planted unaged crop.
	 * 
	 * @param state The crop to get the base planted state of.
	 * 
	 * @return The state of the crop when first planted.
	 */
    private static BlockState getReplantState(BlockState state) {
        if (state.getBlock() instanceof CocoaBlock) return state.setValue(CocoaBlock.AGE, 0);
        else if (state.getBlock() instanceof CropBlock cropBlock) return cropBlock.getStateForAge(0);
        else if (state.getBlock() instanceof NetherWartBlock) return state.setValue(NetherWartBlock.AGE, 0);
        return state;
    }

	/**
	 * Drops the harvested crop items in the world.
	 * 
	 * @param state The crop being harvested
	 * @param level The level to drop the crops in
	 * @param pos The position of the crop block
	 * @param player The player harvesting the crop
	 * @param toolStack The tool the player is harvesting with
	 * @param removeReplant Whether to remove the replanted item
	 */
    private static void dropStacks(BlockState state, ServerLevel level, BlockPos pos, Player player, ItemStack toolStack, boolean removeReplant) {
        Item replant = new ItemStack(state.getBlock()).getItem();
        final boolean[] removedReplant = { !removeReplant };

        Block.getDrops(state, level, pos, null, player, toolStack).forEach(stack -> {
            if (!removedReplant[0] && stack.getItem() == replant) {
                stack.setCount(stack.getCount() - 1);
                removedReplant[0] = true;
            }

            Block.popResource(level, pos, stack);
        });

        state.spawnAfterBreak(level, pos, toolStack, true);
    }
}
