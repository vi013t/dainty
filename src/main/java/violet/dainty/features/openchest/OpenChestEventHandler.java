package violet.dainty.features.openchest;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import violet.dainty.Dainty;

@EventBusSubscriber(modid = Dainty.MODID)
public class OpenChestEventHandler {

	private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>() {
		@Override
        public Optional<MenuProvider> acceptDouble(final ChestBlockEntity firstChest, final ChestBlockEntity secondChest) {
            final Container container = new CompoundContainer(firstChest, secondChest);
            return Optional.of(new MenuProvider() {
                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int flag, Inventory inventory, Player player) {
                    if (firstChest.canOpen(player) && secondChest.canOpen(player)) {
                        firstChest.unpackLootTable(inventory.player);
                        secondChest.unpackLootTable(inventory.player);
                        return ChestMenu.sixRows(flag, inventory, container);
                    } else {
                        return null;
                    }
                }

                @Override
                public Component getDisplayName() {
                    if (firstChest.hasCustomName()) {
                        return firstChest.getDisplayName();
                    } else {
                        return (Component)(secondChest.hasCustomName() ? secondChest.getDisplayName() : Component.translatable("container.chestDouble"));
                    }
                }
            });
        }

        public Optional<MenuProvider> acceptSingle(ChestBlockEntity p_51602_) {
            return Optional.of(p_51602_);
        }

        public Optional<MenuProvider> acceptNone() {
            return Optional.empty();
        }
    };
	
	@SubscribeEvent
	public static void openContainer(PlayerInteractEvent.RightClickBlock event) {
		if (event.getLevel().isClientSide()) return;

		BlockState state = event.getLevel().getBlockState(event.getPos());
		if (state.getBlock() instanceof ChestBlock chest) {
			if (isChestBlockedAt(event.getLevel(), event.getPos())) {
				openChest(chest, state, event.getLevel(), event.getPos(), event.getEntity());
			}
		}
	}    

	private static void openChest(ChestBlock chest, BlockState state, Level level, BlockPos pos, Player player) {
		if (level.isClientSide) return;
		MenuProvider menu = getMenuProvider(chest, state, level, pos);
		if (menu != null) {
			player.openMenu(menu);
			player.awardStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
			PiglinAi.angerNearbyPiglins(player, true);
		}
	}
	
	private static MenuProvider getMenuProvider(ChestBlock chest, BlockState state, Level level, BlockPos pos) {
        return combine(chest, state, level, pos, false).apply(MENU_PROVIDER_COMBINER).orElse(null);
    }

	private static DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(ChestBlock chest, BlockState state, Level level, BlockPos pos, boolean override) {
		BiPredicate<LevelAccessor, BlockPos> predicate;
        if (override) predicate = (p_51578_, p_51579_) -> false;
        else predicate = OpenChestEventHandler::nonBlockedChest;
        return DoubleBlockCombiner.combineWithNeigbour(
            getBlockEntityType(chest), ChestBlock::getBlockType, ChestBlock::getConnectedDirection, HorizontalDirectionalBlock.FACING, state, level, pos, predicate
        );
	}

    private static boolean nonBlockedChest(LevelAccessor level, BlockPos pos) {
		return false;
    }    
	
	private static boolean isChestBlockedAt(LevelAccessor level, BlockPos pos) {
        return isBlockedChestByBlock(level, pos) || isCatSittingOnChest(level, pos);
    }

    private static boolean isBlockedChestByBlock(BlockGetter level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return level.getBlockState(blockpos).isRedstoneConductor(level, blockpos);
    }

    private static boolean isCatSittingOnChest(LevelAccessor level, BlockPos pos) {
        List<Cat> list = level.getEntitiesOfClass(
            Cat.class,
            new AABB(
                (double) pos.getX(),
                (double)(pos.getY() + 1),
                (double) pos.getZ(),
                (double)(pos.getX() + 1),
                (double)(pos.getY() + 2),
                (double)(pos.getZ() + 1)
            )
        );
        if (!list.isEmpty()) {
            for (Cat cat : list) {
                if (cat.isInSittingPose()) {
                    return true;
                }
            }
        }

        return false;
    }

	private static BlockEntityType<? extends ChestBlockEntity> getBlockEntityType(AbstractChestBlock<?> chest) {
		if (chest instanceof TrappedChestBlock) return BlockEntityType.TRAPPED_CHEST;
		return BlockEntityType.CHEST;
	}
}
