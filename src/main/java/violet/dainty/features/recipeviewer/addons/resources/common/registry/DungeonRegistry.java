package violet.dainty.features.recipeviewer.addons.resources.common.registry;  

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import violet.dainty.features.recipeviewer.addons.resources.common.entry.DungeonEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.util.TranslationHelper;

public class DungeonRegistry {
    private Map<String, DungeonEntry> registry;
    public static Map<String, String> categoryToLocalKeyMap = new LinkedHashMap<>();
    private static DungeonRegistry instance;

    public static DungeonRegistry getInstance() {
        if (instance == null)
            return instance = new DungeonRegistry();
        return instance;
    }

    public DungeonRegistry() {
        registry = new LinkedHashMap<>();
        addCategoryMapping("chests/abandoned_mineshaft", "dainty.dungeon.abandonedMineshaftChest");
        addCategoryMapping("chests/desert_pyramid", "dainty.dungeon.desertPyramidChest");
        addCategoryMapping("chests/jungle_temple", "dainty.dungeon.pyramidJungleChest");
        addCategoryMapping("chests/igloo_chest", "dainty.dungeon.iglooChest");
        addCategoryMapping("chests/stronghold_corridor", "dainty.dungeon.strongholdCorridorChest");
        addCategoryMapping("chests/stronghold_library", "dainty.dungeon.strongholdLibraryChest");
        addCategoryMapping("chests/stronghold_crossing", "dainty.dungeon.strongholdCrossingChest");
        addCategoryMapping("chests/village_blacksmith", "dainty.dungeon.villageBlacksmithChest");
        addCategoryMapping("chests/spawn_bonus_chest", "dainty.dungeon.spawnBonusChest");
        addCategoryMapping("chests/simple_dungeon", "dainty.dungeon.simpleDungeonChest");
        addCategoryMapping("chests/nether_bridge", "dainty.dungeon.netherBridgeChest");
        addCategoryMapping("chests/end_city_treasure", "dainty.dungeon.endCityTreasureChest");
        addCategoryMapping("chests/woodland_mansion", "dainty.dungeon.woodlandMansion");
        addCategoryMapping("chests/village/village_weaponsmith", "dainty.dungeon.village_weaponsmith");
        addCategoryMapping("chests/village/village_toolsmith", "dainty.dungeon.village_toolsmith");
        addCategoryMapping("chests/village/village_armorer", "dainty.dungeon.village_armorer");
        addCategoryMapping("chests/village/village_cartographer", "dainty.dungeon.village_cartographer");
        addCategoryMapping("chests/village/village_mason", "dainty.dungeon.village_mason");
        addCategoryMapping("chests/village/village_shepherd", "dainty.dungeon.village_shepherd");
        addCategoryMapping("chests/village/village_butcher", "dainty.dungeon.village_butcher");
        addCategoryMapping("chests/village/village_fletcher", "dainty.dungeon.village_fletcher");
        addCategoryMapping("chests/village/village_fisher", "dainty.dungeon.village_fisher");
        addCategoryMapping("chests/village/village_tannery", "dainty.dungeon.village_tannery");
        addCategoryMapping("chests/village/village_temple", "dainty.dungeon.village_temple");
        addCategoryMapping("chests/village/village_desert_house", "dainty.dungeon.village_desert_house");
        addCategoryMapping("chests/village/village_plains_house", "dainty.dungeon.village_plains_house");
        addCategoryMapping("chests/village/village_taiga_house", "dainty.dungeon.village_taiga_house");
        addCategoryMapping("chests/village/village_snowy_house", "dainty.dungeon.village_snowy_house");
        addCategoryMapping("chests/village/village_savanna_house", "dainty.dungeon.village_savanna_house");
        addCategoryMapping("chests/underwater_ruin_small", "dainty.dungeon.underwater_ruin_small");
        addCategoryMapping("chests/underwater_ruin_big", "dainty.dungeon.underwater_ruin_big");
        addCategoryMapping("chests/buried_treasure", "dainty.dungeon.buried_treasure");
        addCategoryMapping("chests/shipwreck_map", "dainty.dungeon.shipwreck_map");
        addCategoryMapping("chests/shipwreck_supply", "dainty.dungeon.shipwreck_supply");
        addCategoryMapping("chests/shipwreck_treasure", "dainty.dungeon.shipwreck_treasure");
        addCategoryMapping("chests/pillager_outpost", "dainty.dungeon.pillager_outpost");
        addCategoryMapping("chests/bastion_treasure", "dainty.dungeon.bastion_treasure");
        addCategoryMapping("chests/bastion_other", "dainty.dungeon.bastion_other");
        addCategoryMapping("chests/bastion_bridge", "dainty.dungeon.bastion_bridge");
        addCategoryMapping("chests/bastion_hoglin_stable", "dainty.dungeon.bastion_hoglin_stable");
        addCategoryMapping("chests/ruined_portal", "dainty.dungeon.ruined_portal");
        addCategoryMapping("chests/ancient_city", "dainty.dungeon.ancient_city");
        addCategoryMapping("chests/ancient_city_ice_box", "dainty.dungeon.ancient_city_ice_box");
    }

    public static boolean addCategoryMapping(String category, String name) {
        if (!categoryToLocalKeyMap.containsKey(category)) {
            categoryToLocalKeyMap.put(category, name);
            return true;
        }
        return false;
    }

    public void registerDungeonEntry(DungeonEntry entry) {
        if (entry == null) return;
        String name = entry.getName();
        if (registry.containsKey(name)) return;
        registry.put(name, entry);
    }

    public List<DungeonEntry> getDungeons() {
        return new ArrayList<>(registry.values());
    }

    public String getNumStacks(DungeonEntry entry) {
        int max = entry.getMaxStacks();
        int min = entry.getMinStacks();
        if (min == max) return TranslationHelper.translateAndFormat("dainty.stacks", max);
        return TranslationHelper.translateAndFormat("dainty.stacks", min + " - " + max);
    }

    public void clear() {
        registry.clear();
    }
}
