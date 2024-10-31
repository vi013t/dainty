package violet.dainty.features.cropharvest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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

@EventBusSubscriber(modid = Dainty.MODID)
public class CropHarvestEventHandler {

	@SubscribeEvent
	public static void rightClickHarvest(PlayerInteractEvent.RightClickBlock event) {
		if (event.getLevel().isClientSide() || event.getEntity().isSpectator() || event.getEntity().isCrouching() || event.getHand() != InteractionHand.MAIN_HAND) return;

        BlockState state = event.getLevel().getBlockState(event.getPos());
        ItemStack stackInHand = event.getItemStack();
        boolean hoeInUse = false;
		BlockHitResult hitResult = event.getHitVec();

		// Single block crops
        if (state.getBlock() instanceof CocoaBlock || state.getBlock() instanceof CropBlock || state.getBlock() instanceof NetherWartBlock) {
            if (isMature(state)) {
                completeHarvest(event.getLevel(), state, hitResult.getBlockPos(), event.getEntity(), event.getHand(), stackInHand, hoeInUse, true, () -> event.getLevel().setBlockAndUpdate(hitResult.getBlockPos(), getReplantState(state)));
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
            completeHarvest(event.getLevel(), state, breakPos, event.getEntity(), event.getHand(), stackInHand, hoeInUse, false, () -> event.getLevel().removeBlock(breakPos, false));
        }

    }

	private static InteractionResult completeHarvest(Level level, BlockState state, BlockPos pos, Player player, InteractionHand hand, ItemStack stackInHand, boolean hoeInUse, boolean removeReplant, Runnable setBlockAction) {
        if (!level.isClientSide) {
            dropStacks(state, (ServerLevel) level, pos, player, player.getItemInHand(hand), removeReplant);
            setBlockAction.run();
            if (hoeInUse) stackInHand.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            player.causeFoodExhaustion(0.008f);
        } else {
            player.playSound(state.getBlock() instanceof NetherWartBlock ? SoundEvents.NETHER_WART_PLANTED : SoundEvents.CROP_PLANTED, 1.0f, 1.0f);
        }

        return InteractionResult.SUCCESS;
    }

	private static boolean isMature(BlockState state) {
        if (state.getBlock() instanceof CocoaBlock) {
            return state.getValue(CocoaBlock.AGE) >= CocoaBlock.MAX_AGE;
        } else if (state.getBlock() instanceof CropBlock cropBlock) {
            return cropBlock.isMaxAge(state);
        } else if (state.getBlock() instanceof NetherWartBlock) {
            return state.getValue(NetherWartBlock.AGE) >= NetherWartBlock.MAX_AGE;
        }

        return false;
    }

    private static BlockState getReplantState(BlockState state) {
        if (state.getBlock() instanceof CocoaBlock) {
            return state.setValue(CocoaBlock.AGE, 0);
        } else if (state.getBlock() instanceof CropBlock cropBlock) {
            return cropBlock.getStateForAge(0);
        } else if (state.getBlock() instanceof NetherWartBlock) {
            return state.setValue(NetherWartBlock.AGE, 0);
        }

        return state;
    }

    private static void dropStacks(BlockState state, ServerLevel world, BlockPos pos, Entity entity, ItemStack toolStack, boolean removeReplant) {
        Item replant = new ItemStack(state.getBlock()).getItem();
        final boolean[] removedReplant = {!removeReplant};

        Block.getDrops(state, world, pos, null, entity, toolStack).forEach(stack -> {
            if (!removedReplant[0] && stack.getItem() == replant) {
                stack.setCount(stack.getCount() - 1);
                removedReplant[0] = true;
            }

            Block.popResource(world, pos, stack);
        });

        state.spawnAfterBreak(world, pos, toolStack, true);
    }
}
