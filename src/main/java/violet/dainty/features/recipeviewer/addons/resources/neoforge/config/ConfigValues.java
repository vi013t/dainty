package violet.dainty.features.recipeviewer.addons.resources.neoforge.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.neoforged.neoforge.common.ModConfigSpec;
import violet.dainty.features.recipeviewer.addons.resources.common.config.Settings;

public class ConfigValues {

    public static ModConfigSpec.IntValue itemsPerColumn;
    public static ModConfigSpec.IntValue itemsPerRow;
    public static ModConfigSpec.BooleanValue diyData;
    public static ModConfigSpec.BooleanValue showDevData;
    public static ModConfigSpec.ConfigValue<List<? extends String>> enchantsBlacklist;
    public static ModConfigSpec.ConfigValue<List<? extends String>> hiddenTabs;
    public static ModConfigSpec.ConfigValue<List<? extends Integer>> dimensionsBlacklist;
    public static ModConfigSpec.BooleanValue disableLootManagerReloading;

    public static ModConfigSpec build() {
        ModConfigSpec.Builder builder =  new ModConfigSpec.Builder();

        itemsPerColumn = builder.defineInRange("itemsPerColumn", 4, 1, 4);
        itemsPerRow = builder.defineInRange("itemsPerRow", 4, 1, 4);
        diyData = builder.worldRestart().define("diyData", true);
        showDevData = builder.worldRestart().define("showDevData", false);
        enchantsBlacklist = builder.worldRestart().defineListAllowEmpty("enchantsBlacklist", List.of("flimflam", "soulBound"), () -> "", new TypePredicate(String.class));
        hiddenTabs = builder.worldRestart().defineListAllowEmpty("hiddenTabs", new ArrayList<>(), () -> "", new TypePredicate(String.class));
        dimensionsBlacklist = builder.worldRestart().defineListAllowEmpty("dimensionsBlacklist", List.of(-11), () -> 100, new TypePredicate(Integer.class));
        disableLootManagerReloading = builder.worldRestart().define("disableLootManagerReloading", false);

        return builder.build();
    }

    public static void pushChanges() {
        Settings.ITEMS_PER_COLUMN = itemsPerColumn.get();
        Settings.ITEMS_PER_ROW = itemsPerRow.get();
        Settings.useDIYdata = diyData.get();
        Settings.showDevData = showDevData.get();
        Settings.disableLootManagerReloading = disableLootManagerReloading.get();
        Settings.excludedEnchants = enchantsBlacklist.get().toArray(new String[0]);
        Settings.hiddenCategories = hiddenTabs.get().toArray(new String[0]);
        Settings.excludedDimensions = new ArrayList<>(dimensionsBlacklist.get());
        Settings.reload();
    }

    private static class TypePredicate implements Predicate<Object> {

        private Class type;

        public TypePredicate(Class type) {
            this.type = type;
        }

        @Override
        public boolean test(Object o) {
            return type.isInstance(o);
        }
    }
}
