package violet.dainty.features.recipeviewer.addons.resources.common.compatibility.api;

import net.minecraft.world.level.Level;
import violet.dainty.features.recipeviewer.addons.resources.api.IDungeonRegistry;
import violet.dainty.features.recipeviewer.addons.resources.api.IJERAPI;
import violet.dainty.features.recipeviewer.addons.resources.api.IMobRegistry;
import violet.dainty.features.recipeviewer.addons.resources.api.IPlantRegistry;
import violet.dainty.features.recipeviewer.addons.resources.api.IWorldGenRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.compatibility.CompatBase;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.Services;

public class JERAPI implements IJERAPI {
    private IWorldGenRegistry worldGenRegistry;
    private IMobRegistry mobRegistry;
    private IPlantRegistry plantRegistry;
    private IDungeonRegistry dungeonRegistry;
    private static IJERAPI instance;

    public static IJERAPI getInstance() {
        if (instance == null)
            instance = new JERAPI();
        return instance;
    }

    private JERAPI() {
        worldGenRegistry = new WorldGenRegistryImpl();
        mobRegistry = new MobRegistryImpl();
        plantRegistry = new PlantRegistryImpl();
        dungeonRegistry = new DungeonRegistryImpl();
    }

    public static void init() {
        Services.PLATFORM.injectApi(JERAPI.getInstance());
    }

    @Override
    public IMobRegistry getMobRegistry() {
        return mobRegistry;
    }

    @Override
    public IWorldGenRegistry getWorldGenRegistry() {
        return worldGenRegistry;
    }

    @Override
    public IPlantRegistry getPlantRegistry() {
        return plantRegistry;
    }

    @Override
    public IDungeonRegistry getDungeonRegistry() {
        return dungeonRegistry;
    }

    @Override
    public Level getLevel() {
        return CompatBase.getLevel();
    }

    public static void commit(boolean initWorldGen) {
        // API implements their own abuse protection
        DungeonRegistryImpl.commit();
        MobRegistryImpl.commit();
        PlantRegistryImpl.commit();
        if (initWorldGen)
            WorldGenRegistryImpl.commit();
    }
}
