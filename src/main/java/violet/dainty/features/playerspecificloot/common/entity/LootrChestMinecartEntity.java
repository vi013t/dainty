package violet.dainty.features.playerspecificloot.common.entity;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.advancement.IContainerTrigger;
import violet.dainty.features.playerspecificloot.api.data.LootrBlockType;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;
import violet.dainty.features.playerspecificloot.neoforge.entity.ILootrNeoForgeCart;

public class LootrChestMinecartEntity extends AbstractMinecartContainer implements ILootrNeoForgeCart {
  private static BlockState cartNormal = null;
  private final Set<UUID> clientOpeners = new ObjectLinkedOpenHashSet<>();
  private boolean opened = false;

  public LootrChestMinecartEntity(EntityType<LootrChestMinecartEntity> type, Level world) {
    super(type, world);
  }

  public LootrChestMinecartEntity(Level worldIn, double x, double y, double z) {
    super(LootrRegistry.getMinecart(), x, y, z, worldIn);
  }

  @Override
  public void unpackChestVehicleLootTable(@Nullable Player p_219950_) {
  }

  @Override
  public @Nullable Set<UUID> getClientOpeners() {
    return clientOpeners;
  }

  @Override
  public boolean isClientOpened() {
    return opened;
  }

  @Override
  public void setClientOpened(boolean opened) {
    this.opened = opened;
  }

  @SuppressWarnings("null")
@Override
  public boolean isInvulnerableTo(@Nonnull DamageSource source) {
    if (this.isInvulnerable() && source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
      return true;
    }

    if (source.getEntity() instanceof Player player) {
      if (LootrAPI.canDestroyOrBreak(player)) {
        return false;
      }
      if (LootrAPI.isBreakDisabled()) {
        if (player.getAbilities().instabuild) {
          if (!player.isShiftKeyDown()) {
            player.displayClientMessage(Component.translatable("Dainty.MODID.message.cannot_break_sneak").setStyle(LootrAPI.getChatStyle()), false);
            return true;
          } else {
            return false;
          }
        } else {
          player.displayClientMessage(Component.translatable("dainty.message.cannot_break").setStyle(LootrAPI.getChatStyle()), false);
          return true;
        }
      } else if (!source.getEntity().isShiftKeyDown()) {
        ((Player) source.getEntity()).displayClientMessage(Component.translatable("dainty.message.cart_should_sneak").setStyle(LootrAPI.getChatStyle()), false);
        ((Player) source.getEntity()).displayClientMessage(Component.translatable("dainty.message.should_sneak2", Component.translatable("dainty.message.cart_should_sneak3").setStyle(Style.EMPTY.withBold(true))).setStyle(LootrAPI.getChatStyle()), false);
        // TODO: I think this is broken
      }
    } else {
      return true;
    }

    return true;
  }

  @Override
  public Item getDropItem() {
    return Items.CHEST_MINECART;
  }

  @Override
  public int getContainerSize() {
    return 27;
  }

  @Override
  public AbstractMinecart.Type getMinecartType() {
    return AbstractMinecart.Type.CHEST;
  }

  @Override
  public BlockState getDefaultDisplayBlockState() {
    if (cartNormal == null) {
      cartNormal = LootrRegistry.getChestBlock().defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH);
    }
    return cartNormal;
  }

  @Override
  public int getDefaultDisplayOffset() {
    return 8;
  }

  @Override
  public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventoryIn) {
    return ChestMenu.threeRows(id, playerInventoryIn, this);
  }

  @Override
  public void remove(@Nonnull RemovalReason reason) {
    this.setRemoved(reason);
    if (reason == Entity.RemovalReason.KILLED) {
      this.gameEvent(GameEvent.ENTITY_DIE);
    }
  }

  @Override
  public InteractionResult interact(@Nonnull Player player, @Nonnull InteractionHand hand) {
    if (level().isClientSide() || player.isSpectator() || !(player instanceof ServerPlayer serverPlayer)) {
      return InteractionResult.CONSUME;
    }

    if (player.isShiftKeyDown()) {
      LootrAPI.handleProviderSneak(this, serverPlayer);
    } else {
      LootrAPI.handleProviderOpen(this, serverPlayer);
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public void startOpen(@Nonnull Player player) {
    if (!player.isSpectator()) {
      performOpen((ServerPlayer) player);
    }
  }

  @Override
  public void stopOpen(@Nonnull Player player) {
    if (!player.isSpectator()) {
    }
  }

  @Override
  public void startSeenByPlayer(@Nonnull ServerPlayer pPlayer) {
    super.startSeenByPlayer(pPlayer);

    if (hasVisualOpened(pPlayer)) {
      performOpen(pPlayer);
    } else {
      performClose(pPlayer);
    }
  }

  @Override
  public @NotNull BlockPos getInfoPos() {
    return blockPosition();
  }

  @Override
  public ResourceKey<LootTable> getInfoLootTable() {
    return getLootTable();
  }

  @Override
  public @Nullable Component getInfoDisplayName() {
    return getDisplayName();
  }

  @Override
  public @NotNull ResourceKey<Level> getInfoDimension() {
    return level().dimension();
  }

  @Override
  public int getInfoContainerSize() {
    return getContainerSize();
  }

  @Override
  public long getInfoLootSeed() {
    return getLootTableSeed();
  }

  @Override
  public @Nullable NonNullList<ItemStack> getInfoReferenceInventory() {
    return null;
  }

  @Override
  public boolean isInfoReferenceInventory() {
    return false;
  }

  @Override
  public Level getInfoLevel() {
    return level();
  }

  @Override
  public LootrBlockType getInfoBlockType() {
    return LootrBlockType.ENTITY;
  }

  @Override
  public @NotNull Vec3 getInfoVec() {
    return position();
  }

  @Override
  @NotNull
  public UUID getInfoUUID() {
    return getUUID();
  }

  // TODO:
  @Override
  public boolean isPhysicallyOpen() {
    return false;
  }

  @Override
  public void markChanged() {
    setChanged();
    markDataChanged();
  }

  @Override
  public @Nullable IContainerTrigger getTrigger() {
    return LootrRegistry.getCartTrigger();
  }

  // TODO:
/*  @Override
  protected void applyNaturalSlowdown() {
    float f = 0.98F;
    if (this.lootTable == null) {
      int i = 15 - AbstractContainerMenu.getRedstoneSignalFromContainer(this);
      f += (float)i * 0.001F;
    }

    if (this.isInWater()) {
      f *= 0.95F;
    }

    this.setDeltaMovement(this.getDeltaMovement().multiply((double)f, 0.0, (double)f));
  } */
}
