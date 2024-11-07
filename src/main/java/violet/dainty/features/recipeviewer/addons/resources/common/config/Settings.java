package violet.dainty.features.recipeviewer.addons.resources.common.config;

import java.util.List;

import violet.dainty.features.recipeviewer.addons.resources.common.jei.JEIConfig;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.dungeon.DungeonCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.Services;

public final class Settings {
    public static int ITEMS_PER_ROW = 4;
    public static int ITEMS_PER_COLUMN = 4;

    public static boolean useDIYdata = true;

    public static String[] excludedEnchants = new String[] {"flimflam", "soulBound"};
    public static String[] hiddenCategories = new String[0];
    public static boolean showDevData = false;
    public static boolean disableLootManagerReloading = false;
    public static List<Integer> excludedDimensions = List.of(-11);

    public static void reload() {
        if (Services.PLATFORM.isClient()) {
            DungeonCategory.reloadSettings();
        }
        JEIConfig.resetCategories();
        JEIConfig.hideCategories(hiddenCategories);
    }
}
