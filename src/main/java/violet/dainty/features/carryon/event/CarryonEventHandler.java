package violet.dainty.features.carryon.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import violet.dainty.Dainty;
import violet.dainty.features.carryon.CarryOnCommon;
import violet.dainty.features.carryon.CarryOnData;
import violet.dainty.features.carryon.CarryOnData.CarryType;
import violet.dainty.features.carryon.CarryOnDataManager;
import violet.dainty.features.carryon.ConfigLoader;
import violet.dainty.features.carryon.PickupHandler;
import violet.dainty.features.carryon.PlacementHandler;
import violet.dainty.features.carryon.ScriptReloadListener;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = Dainty.MODID)
public class CarryonEventHandler {
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void placeEntityOrPlaceOrPickupBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.isCanceled()) return;

		// Get values
		Player player = event.getEntity();
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		if (level.isClientSide) return;
		boolean success = false;
		CarryOnData carry = CarryOnDataManager.getCarryData(player);

		// Not carrying - pick up block
		if (!carry.isCarrying()) {
			if (PickupHandler.tryPickUpBlock((ServerPlayer) player, pos, level, (pState, pPos) -> {
				BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(level, pPos, pState, player);
				NeoForge.EVENT_BUS.post(breakEvent);
				return !breakEvent.isCanceled();
			})) {
				success = true;
			}
		} 
		
		// Already carrying - place block/entity
		else {

			// Carrying block
			if (carry.isCarrying(CarryType.BLOCK)) {
				PlacementHandler.tryPlaceBlock((ServerPlayer) player, pos, event.getFace(), (pos2, state) -> {
					BlockSnapshot snapshot = BlockSnapshot.create(level.dimension(), level, pos2);
					BlockEvent.EntityPlaceEvent event1 = new BlockEvent.EntityPlaceEvent(snapshot, level.getBlockState(pos), player);
					NeoForge.EVENT_BUS.post(event1);
					return !event1.isCanceled();
				});
			} 
			
			// Carrying entity
			else {
				PlacementHandler.tryPlaceEntity((ServerPlayer) player, pos, event.getFace(), (pPos, toPlace) -> {
					if (toPlace instanceof Mob mob) {
						mob.setPos(pPos.x, pPos.y, pPos.z);
						@SuppressWarnings("null")
						MobSpawnEvent.PositionCheck checkSpawn = new MobSpawnEvent.PositionCheck(mob, (ServerLevelAccessor) level, MobSpawnType.EVENT, null);
						NeoForge.EVENT_BUS.post(checkSpawn);
						return checkSpawn.getResult() != MobSpawnEvent.PositionCheck.Result.FAIL;
					}
					return true;
				});
			}

			success = true;
		}

		// Update event if place was successful
		if (success) {
			event.setUseBlock(TriState.FALSE);
			event.setUseItem(TriState.FALSE);
			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void pickupEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.isCanceled()) return;

		// Get values
		Player player = event.getEntity();
		Level level = event.getLevel();
		Entity target = event.getTarget();
		if (level.isClientSide) return;
		CarryOnData carry = CarryOnDataManager.getCarryData(player);

		// Not carrying - pick up entity
		if (!carry.isCarrying()) {
			if (PickupHandler.tryPickupEntity((ServerPlayer) player, target, (toPickup) -> {
				EntityPickupEvent pickupEvent = new EntityPickupEvent((ServerPlayer) player, toPickup);
				NeoForge.EVENT_BUS.post(pickupEvent);
				return !pickupEvent.isCanceled();
			})) {
				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
				return;
			}
		} 
		
		// Already carrying - stack entity
		else if (carry.isCarrying(CarryType.ENTITY) || carry.isCarrying(CarryType.PLAYER)) {
			PlacementHandler.tryStackEntity((ServerPlayer) player, target);
		}
	}

	@SubscribeEvent
	public static void onDatapackRegister(AddReloadListenerEvent event) {
		event.addListener(new ScriptReloadListener());
	}

	@SubscribeEvent
	public static void onDatapackSync(OnDatapackSyncEvent event) {
		ServerPlayer player = event.getPlayer();
		if (player == null) {
			for (ServerPlayer p : event.getPlayerList().getPlayers()) ScriptReloadListener.syncScriptsWithClient(p);
		} 
		else ScriptReloadListener.syncScriptsWithClient(player);
	}

	@SubscribeEvent
	public static void onTagsUpdate(TagsUpdatedEvent event) {
		ConfigLoader.onConfigLoaded(event.getRegistryAccess());
	}

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Post event) {
		for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) CarryOnCommon.onCarryTick(player);
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event) {
		if (!event.getOriginal().level().isClientSide) PlacementHandler.placeCarriedOnDeath((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getEntity(), event.isWasDeath());
	}

	@SubscribeEvent
	public static void harvestSpeed(PlayerEvent.BreakSpeed event) {
		if (!CarryOnCommon.onTryBreakBlock(event.getEntity())) event.setNewSpeed(0);
	}

	@SubscribeEvent
	public static void attackEntity(AttackEntityEvent event) {
		if(!CarryOnCommon.onAttackedByPlayer(event.getEntity()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onBreakBlock(BlockEvent.BreakEvent event) {
		if (!CarryOnCommon.onTryBreakBlock(event.getPlayer())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void playerAttack(AttackEntityEvent event) {
		if (event.getEntity() instanceof Player player) CarryOnCommon.onPlayerAttacked(player);
	}
}
