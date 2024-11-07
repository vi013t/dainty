package violet.dainty.features.recipeviewer.addons.resources.common.entry;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.LootDrop;
import violet.dainty.features.recipeviewer.addons.resources.api.util.ItemHelper;
import violet.dainty.features.recipeviewer.addons.resources.api.util.LootFunctionHelper;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.ILootTableHelper;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.Services;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.DungeonRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.util.LootTableFetcher;
import violet.dainty.features.recipeviewer.addons.resources.common.util.LootTableHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;

public class DungeonEntry {
    private Set<LootDrop> drops;
    private String name;
    private int maxStacks, minStacks;

    public DungeonEntry(String name, LootTable lootTable) {
        this.drops = new TreeSet<>();
        this.name = name;
        final float[] tmpMinStacks = {0};
        final float[] tmpMaxStacks = {0};
        final LootTableFetcher lootTables = LootTableHelper.getLootTableFetcher();
        handleTable(lootTable, lootTables, tmpMinStacks, tmpMaxStacks);
        this.minStacks = Mth.floor(tmpMinStacks[0]);
        this.maxStacks = Mth.floor(tmpMaxStacks[0]);
    }

    private void handleTable(LootTable lootTable, LootTableFetcher lootTables, float[] tmpMinStacks, float[] tmpMaxStacks) {
        ILootTableHelper lootTableHelper = Services.PLATFORM.getLootTableHelper();
        for (LootPool pool : LootTableHelper.getPools(lootTable)) {
            tmpMinStacks[0] += LootFunctionHelper.getMin(lootTableHelper.getRolls(pool));
            tmpMaxStacks[0] += LootFunctionHelper.getMax(lootTableHelper.getRolls(pool)) + LootFunctionHelper.getMax(lootTableHelper.getBonusRolls(pool));
            final float totalWeight = LootTableHelper.getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootPoolSingletonContainer).map(entry -> (LootPoolSingletonContainer) entry)
                    .mapToInt(entry -> entry.weight).sum();
            LootTableHelper.getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootItem).map(entry -> (LootItem) entry)
                    .map(entry -> new LootDrop(entry.item.value(), entry.weight / totalWeight, entry.functions)).forEach(drops::add);

            LootTableHelper.getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof DynamicLoot).map(entry -> (DynamicLoot) entry)
                    .map(entry -> lootTables.getLootTable(ResourceKey.create(Registries.LOOT_TABLE, entry.name)))
                    .forEach(table -> handleTable(table, lootTables, tmpMinStacks, tmpMaxStacks));
        }
    }

    public boolean containsItem(ItemStack itemStack) {
        return drops.stream().anyMatch(drop -> drop.item.is(itemStack.getItem()));
    }

    public String getName() {
        String name = DungeonRegistry.categoryToLocalKeyMap.get(this.name);
        return name == null ? this.name : name;
    }

    public List<ItemStack> getItemStacks(IFocus<ItemStack> focus) {
        return drops.stream().map(drop -> drop.item)
            .filter(stack -> focus == null || ItemStack.isSameItem(ItemHelper.copyStackWithSize(stack, focus.getTypedValue().getIngredient().getCount()), focus.getTypedValue().getIngredient()))
            .collect(Collectors.toList());
    }

    public List<ItemStack> getItemStacks(Stream<IFocus<ItemStack>> focuses) {
        return getItemStacks(focuses.findFirst().orElse(null));
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public int getMinStacks() {
        return minStacks;
    }

    public LootDrop getChestDrop(ItemStack ingredient) {
        return drops.stream().filter(drop -> ItemStack.isSameItem(drop.item, ingredient)).findFirst().orElse(null);
    }

    public int amountOfItems(IFocus<ItemStack> focus) {
        return getItemStacks(focus).size();
    }

    public List<ItemStack> getItems(IFocus<ItemStack> focus, int slot, int slots) {
        List<ItemStack> list = getItemStacks(focus).subList(slot, slot + 1);
        for (int n = 1; n < (amountOfItems(focus) / slots) + 1; n++)
            list.add(this.amountOfItems(focus) <= slot + slots * n ? null : getItemStacks(focus).get(slot + slots * n));
        list.removeIf(Objects::isNull);
        return list;
    }

}
