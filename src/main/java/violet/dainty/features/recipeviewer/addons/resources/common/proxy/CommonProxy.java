package violet.dainty.features.recipeviewer.addons.resources.common.proxy;

import violet.dainty.features.recipeviewer.addons.resources.common.compatibility.Compatibility;
import violet.dainty.features.recipeviewer.addons.resources.common.config.Settings;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.DungeonRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.EnchantmentRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.MobRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.PlantRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.VillagerRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.WorldGenRegistry;

public class CommonProxy {
    public void initCompatibility() {
        DungeonRegistry.getInstance().clear();
        MobRegistry.getInstance().clear();
        PlantRegistry.getInstance().clear();
        VillagerRegistry.getInstance().clear();
        WorldGenRegistry.getInstance().clear();
        Compatibility.init(); 
        EnchantmentRegistry.getInstance().removeAll(Settings.excludedEnchants);
    }
}
