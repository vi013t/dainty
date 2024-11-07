package violet.dainty.features.recipeviewer.addons.resources.neoforge;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.ILootTableHelper;


public class LootTableHelper implements ILootTableHelper {

    private static LootTableHelper instance;

    public static ILootTableHelper instance() {
        if (instance == null) {
            instance = new LootTableHelper();
        }
        return instance;
    }

    private LootTableHelper() {

    }

    @Override
    public NumberProvider getRolls(LootPool pool) {
        return pool.getRolls();
    }

    @Override
    public NumberProvider getBonusRolls(LootPool pool) {
        return pool.getBonusRolls();
    }
}
