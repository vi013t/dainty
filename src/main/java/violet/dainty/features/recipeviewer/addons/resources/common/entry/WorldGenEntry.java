package violet.dainty.features.recipeviewer.addons.resources.common.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.addons.resources.api.distributions.DistributionBase;
import violet.dainty.features.recipeviewer.addons.resources.api.distributions.DistributionHelpers;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.LootDrop;
import violet.dainty.features.recipeviewer.addons.resources.api.render.ColorHelper;
import violet.dainty.features.recipeviewer.addons.resources.api.restrictions.Restriction;
import violet.dainty.features.recipeviewer.addons.resources.common.util.MapKeys;

public class WorldGenEntry {
    private float[] chances;
    private boolean silktouch;
    private ItemStack block;

    private ItemStack deepSlateBlock;
    private int minY;
    private int maxY;
    private int colour;
    private Restriction restriction;
    private DistributionBase distribution;
    private Map<String, Set<LootDrop>> drops;
    private Map<Item, Set<LootDrop>> wildcardDrops;
    private Map<String, ItemStack> dropsDisplay;

    public WorldGenEntry(ItemStack block, ItemStack deepSlateBlock, DistributionBase distribution, Restriction restriction, boolean silktouch, LootDrop... drops) {
        this.block = block;
        this.deepSlateBlock = deepSlateBlock;
        this.distribution = distribution;
        this.restriction = restriction;
        this.colour = ColorHelper.BLACK;
        this.silktouch = silktouch;
        this.drops = new HashMap<>();
        this.wildcardDrops = new HashMap<>();
        this.dropsDisplay = new HashMap<>();
        addDrops(drops);
        calcChances();
    }

    public WorldGenEntry(ItemStack block, DistributionBase distribution, Restriction restriction, boolean silktouch, LootDrop... drops) {
        this(block, null, distribution, restriction, silktouch, drops);
    }

    public WorldGenEntry(ItemStack block, DistributionBase distribution, LootDrop... drops) {
        this(block, distribution, Restriction.OVERWORLD, false, drops);
    }

    public WorldGenEntry(ItemStack block, ItemStack deepSlateBlock, DistributionBase distribution, LootDrop... drops) {
        this(block, deepSlateBlock, distribution, Restriction.OVERWORLD, false, drops);
    }

    public WorldGenEntry(ItemStack block, DistributionBase distribution, boolean silktouch, LootDrop... drops) {
        this(block, distribution, Restriction.OVERWORLD, silktouch, drops);
    }

    public WorldGenEntry(ItemStack block, ItemStack deepSlateBlock, DistributionBase distribution, boolean silktouch, LootDrop... drops) {
        this(block, deepSlateBlock, distribution, Restriction.OVERWORLD, silktouch, drops);
    }

    public WorldGenEntry(ItemStack block, DistributionBase distribution, Restriction restriction, LootDrop... drops) {
        this(block, distribution, restriction, false, drops);
    }

    public WorldGenEntry(ItemStack block, ItemStack deepSlateBlock, DistributionBase distribution, Restriction restriction, LootDrop... drops) {
        this(block, deepSlateBlock, distribution, restriction, false, drops);
    }

    public void addDrops(LootDrop... drops) {
        for (LootDrop drop : drops) {
            String mapKey = MapKeys.getKey(drop.item);
            if (mapKey == null) continue;
            Set<LootDrop> dropSet = this.drops.get(mapKey);
            if (dropSet == null) dropSet = new TreeSet<>();
            dropSet.add(drop);
            this.drops.put(mapKey, dropSet);
            /*if (drop.item.getMetadata() == OreDictionary.WILDCARD_VALUE) {
                Set<LootDrop> wildcardDropSet = this.wildcardDrops.get(drop.item.getItem());
                if (wildcardDropSet == null) wildcardDropSet = new TreeSet<>();
                wildcardDropSet.add(drop);
                this.wildcardDrops.put(drop.item.getItem(), wildcardDropSet);
            }*/
            if (!this.dropsDisplay.containsKey(mapKey)) {
                ItemStack itemStack = drop.item.copy();
                itemStack.setCount(Math.max(1, drop.minDrop));
                this.dropsDisplay.put(mapKey, itemStack);
            }
        }
    }

    public void addDrops(Collection<LootDrop> drops) {
        addDrops(drops.toArray(new LootDrop[drops.size()]));
    }

    private void calcChances() {
        chances = new float[256 + 64];
        minY = 256 + 64;
        maxY = 0;
        int i = -1;
        for (float chance : this.distribution.getDistribution()) {
            if (++i == chances.length) break;
            chances[i] += chance;
            if (chances[i] > 0) {
                if (minY > i)
                    minY = i;
                if (i > maxY)
                    maxY = i;
            }
        }
        if (minY == 256 + 64) minY = 0;
        if (maxY == 0) maxY = 255 + 64;

        if (minY < 128 + 32)
            minY = 0;
        else
            minY = 128 + 32;

        if (maxY <= 127 + 32)
            maxY = 127 + 32;
        else
            maxY = 255 + 32;
        minY -= 64;
    }

    public float[] getChances() {
        return Arrays.copyOfRange(chances, minY + 64, maxY + 1 + 64);
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public boolean isSilkTouchNeeded() {
        return silktouch;
    }

    public int getColour() {
        return colour;
    }

    public List<ItemStack> getDrops() {
        return new ArrayList<>(this.dropsDisplay.values());
    }

    public List<ItemStack> getBlockAndDrops() {
        List<ItemStack> list = new LinkedList<>();
        list.add(this.block);
        list.addAll(getDrops());
        return list;
    }

    public ItemStack getBlock() {
        return this.block;
    }

    public ItemStack getDeepSlateBlock() {
        return deepSlateBlock;
    }

    public List<String> getBiomeRestrictions() {
        return this.restriction.getBiomeRestrictions();
    }

    public String getDimension() {
        return this.restriction.getDimensionRestriction();
    }

    public List<LootDrop> getLootDrops(ItemStack itemStack) {
        String key = MapKeys.getKey(itemStack);
        List<LootDrop> list = new ArrayList<>(this.drops.containsKey(key) ? this.drops.get(key) : this.wildcardDrops.get(itemStack.getItem()));
        Collections.reverse(list);
        return list;
    }

    public float getAverageBlockCountPerChunk() {
        float sum = 0;
        for (float chance : this.chances) {
            sum += chance;
        }
        return Math.round(sum * chances.length * 100) / 100F;
    }

    @Override
    public String toString() {
        return "WorldGenEntry: " + block.getDisplayName() + " - " + restriction.toString();
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public void merge(WorldGenEntry entry) {
        entry.drops.values().forEach(this::addDrops);
        this.distribution = DistributionHelpers.addDistribution(this.distribution, entry.distribution);
        calcChances();
    }

    public boolean hasDeepSlateVariant() {
        return this.deepSlateBlock != null && !this.deepSlateBlock.isEmpty();
    }

    public List<ItemStack> getBlocks() {
        List<ItemStack> blocks = new LinkedList<>();
        blocks.add(getBlock());
        if (hasDeepSlateVariant()) {
            blocks.add(getDeepSlateBlock());
        }
        return blocks;
    }
}
