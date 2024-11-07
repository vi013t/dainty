package violet.dainty.features.recipeviewer.addons.resources.common.compatibility.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.storage.loot.LootTable;
import violet.dainty.features.recipeviewer.addons.resources.api.IDungeonRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.DungeonEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.DungeonRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.util.LogHelper;
import violet.dainty.features.recipeviewer.addons.resources.common.util.LootTableFetcher;
import violet.dainty.features.recipeviewer.addons.resources.common.util.LootTableHelper;

public class DungeonRegistryImpl implements IDungeonRegistry {
    private static List<Tuple<String, String>> categoryMapping = new LinkedList<>();
    private static Map<String, ResourceKey<LootTable>> rawRegisters = new HashMap<>();
    private static List<DungeonEntry> preppedRegisters = new LinkedList<>();

    protected DungeonRegistryImpl() {

    }

    @Override
    public void registerCategory(@NotNull String category, @NotNull String localization) {
        categoryMapping.add(new Tuple<>(category, localization));
    }

    @Override
    public void registerChest(@NotNull String category, @NotNull ResourceKey<LootTable> tableLocation) {
        rawRegisters.put(category, tableLocation);
    }

    @Override
    public void registerChest(@NotNull String category, @NotNull LootTable lootTable) {
        try {
            preppedRegisters.add(new DungeonEntry(category, lootTable));
        } catch (Exception e) {
            LogHelper.debug("Bad dungeon chest registry for category %s", category);
        }
    }

    protected static void commit() {
        categoryMapping.forEach(t -> DungeonRegistry.addCategoryMapping(t.getA(), t.getB()));
        preppedRegisters.forEach(entry -> DungeonRegistry.getInstance().registerDungeonEntry(entry));
        LootTableFetcher lootTableFetcher = LootTableHelper.getLootTableFetcher();
        rawRegisters.entrySet().stream()
            .map(entry -> {
                try {
                    return new DungeonEntry(entry.getKey(), lootTableFetcher.getLootTable(entry.getValue()));
                } catch (Exception e) {
                    LogHelper.debug("Bad dungeon chest registry for category %s", entry.getKey());
                    return null;
                }
            }).forEach(entry -> DungeonRegistry.getInstance().registerDungeonEntry(entry));
    }
}
