package violet.dainty.features.recipeviewer.addons.resources.api;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import violet.dainty.features.recipeviewer.addons.resources.api.conditionals.LightLevel;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.LootDrop;
import violet.dainty.features.recipeviewer.addons.resources.api.render.IMobRenderHook;
import violet.dainty.features.recipeviewer.addons.resources.api.render.IScissorHook;

/**
 * Use to register new {@link LivingEntity}s, {@link IMobRenderHook}s and {@link IScissorHook}s
 */
public interface IMobRegistry {
    /**
     * Register a custom {@link LivingEntity} with given parameters
     * Implement {@link violet.dainty.features.recipeviewer.addons.resources.api.conditionals.ICustomLootFunction}
     * to gain more control over the information added to the tooltips when using custom
     * {@link net.minecraft.world.level.storage.loot.functions.LootItemFunction}s
     *
     * @param entity     the {@link LivingEntity} instance
     * @param lightLevel the {@link LightLevel} the {@link LivingEntity} spawns at
     * @param minExp     minimum exp gained by killing the {@link LivingEntity}
     * @param maxExp     maximum exp gained by killing the {@link LivingEntity}
     * @param biomes     {@link java.util.List} of {@link String} names of the biomes
     * @param lootTable  the {@link ResourceKey} of the loot table
     */
    void register(LivingEntity entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, ResourceKey<LootTable> lootTable);
    void register(LivingEntity entity, LightLevel lightLevel, int minExp, int maxExp, ResourceKey<LootTable> lootTable);
    void register(LivingEntity entity, LightLevel lightLevel, int exp, String[] biomes, ResourceKey<LootTable> lootTable);
    void register(LivingEntity entity, LightLevel lightLevel, int exp, ResourceKey<LootTable> lootTable);
    void register(LivingEntity entity, LightLevel lightLevel, String[] biomes, ResourceKey<LootTable> lootTable);
    void register(LivingEntity entity, LightLevel lightLevel, ResourceKey<LootTable> lootTable);
    void register(LivingEntity entity, ResourceKey<LootTable> lootTable);

    /**
     * Register a custom {@link LivingEntity} with given parameters
     *
     * @param entity     the {@link LivingEntity} instance
     * @param lightLevel the {@link LightLevel} the {@link LivingEntity} spawns at
     * @param minExp     minimum exp gained by killing the {@link LivingEntity}
     * @param maxExp     maximum exp gained by killing the {@link LivingEntity}
     * @param biomes     {@link java.util.List} of {@link String} names of the biomes
     * @param lootDrops  the {@link LootDrop}s to add
     */
    void register(LivingEntity entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, LootDrop... lootDrops);
    void register(LivingEntity entity, LightLevel lightLevel, int minExp, int maxExp, LootDrop... lootDrops);
    void register(LivingEntity entity, LightLevel lightLevel, int exp, String[] biomes, LootDrop... lootDrops);
    void register(LivingEntity entity, LightLevel lightLevel, int exp, LootDrop... lootDrops);
    void register(LivingEntity entity, LightLevel lightLevel, String[] biomes, LootDrop... lootDrops);
    void register(LivingEntity entity, LightLevel lightLevel, LootDrop... lootDrops);
    void register(LivingEntity entity, LootDrop... lootDrops);

    /**
     * Add a hook for scissoring in the mob view
     * The stacktrace will be used to see what called the render
     *
     * @param caller      the class that will call the render
     * @param scissorHook your {@link IScissorHook}
     */
    void registerScissorHook(Class caller, IScissorHook scissorHook);

    /**
     * Add a {@link IMobRenderHook} for the given mob type
     * The render hook will be called when rendering in the mob view of JER
     *
     * @param entity     the  {@link Class} of the {@link LivingEntity}
     * @param renderHook the {@link IMobRenderHook} to be applied
     */
    void registerRenderHook(Class<? extends LivingEntity> entity, IMobRenderHook renderHook);
}
