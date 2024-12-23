package violet.dainty.features.playerspecificloot.api.data.blockentity;

import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.data.ILootrSavedData;
import violet.dainty.features.playerspecificloot.api.data.LootrBlockType;

public record RandomizableContainerBlockEntityLootrInfoProvider(
    @NotNull RandomizableContainerBlockEntity blockEntity, UUID id,
    NonNullList<ItemStack> customInventory) implements ILootrBlockEntity {

  @Override
  public LootrBlockType getInfoBlockType() {
    if (blockEntity instanceof BarrelBlockEntity) {
      return LootrBlockType.BARREL;
    } else if (blockEntity instanceof TrappedChestBlockEntity) {
      return LootrBlockType.TRAPPED_CHEST;
    } else if (blockEntity instanceof ShulkerBoxBlockEntity) {
      return LootrBlockType.SHULKER;
    } else {
      return LootrBlockType.CHEST;
    }
  }

  @Override
  public LootrInfoType getInfoType() {
    return LootrInfoType.CONTAINER_BLOCK_ENTITY;
  }

  @Override
  public @NotNull UUID getInfoUUID() {
    return id();
  }

  @Override
  public boolean hasBeenOpened() {
    return false;
  }

  @Override
  public boolean isPhysicallyOpen() {
    return false;
  }

  @Override
  public @NotNull BlockPos getInfoPos() {
    return blockEntity.getBlockPos();
  }

  // TODO: Can return null
  @Override
  public @NotNull ResourceKey<LootTable> getInfoLootTable() {
    return blockEntity.getLootTable();
  }

  @Override
  public @Nullable Component getInfoDisplayName() {
    return blockEntity.getDisplayName();
  }

  @SuppressWarnings("null")
@Override
  public @NotNull ResourceKey<Level> getInfoDimension() {
    return blockEntity.getLevel().dimension();
  }

  @Override
  public int getInfoContainerSize() {
    return blockEntity.getContainerSize();
  }

  @Override
  public long getInfoLootSeed() {
    return blockEntity.getLootTableSeed();
  }

  @Override
  public @Nullable NonNullList<ItemStack> getInfoReferenceInventory() {
    return customInventory();
  }

  @Override
  public boolean isInfoReferenceInventory() {
    return false;
  }

  @Override
  public Level getInfoLevel() {
    return blockEntity.getLevel();
  }

  @Override
  public Container getInfoContainer() {
    return blockEntity;
  }

  @Override
  public void markChanged() {
    blockEntity.setChanged();
  }

  @Override
  public void markDataChanged() {
    ILootrSavedData data = LootrAPI.getData(this);
    if (data != null) {
      data.markChanged();
    }
  }

  @Override
  public @Nullable Set<UUID> getClientOpeners() {
    return null;
  }

  @Override
  public boolean isClientOpened() {
    return false;
  }

  @Override
  public void setClientOpened(boolean opened) {
  }
}
