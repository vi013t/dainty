package violet.dainty.features.recipeviewer.addons.resources.common.compatibility;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import violet.dainty.features.recipeviewer.addons.resources.api.render.IMobRenderHook;
import violet.dainty.features.recipeviewer.addons.resources.common.compatibility.api.JERAPI;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.DungeonEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.MobEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.PlantEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.WorldGenEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.DungeonRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.MobRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.PlantRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.registry.WorldGenRegistry;
import violet.dainty.features.recipeviewer.addons.resources.common.util.FakeClientLevel;

public abstract class CompatBase {
    @Nullable
    private static Level fakeClientLevel = null;

    /**
     * This should only be used for loading loot tables,
     * it is used so that clients connected to an integrated server
     * do not need to load the loot tables multiple times.
     *
     * It is dangerous to use for other purposes, because modded entities (and even vanilla villagers)
     * can load lots of things if they have the server level, which will make JER load slowly.
     */
    public static Optional<Level> getServerLevel() {
        Minecraft minecraft = Minecraft.getInstance();
        return Optional.of(minecraft)
                .map(Minecraft::getSingleplayerServer)
                .map(integratedServer -> integratedServer.getLevel(Level.OVERWORLD));
    }

    @NotNull
    public static Level getLevel() {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level != null) {
            return level;
        }

        if (fakeClientLevel == null) {
            fakeClientLevel = new FakeClientLevel();
        }
        return fakeClientLevel;
    }

    public abstract void init(boolean worldGen);

    protected void registerMob(MobEntry entry) {
        MobRegistry.getInstance().registerMob(entry);
    }

    protected void registerDungeonEntry(DungeonEntry entry) {
        DungeonRegistry.getInstance().registerDungeonEntry(entry);
    }

    protected void registerWorldGen(WorldGenEntry entry) {
        WorldGenRegistry.getInstance().registerEntry(entry);
    }

    protected void registerPlant(PlantEntry entry) {
        PlantRegistry.getInstance().registerPlant(entry);
    }

    protected void registerMobRenderHook(Class<? extends LivingEntity> clazz, IMobRenderHook renderHook) {
        JERAPI.getInstance().getMobRegistry().registerRenderHook(clazz, renderHook);
    }
}
