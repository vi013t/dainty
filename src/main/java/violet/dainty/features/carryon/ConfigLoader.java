package violet.dainty.features.carryon;

import net.minecraft.core.HolderLookup;

public class ConfigLoader {

    public static void onConfigLoaded(HolderLookup.Provider provider) {
        ListHandler.initConfigLists();
        PickupConditionHandler.initPickupConditions();
        // ModelOverrideHandler.initModelOverrides(provider);
    }
}
