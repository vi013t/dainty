package violet.dainty.features.recipeviewer.addons.resources.api.conditionals;

import java.util.Random;

import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.LootDrop;

/**
 * Should be implemented on a {@link }
 * when a table has use that {@link LootItemFunction}
 * you'll be granted access to the {@link LootDrop}
 * <p>
 * It is advised to add {@link Conditional}s,
 * but you can also change stack sizes or other data.
 */
public interface ICustomLootFunction {
    /**
     * Similar to {@link LootItemFunction#apply}
     * but without all the {@link Random} and {@link net.minecraft.world.level.storage.loot.LootContext}
     * <p>
     * You only need to implement this if the the {@link LootItemFunction#apply}
     * can't be called with null random and context or if you want to do a more advanced interaction with the {@link LootDrop}
     *
     * @param drop the {@link LootDrop} to apply the {@link LootItemFunction} on
     */
    void apply(LootDrop drop);
}
