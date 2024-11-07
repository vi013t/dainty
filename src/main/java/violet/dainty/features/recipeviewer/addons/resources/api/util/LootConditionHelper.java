package violet.dainty.features.recipeviewer.addons.resources.api.util;

import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import violet.dainty.features.recipeviewer.addons.resources.api.conditionals.Conditional;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.LootDrop;

public class LootConditionHelper {
    public static void applyCondition(LootItemCondition condition, LootDrop lootDrop) {
        if (condition instanceof LootItemKilledByPlayerCondition) {
            lootDrop.addConditional(Conditional.playerKill);
        } else if (condition instanceof LootItemRandomChanceCondition) {
            lootDrop.chance = ((LootItemRandomChanceCondition) condition).chance().getFloat(null); // TODO check if null is OK to use
        } else if (condition instanceof LootItemRandomChanceWithEnchantedBonusCondition) {
            lootDrop.chance = ((LootItemRandomChanceWithEnchantedBonusCondition) condition).enchantedChance().calculate(1);
            lootDrop.addConditional(Conditional.affectedByLooting);
        } else if (condition instanceof LootItemBlockStatePropertyCondition) {
            /*
            for (EntityProperty property : ((EntityHasProperty) condition).properties) {
                if (property instanceof Fire)
                    lootDrop.addConditional(Conditional.burning);
            }
            */
        }
    }
}
