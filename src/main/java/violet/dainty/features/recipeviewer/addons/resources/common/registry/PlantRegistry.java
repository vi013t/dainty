package violet.dainty.features.recipeviewer.addons.resources.common.registry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.PlantDrop;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.PlantEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.util.MapKeys;

public class PlantRegistry {
    private Map<String, PlantEntry> registry;
    private static PlantRegistry instance;

    public static PlantRegistry getInstance() {
        if (instance == null)
            return instance = new PlantRegistry();
        return instance;
    }

    public PlantRegistry() {
        registry = new LinkedHashMap<>();
        registerPlant(PlantEntry.registerGrass());
    }

    public boolean registerPlant(PlantEntry entry) {
        String key = MapKeys.getKey(entry.getPlantItemStack());
        if (key == null || contains(key)) return false;
        registry.put(key, entry);
        return true;
    }

    private boolean contains(String key) {
        return registry.containsKey(key);
    }

    public List<PlantEntry> getAllPlants() {
        return new ArrayList<>(registry.values());
    }

    public void addDrops(ItemStack itemStack, PlantDrop[] drops) {
        String key = MapKeys.getKey(itemStack);
        if (key == null || contains(key))
            for (PlantDrop drop : drops)
                registry.get(key).add(drop);
    }

    public void clear() {
        registry.clear();
    }
}
