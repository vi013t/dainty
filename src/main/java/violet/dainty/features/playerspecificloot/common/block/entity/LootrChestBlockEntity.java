package violet.dainty.features.playerspecificloot.common.block.entity;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Sets;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.storage.loot.LootTable;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.advancement.IContainerTrigger;
import violet.dainty.features.playerspecificloot.api.data.LootrBlockType;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;
import violet.dainty.features.playerspecificloot.common.data.LootrInventory;
import violet.dainty.features.playerspecificloot.neoforge.block.entity.ILootrNeoForgeBlockEntity;

public class LootrChestBlockEntity extends ChestBlockEntity implements ILootrNeoForgeBlockEntity {
  private final ChestLidController chestLidController = new ChestLidController();
  protected UUID infoId;
  private final Set<UUID> clientOpeners = new ObjectLinkedOpenHashSet<>();
  private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
    @Override
    protected void onOpen(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
      LootrChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_OPEN);
    }

    @Override
    protected void onClose(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
      LootrChestBlockEntity.playSound(level, pos, state, SoundEvents.CHEST_CLOSE);
    }

    @Override
    protected void openerCountChanged(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, int p_155364_, int p_155365_) {
      LootrChestBlockEntity.this.signalOpenCount(level, pos, state, p_155364_, p_155365_);
    }

    @Override
    protected boolean isOwnContainer(@Nonnull Player player) {
      if ((player.containerMenu instanceof ChestMenu menu)) {
        if (menu.getContainer() instanceof LootrInventory data) {
          return LootrChestBlockEntity.this.getInfoUUID().equals(data.getInfo().getInfoUUID());
        }
      }

      return false;
    }
  };
  protected boolean clientOpened;
  private boolean savingToItem = false;

  protected LootrChestBlockEntity(BlockEntityType<?> p_155327_, BlockPos p_155328_, BlockState p_155329_) {
    super(p_155327_, p_155328_, p_155329_);
  }

  public LootrChestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    this(LootrRegistry.getChestBlockEntity(), pWorldPosition, pBlockState);
  }

  @Override
  public void defaultTick(Level level, BlockPos pos, BlockState state) {
    ILootrNeoForgeBlockEntity.super.defaultTick(level, pos, state);
    chestLidController.tickLid();
  }

  protected static void playSound(Level pLevel, BlockPos pPos, BlockState pState, SoundEvent pSound) {
    ChestType chestType = pState.getValue(ChestBlock.TYPE);
    if (chestType != ChestType.LEFT) {
      double d0 = (double) pPos.getX() + 0.5D;
      double d1 = (double) pPos.getY() + 0.5D;
      double d2 = (double) pPos.getZ() + 0.5D;
      if (chestType == ChestType.RIGHT) {
        Direction direction = ChestBlock.getConnectedDirection(pState);
        d0 += (double) direction.getStepX() * 0.5D;
        d2 += (double) direction.getStepZ() * 0.5D;
      }

      pLevel.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, pLevel.random.nextFloat() * 0.1F + 0.9F);
    }
  }

  public static int getOpenCount(BlockGetter pLevel, BlockPos pPos) {
    BlockState blockState = pLevel.getBlockState(pPos);
    if (blockState.hasBlockEntity()) {
      BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
      if (blockEntity instanceof LootrChestBlockEntity chest) {
        return chest.openersCounter.getOpenerCount();
      }
    }

    return 0;
  }

  @Override
  public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
    super.loadAdditional(compound, provider);
    this.tryLoadLootTable(compound);
    if (compound.hasUUID("LootrId")) {
      this.infoId = compound.getUUID("LootrId");
    }
    if (this.infoId == null) {
      getInfoUUID();
    }
    clientOpeners.clear();
    if (compound.contains("LootrOpeners")) {
      ListTag list = compound.getList("LootrOpeners", CompoundTag.TAG_INT_ARRAY);
      for (Tag thisTag : list) {
        clientOpeners.add(NbtUtils.loadUUID(thisTag));
      }
    }
  }

  @Override
  public void saveToItem(@Nonnull ItemStack itemStack, @Nonnull HolderLookup.Provider provider) {
    savingToItem = true;
    super.saveToItem(itemStack, provider);
    savingToItem = false;
  }

  @Override
  protected void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
    super.saveAdditional(compound, provider);
    this.trySaveLootTable(compound);
    if (!LootrAPI.shouldDiscard() && !savingToItem) {
      compound.putUUID("LootrId", getInfoUUID());
    }
  }

  @Override
  public boolean triggerEvent(int pId, int pType) {
    if (pId == 1) {
      this.chestLidController.shouldBeOpen(pType > 0);
      return true;
    } else {
      return super.triggerEvent(pId, pType);
    }
  }

  @SuppressWarnings("null")
@Override
  public void startOpen(@Nonnull Player pPlayer) {
    if (!this.remove && !pPlayer.isSpectator()) {
      this.openersCounter.incrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }

  }

  @SuppressWarnings("null")
@Override
  public void stopOpen(@Nonnull Player pPlayer) {
    if (!this.remove && !pPlayer.isSpectator()) {
      this.openersCounter.decrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  @SuppressWarnings("null")
@Override
  public void recheckOpen() {
    if (!this.remove) {
      this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
    }
  }

  @Override
  public float getOpenNess(float pPartialTicks) {
    return this.chestLidController.getOpenness(pPartialTicks);
  }

  @Override
  @NotNull
  public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider provider) {
    CompoundTag result = super.getUpdateTag(provider);
    saveAdditional(result, provider);
    Set<UUID> currentOpeners = getVisualOpeners();
    if (currentOpeners != null) {
      ListTag list = new ListTag();
      for (UUID opener : Sets.intersection(currentOpeners, LootrAPI.getPlayerIds())) {
        list.add(NbtUtils.createUUID(opener));
      }
      if (!list.isEmpty()) {
        result.put("LootrOpeners", list);
      }
    }
    return result;
  }

  @Override
  @Nullable
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
  }

  @Override
  public void unpackLootTable(@Nullable Player player) {
  }

  @Override
  public @Nullable Set<UUID> getClientOpeners() {
    return clientOpeners;
  }

  @Override
  public LootrBlockType getInfoBlockType() {
    return LootrBlockType.CHEST;
  }

  @Override
  public LootrInfoType getInfoType() {
    return LootrInfoType.CONTAINER_BLOCK_ENTITY;
  }

  @Override
  public void markChanged() {
    setChanged();
    markDataChanged();
  }

  @Override
  @NotNull
  public UUID getInfoUUID() {
    if (this.infoId == null) {
      this.infoId = UUID.randomUUID();
    }
    return this.infoId;
  }

  @Override
  public boolean isPhysicallyOpen() {
    return getOpenNess(1f) > 0;
  }

  public boolean isClientOpened() {
    return clientOpened;
  }

  @Override
  public void setClientOpened(boolean opened) {
    this.clientOpened = opened;
  }

  @Override
  public @NotNull BlockPos getInfoPos() {
    return getBlockPos();
  }

  @Override
  public ResourceKey<LootTable> getInfoLootTable() {
    return getLootTable();
  }

  @Override
  public @Nullable Component getInfoDisplayName() {
    return getDisplayName();
  }

  @SuppressWarnings("null")
@Override
  public @NotNull ResourceKey<Level> getInfoDimension() {
    return getLevel().dimension();
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
    return getLevel();
  }

  @Override
  public @Nullable IContainerTrigger getTrigger() {
    return LootrRegistry.getChestTrigger();
  }
}
