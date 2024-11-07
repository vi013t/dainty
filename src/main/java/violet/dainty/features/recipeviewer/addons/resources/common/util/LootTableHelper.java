package violet.dainty.features.recipeviewer.addons.resources.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.LootDrop;
import violet.dainty.features.recipeviewer.addons.resources.common.compatibility.CompatBase;
import violet.dainty.features.recipeviewer.addons.resources.common.config.Settings;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.Services;

public class LootTableHelper {
    private static final Map<DyeColor, ResourceKey<LootTable>> sheepColors = new HashMap<>();

    static {
        sheepColors.put(DyeColor.WHITE,BuiltInLootTables.SHEEP_WHITE);
        sheepColors.put(DyeColor.ORANGE, BuiltInLootTables.SHEEP_ORANGE);
        sheepColors.put(DyeColor.MAGENTA, BuiltInLootTables.SHEEP_MAGENTA);
        sheepColors.put(DyeColor.LIGHT_BLUE, BuiltInLootTables.SHEEP_LIGHT_BLUE);
        sheepColors.put(DyeColor.YELLOW, BuiltInLootTables.SHEEP_YELLOW);
        sheepColors.put(DyeColor.LIME, BuiltInLootTables.SHEEP_LIME);
        sheepColors.put(DyeColor.PINK, BuiltInLootTables.SHEEP_PINK);
        sheepColors.put(DyeColor.GRAY, BuiltInLootTables.SHEEP_GRAY);
        sheepColors.put(DyeColor.LIGHT_GRAY, BuiltInLootTables.SHEEP_LIGHT_GRAY);
        sheepColors.put(DyeColor.CYAN, BuiltInLootTables.SHEEP_CYAN);
        sheepColors.put(DyeColor.PURPLE, BuiltInLootTables.SHEEP_PURPLE);
        sheepColors.put(DyeColor.BLUE, BuiltInLootTables.SHEEP_BLUE);
        sheepColors.put(DyeColor.BROWN, BuiltInLootTables.SHEEP_BROWN);
        sheepColors.put(DyeColor.GREEN, BuiltInLootTables.SHEEP_GREEN);
        sheepColors.put(DyeColor.RED, BuiltInLootTables.SHEEP_RED);
        sheepColors.put(DyeColor.BLACK, BuiltInLootTables.SHEEP_BLACK);
    }

    public static List<LootPool> getPools(LootTable table) {
        return Services.PLATFORM.getLootTableHelper().getPools(table);  
    }

    public static List<LootPoolEntryContainer> getLootEntries(LootPool pool) {
        return Services.PLATFORM.getLootTableHelper().getLootEntries(pool);
    }

    public static List<LootItemCondition> getLootConditions(LootPool pool) {
        return Services.PLATFORM.getLootTableHelper().getLootConditions(pool);
    }

