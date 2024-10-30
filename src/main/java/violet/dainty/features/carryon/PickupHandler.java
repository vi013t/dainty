package violet.dainty.features.carryon;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class PickupHandler {

    public static boolean canCarryGeneral(ServerPlayer player, Vec3 pos) {
        if (!player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty()) return false;
        if (player.position().distanceTo(pos) > 2.5) return false;

        CarryOnData carry = CarryOnDataManager.getCarryData(player);
        if (carry.isCarrying()) return false;
        if (!carry.isKeyPressed()) return false;
        if (player.tickCount == carry.getTick()) return false;
        if (player.gameMode.getGameModeForPlayer() == GameType.SPECTATOR || player.gameMode.getGameModeForPlayer() == GameType.ADVENTURE) return false;
        return true;
    }

    @SuppressWarnings({ "null", "deprecation" })
	public static boolean tryPickUpBlock(ServerPlayer player, BlockPos pos, Level level, @Nullable BiFunction<BlockState, BlockPos, Boolean> pickupCallback) {
        if (!canCarryGeneral(player, Vec3.atCenterOf(pos))) return false;

        CarryOnData carry = CarryOnDataManager.getCarryData(player);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        BlockState state = level.getBlockState(pos);
        CompoundTag nbt = null;
        if (blockEntity != null) nbt = blockEntity.saveWithId(level.registryAccess());

        if (!ListHandler.isPermitted(state.getBlock())) return false;
        if (state.getDestroySpeed(level, pos) == -1 && !player.isCreative()) return false;
        if (blockEntity == null) return false;
		if (nbt.contains("Lock") && !nbt.getString("Lock").equals("")) return false;

        Optional<PickupCondition> cond = PickupConditionHandler.getPickupCondition(state);
        if (cond.isPresent()) {
            if (!cond.get().isFulfilled(player)) return false;
        }

        boolean doPickup = pickupCallback == null ? true : pickupCallback.apply(state, pos);
        if (!doPickup) return false;

        Optional<CarryOnScript> result =  ScriptManager.inspectBlock(state, level, pos, nbt);
        if (result.isPresent()) {
            CarryOnScript script = result.get();
            if (!script.fulfillsConditions(player)) return false;

            carry.setActiveScript(script);

            String cmd = script.scriptEffects().commandInit();
            if (!cmd.isEmpty()) player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack(), "/execute as " + player.getGameProfile().getName() + " run " + cmd);
        }

        carry.setBlock(state, blockEntity);

        level.removeBlockEntity(pos);
        level.removeBlock(pos, false);

        CarryOnDataManager.setCarryData(player, carry);
        level.playSound(null, pos, state.getSoundType().getHitSound(), SoundSource.BLOCKS, 1.0f, 0.5f);
        player.swing(InteractionHand.MAIN_HAND, true);
        return true;
    }

    @SuppressWarnings("null")
	public static boolean tryPickupEntity(ServerPlayer player, Entity entity, @Nullable Function<Entity, Boolean> pickupCallback) {
        if(!canCarryGeneral(player, entity.position())) return false;
        if (entity.invulnerableTime != 0) return false;
        if(entity.isRemoved()) return false;

        if (entity instanceof TamableAnimal tame) {
            UUID owner = tame.getOwnerUUID();
            UUID playerID = player.getGameProfile().getId();
            if (owner != null && !owner.equals(playerID)) return false;
        }

        if (!ListHandler.isPermitted(entity)) {
            return false;
        }

        //Non-Creative only guards
        if (!player.isCreative()) {
            if (entity.getType().getCategory() == MobCategory.MONSTER) return false;
            if (2.5 < entity.getBbHeight() || 1.5 < entity.getBbWidth()) return false;
        }

        Optional<PickupCondition> cond = PickupConditionHandler.getPickupCondition(entity);
        if (cond.isPresent()) {
    		if (!cond.get().isFulfilled(player)) return false;
        }

        boolean doPickup = pickupCallback == null ? true : pickupCallback.apply(entity);
        if (!doPickup) return false;

        CarryOnData carry = CarryOnDataManager.getCarryData(player);

        Optional<CarryOnScript> result =  ScriptManager.inspectEntity(entity);
        if (result.isPresent()) {
            CarryOnScript script = result.get();
            if (!script.fulfillsConditions(player)) return false;
            carry.setActiveScript(script);
        }

        if (entity instanceof Player otherPlayer) {
            if (!player.isCreative() && otherPlayer.isCreative()) return false;

            otherPlayer.ejectPassengers();
            otherPlayer.stopRiding();

            if (result.isPresent()) {
                String cmd = result.get().scriptEffects().commandInit();
                if (!cmd.isEmpty()) player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack(), "/execute as " + player.getGameProfile().getName() + " run " + cmd);
            }

            otherPlayer.startRiding(player);
            PacketDistributor.sendToPlayer(player, new ClientboundStartRidingPacket(otherPlayer.getId(), true));
            carry.setCarryingPlayer();
            player.swing(InteractionHand.MAIN_HAND, true);
            player.level().playSound(null, player.getOnPos(), SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.AMBIENT, 1.0f, 0.5f);
            CarryOnDataManager.setCarryData(player, carry);
            return true;

        }

        entity.ejectPassengers();
        entity.stopRiding();
        if (entity instanceof Animal animal) {
            animal.dropLeash(true, true);
        }

        if (result.isPresent()) {
            String cmd = result.get().scriptEffects().commandInit();
            if (!cmd.isEmpty()) player.getServer().getCommands().performPrefixedCommand(player.getServer().createCommandSourceStack(), "/execute as " + player.getGameProfile().getName() + " run " + cmd);
        }

        carry.setEntity(entity);
        entity.remove(RemovalReason.UNLOADED_WITH_PLAYER);

        player.level().playSound(null, player.getOnPos(), SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.AMBIENT, 1.0f, 0.5f);
        CarryOnDataManager.setCarryData(player, carry);
        player.swing(InteractionHand.MAIN_HAND, true);
        return true;
    }

}
