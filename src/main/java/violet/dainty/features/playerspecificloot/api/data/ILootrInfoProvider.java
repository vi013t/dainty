package violet.dainty.features.playerspecificloot.api.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import violet.dainty.features.playerspecificloot.api.IClientOpeners;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.advancement.IContainerTrigger;
import violet.dainty.features.playerspecificloot.api.data.blockentity.RandomizableContainerBlockEntityLootrInfoProvider;
import violet.dainty.features.playerspecificloot.api.data.entity.AbstractMinecartContainerLootrInfoProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public interface ILootrInfoProvider extends ILootrInfo, IClientOpeners {
  static ILootrInfoProvider of(BlockPos pos, Level level) {
    if (level.isClientSide()) {
      return null;
    }
    if (level.getBlockEntity(pos) instanceof ILootrInfoProvider provider) {
      return provider;
    }
    return null;
  }

  static ILootrInfoProvider of(RandomizableContainerBlockEntity blockEntity, UUID id) {
    if (blockEntity instanceof ILootrInfoProvider provider) {
      return provider;
    }
    return new RandomizableContainerBlockEntityLootrInfoProvider(blockEntity, id, null);
  }

  static ILootrInfoProvider of(RandomizableContainerBlockEntity blockEntity, UUID id, NonNullList<ItemStack> customInventory) {
    if (blockEntity instanceof ILootrInfoProvider provider) {
      return provider;
    }
    return new RandomizableContainerBlockEntityLootrInfoProvider(blockEntity, id, customInventory);
  }

  static ILootrInfoProvider of(AbstractMinecartContainer minecart) {
    if (minecart instanceof ILootrInfoProvider provider) {
      return provider;
    }
    return new AbstractMinecartContainerLootrInfoProvider(minecart);
  }

  static ILootrInfoProvider of(UUID id, BlockPos pos, int containerSize, ResourceKey<LootTable> lootTable, long lootSeed, Component displayName, ResourceKey<Level> dimension, NonNullList<ItemStack> customInventory, LootrInfoType type, LootrBlockType blockType) {
    return new CustomLootrInfoProvider(id, pos, containerSize, lootTable, lootSeed, displayName, dimension, customInventory, type, blockType);
  }

  @Override
  default Set<UUID> getVisualOpeners() {
    ILootrSavedData data = LootrAPI.getData(this);
    if (data != null) {
      return data.getVisualOpeners();
    }
    return null;
  }

  @Override
  default Set<UUID> getActualOpeners() {
    ILootrSavedData data = LootrAPI.getData(this);
    if (data != null) {
      return data.getActualOpeners();
    }
    return null;
  }

  @Override
  default boolean hasBeenOpened () {
    ILootrSavedData data = LootrAPI.getData(this);
    if (data == null) {
      return false;
    }

    return data.hasBeenOpened();
  }

  @Nullable
  default IContainerTrigger getTrigger () {
    return null;
  }

  default void performTrigger (ServerPlayer player) {
    IContainerTrigger trigger = getTrigger();
    if (trigger != null) {
      trigger.trigger(player, getInfoUUID());
    }
  }

  default void performOpen(ServerPlayer player) {
  }

  default void performOpen () {
  }

  default void performClose(ServerPlayer player) {
  }

  default void performClose () {
  }

  default void performDecay () {
  }

  default void performRefresh () {
    ILootrSavedData data = LootrAPI.getData(this);
    if (data != null) {
      data.refresh();
      data.clearOpeners();
      markChanged();
    }
  }

  default void performUpdate (ServerPlayer player) {
  }

  default void performUpdate () {
  }

  @Override
  default void markDataChanged() {
    ILootrSavedData data = LootrAPI.getData(this);
    if (data != null) {
      data.markChanged();
    }
  }
}