    public static List<LootDrop> toDrops(LootTable table) {
        List<LootDrop> drops = new ArrayList<>();

        final LootTableFetcher lootTableFetcher = getLootTableFetcher();

        getPools(table).forEach(
            pool -> {
                final float totalWeight = getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootPoolSingletonContainer).map(entry -> (LootPoolSingletonContainer) entry)
                    .mapToInt(entry -> entry.weight).sum();
                final List<LootItemCondition> poolConditions = getLootConditions(pool);
                getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootItem).map(entry -> (LootItem) entry)
                    .map(entry -> new LootDrop(entry.item.value(), entry.weight / totalWeight, entry.conditions, entry.functions))
                    .map(drop -> drop.addLootConditions(poolConditions))
                    .forEach(drops::add);

                getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof DynamicLoot).map(entry -> (DynamicLoot) entry)
                    .map(entry -> toDrops(lootTableFetcher.getLootTable(ResourceKey.create(Registries.LOOT_TABLE, entry.name)))).forEach(drops::addAll);
            }
        );

        drops.removeIf(Objects::isNull);
        return drops;
    }

    public static List<LootDrop> toDrops(ResourceKey<LootTable> lootTableKey) {
        return toDrops(getLootTableFetcher().getLootTable(lootTableKey));
    }

    public static List<ResourceKey<LootTable>> getAllChestLootTablesResourceKeys() {
        ArrayList<ResourceKey<LootTable>> chestTables = new ArrayList<>();

        chestTables.add(BuiltInLootTables.END_CITY_TREASURE);
        chestTables.add(BuiltInLootTables.SIMPLE_DUNGEON);
        chestTables.add(BuiltInLootTables.VILLAGE_WEAPONSMITH);
        chestTables.add(BuiltInLootTables.VILLAGE_TOOLSMITH);
        chestTables.add(BuiltInLootTables.VILLAGE_ARMORER);
        chestTables.add(BuiltInLootTables.VILLAGE_CARTOGRAPHER);
        chestTables.add(BuiltInLootTables.VILLAGE_MASON);
        chestTables.add(BuiltInLootTables.VILLAGE_SHEPHERD);
        chestTables.add(BuiltInLootTables.VILLAGE_BUTCHER);
        chestTables.add(BuiltInLootTables.VILLAGE_FLETCHER);
        chestTables.add(BuiltInLootTables.VILLAGE_FISHER);
        chestTables.add(BuiltInLootTables.VILLAGE_TANNERY);
        chestTables.add(BuiltInLootTables.VILLAGE_TEMPLE);
        chestTables.add(BuiltInLootTables.VILLAGE_DESERT_HOUSE);
        chestTables.add(BuiltInLootTables.VILLAGE_PLAINS_HOUSE);
        chestTables.add(BuiltInLootTables.VILLAGE_TAIGA_HOUSE);
        chestTables.add(BuiltInLootTables.VILLAGE_SNOWY_HOUSE);
        chestTables.add(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE);
        chestTables.add(BuiltInLootTables.ABANDONED_MINESHAFT);
        chestTables.add(BuiltInLootTables.NETHER_BRIDGE);
        chestTables.add(BuiltInLootTables.STRONGHOLD_LIBRARY);
        chestTables.add(BuiltInLootTables.STRONGHOLD_CROSSING);
        chestTables.add(BuiltInLootTables.STRONGHOLD_CORRIDOR);
        chestTables.add(BuiltInLootTables.DESERT_PYRAMID);
        chestTables.add(BuiltInLootTables.JUNGLE_TEMPLE);
        chestTables.add(BuiltInLootTables.IGLOO_CHEST);
        chestTables.add(BuiltInLootTables.WOODLAND_MANSION);
        chestTables.add(BuiltInLootTables.UNDERWATER_RUIN_SMALL);
        chestTables.add(BuiltInLootTables.UNDERWATER_RUIN_BIG);
        chestTables.add(BuiltInLootTables.BURIED_TREASURE);
        chestTables.add(BuiltInLootTables.SHIPWRECK_MAP);
        chestTables.add(BuiltInLootTables.SHIPWRECK_SUPPLY);
        chestTables.add(BuiltInLootTables.SHIPWRECK_TREASURE);
        chestTables.add(BuiltInLootTables.PILLAGER_OUTPOST);
        chestTables.add(BuiltInLootTables.BASTION_TREASURE);
        chestTables.add(BuiltInLootTables.BASTION_OTHER);
        chestTables.add(BuiltInLootTables.BASTION_BRIDGE);
        chestTables.add(BuiltInLootTables.BASTION_HOGLIN_STABLE);
        chestTables.add(BuiltInLootTables.RUINED_PORTAL);
        chestTables.add(BuiltInLootTables.ANCIENT_CITY);
        chestTables.add(BuiltInLootTables.ANCIENT_CITY_ICE_BOX);

        return chestTables;
    }

    public static Map<ResourceKey<LootTable>, Supplier<LivingEntity>> getAllMobLootTables() {
        MobTableBuilder mobTableBuilder = new MobTableBuilder();

        for (Map.Entry<DyeColor, ResourceKey<LootTable>> entry : sheepColors.entrySet()) {
            ResourceKey<LootTable> lootTableList = entry.getValue();
            DyeColor dyeColor = entry.getKey();
            mobTableBuilder.addSheep(lootTableList, EntityType.SHEEP, dyeColor);
        }

        for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
            if (entityType.getCategory() != MobCategory.MISC && entityType != EntityType.SHEEP) {
                mobTableBuilder.add(entityType.getDefaultLootTable(), entityType);
            }
        }

        return mobTableBuilder.getMobTables();
    }

    private static LootTableFetcher lootTableFetcher;

    public static LootTableFetcher getLootTableFetcher() {
        Level level = CompatBase.getServerLevel().orElseGet(CompatBase::getLevel);
        if (level.getServer() == null) {
            if (lootTableFetcher == null) {
                lootTableFetcher = new LootTableFetcher();

                if(Settings.disableLootManagerReloading) {
                    return lootTableFetcher;
                }


//                ReloadableResourceManager reloadableResourceManager = new ReloadableResourceManager(PackType.SERVER_DATA);
//                List<PackResources> packs = new LinkedList<>();
//                packs.add(new ServerPacksSource(null).getVanillaPack());
//                Services.PLATFORM.getModsList().getMods().forEach(mod -> packs.addAll(mod.getPackResources()));
//                reloadableResourceManager.registerReloadListener(lootTableFetcher);
//                ReloadInstance reloadInstance = reloadableResourceManager.createReload(Util.backgroundExecutor(), Minecraft.getInstance(), CompletableFuture.completedFuture(Unit.INSTANCE), packs);
//                Minecraft.getInstance().managedBlock(reloadInstance::isDone);
            }
            return lootTableFetcher;
        }
        return new LootTableFetcher(level.getServer().reloadableRegistries());
    }
}
