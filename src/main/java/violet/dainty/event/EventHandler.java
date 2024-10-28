package violet.dainty.event;

import java.util.List;
import java.util.function.Function;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.neoforged.neoforge.event.entity.living.SpawnClusterSizeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.BlockEvent.FarmlandTrampleEvent;
import violet.dainty.Config;
import violet.dainty.Dainty;

@EventBusSubscriber(modid=Dainty.MODID)
public class EventHandler {

	/**
	 * Makes it so that endermen can drop phantom membrane, since phantoms are removed.
	 * 
	 * @param event The {@link LivingDropsEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	public static void makeEndermanDropPhantomMembrane(LivingDropsEvent event) {
		if (event.getEntity() instanceof EnderMan enderman && !enderman.level().isClientSide() && Math.random() < Config.endermanPhantomMembraneDropChance()) {
			event.getDrops().add(new ItemEntity(enderman.level(), enderman.getX(), enderman.getY(), enderman.getZ(), new ItemStack(Items.PHANTOM_MEMBRANE)));
		}
	}
	
	/**
	 * Removes farmland trampling by listening for a {@link FarmlandTrampleEvent} and canceling it.
	 * 
	 * @param event The {@link FarmlandTrampleEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	public static void removeFarmlandTrampling(BlockEvent.FarmlandTrampleEvent event) {
		if (Config.disableFarmlandTrampling()) {
			event.setCanceled(true);
		}
	}

	/**
	 * Disables phantom spawning by listening for a spawn cluster and setting the size to 0.
	 * 
	 * @param event The {@link SpawnClusterSizeEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	public static void disablePhantomSpawns(SpawnClusterSizeEvent event) {
		if (event.getEntity() instanceof Phantom && Config.disablePhantoms()) {
			event.setSize(0);
		}
	}

	/**
	 * Enable jumping on fences.
	 * 
	 * @param event The {@link LivingJumpEvent} fired by Neoforge.
	 */
	@SubscribeEvent
	public static void allowJumpingOnFences(LivingJumpEvent event) {
		if (!Config.allowJumpingOnFences()) return;

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
