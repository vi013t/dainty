package violet.dainty.features.playerspecificloot.api.registry;

import net.minecraft.stats.Stat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import violet.dainty.features.playerspecificloot.api.advancement.IAdvancementTrigger;
import violet.dainty.features.playerspecificloot.api.advancement.IContainerTrigger;
import violet.dainty.features.playerspecificloot.api.advancement.ILootedStatTrigger;

public interface ILootrRegistry {
  Block getBarrelBlock();

  Block getChestBlock();

  Block getTrappedChestBlock();

  Block getInventoryBlock();

  Block getTrophyBlock();

  Block getShulker();

  Item getBarrelItem();

  Item getChestItem();

  Item getTrappedChestItem();

  Item getInventoryItem();

  Item getTrophyItem();

  Item getShulkerItem();

  EntityType<?> getMinecart();

  BlockEntityType<?> getBarrelBlockEntity();

  BlockEntityType<? extends ChestBlockEntity> getChestBlockEntity();

  BlockEntityType<? extends ChestBlockEntity> getTrappedChestBlockEntity();

  BlockEntityType<? extends ChestBlockEntity> getInventoryBlockEntity();

  BlockEntityType<?> getShulkerBlockEntity();

  // TODO: Hm
  IAdvancementTrigger getAdvancementTrigger();

  IContainerTrigger getChestTrigger();

  IContainerTrigger getBarrelTrigger();

  IContainerTrigger getCartTrigger();

  IContainerTrigger getShulkerTrigger();

  ILootedStatTrigger getStatTrigger();

  LootItemConditionType getLootCount();

  Stat<?> getLootedStat();

  CreativeModeTab getTab();
}
