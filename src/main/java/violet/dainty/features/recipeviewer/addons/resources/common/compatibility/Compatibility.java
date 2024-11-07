package violet.dainty.features.recipeviewer.addons.resources.common.compatibility;

import violet.dainty.features.recipeviewer.addons.resources.common.compatibility.api.JERAPI;
import violet.dainty.features.recipeviewer.addons.resources.common.compatibility.minecraft.MinecraftCompat;
import violet.dainty.features.recipeviewer.addons.resources.common.config.Settings;
import violet.dainty.features.recipeviewer.addons.resources.common.json.WorldGenAdapter;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.VillagerRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.util.LogHelper;
import violet.dainty.features.recipeviewer.addons.resources.common.util.VillagersHelper;

public class Compatibility {
    public static void init() {
        boolean initWorldGen = true;

        try {
            if (Settings.useDIYdata) { 
                if (WorldGenAdapter.hasWorldGenDIYData()) {
                    WorldGenAdapter.readDIYData();
                    initWorldGen = false;
                }
            }
        } catch (Exception e) {
            LogHelper.warn("Error during loading of DIY data", e);
        }

        try {
            new MinecraftCompat().init(initWorldGen);
        } catch (Exception e) {
            LogHelper.warn("Error during loading of default minecraft compat", e);
        }

        // Protection is implemented at VillagerEntry creation
        VillagersHelper.initRegistry(VillagerRegistry.getInstance());

        JERAPI.commit(initWorldGen);
    }
}
