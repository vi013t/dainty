package violet.dainty.features.recipeviewer.addons.resources.common.entry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.PlantDrop;
import violet.dainty.features.recipeviewer.addons.resources.common.util.MapKeys;
import violet.dainty.features.recipeviewer.addons.resources.common.util.PlantHelper;

public class PlantEntry {
    private BushBlock plant;
    private ItemStack plantStack;
    private Map<String, PlantDrop> drops = new LinkedHashMap<>();
    private int totalWeight = 0;
    private BlockState soil = null;
    private BlockState plantState = null;
    private Property<?> ageProperty = null;

    public static PlantEntry registerGrass() {
        List<PlantDrop> seeds = PlantHelper.getSeeds();
        PlantEntry grass = new PlantEntry(new ItemStack(Blocks.TALL_GRASS, 1), seeds.toArray(new PlantDrop[seeds.size()]));
        grass.totalWeight *= 8;
        return grass;
    }

    public BlockState getSoil() {
        return soil;
    }

    public BlockState getPlantState() {
        return plantState;
    }

    public Property<?> getAgeProperty() {
        return ageProperty;
    }

    public void setSoil(BlockState soil) {
        this.soil = soil;
    }

    public void setPlantState(BlockState plantState) {
        this.plantState = plantState;
    }

    public void setAgeProperty(Property<?> ageProperty) {
        this.ageProperty = ageProperty;
    }

    public PlantEntry(ItemStack itemStack, BushBlock plant, PlantDrop... drops) {
        this.plantStack = itemStack;
        this.plant = plant;
        for (PlantDrop entry : drops) {
            this.totalWeight += entry.getWeight();
            String key = MapKeys.getKey(entry.getDrop());
            if (key != null) this.drops.put(key, entry);
        }
    }

    public PlantEntry(ItemStack itemStack, PlantDrop... drops) {
        this(itemStack, null, drops);
    }

    public <T extends BushBlock> PlantEntry(T plant, PlantDrop... drops) {
        this(new ItemStack(plant), plant, drops);
    }

    public void add(PlantDrop entry) {
        String key = MapKeys.getKey(entry.getDrop());
        if (!this.drops.containsKey(key)) return;
        this.drops.put(key, new PlantDrop(entry.getDrop(), (this.totalWeight + entry.getWeight())));
    }

    public BushBlock getPlant() {
        return this.plant;
    }

    public ItemStack getPlantItemStack() {
        return this.plantStack;
    }

    public List<PlantDrop> getDrops() {
        return new ArrayList<>(this.drops.values());
    }

    public List<ItemStack> getLootDropStacks() {
        return getDrops().stream().map(PlantDrop::getDrop).collect(Collectors.toList());
    }

    public PlantDrop getDrop(ItemStack itemStack) {
        return this.drops.get(MapKeys.getKey(itemStack));
    }

    public int getTotalWeight() {
        return totalWeight;
    }
}
