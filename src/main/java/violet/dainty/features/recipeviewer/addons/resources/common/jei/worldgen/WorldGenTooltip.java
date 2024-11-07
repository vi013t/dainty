package violet.dainty.features.recipeviewer.addons.resources.common.jei.worldgen;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import violet.dainty.features.recipeviewer.addons.resources.api.conditionals.Conditional;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.LootDrop;
import violet.dainty.features.recipeviewer.addons.resources.common.config.Settings;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.WorldGenEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.util.RegistryHelper;
import violet.dainty.features.recipeviewer.addons.resources.common.util.TranslationHelper;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotTooltipCallback;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;

public class WorldGenTooltip implements IRecipeSlotTooltipCallback {
    private final WorldGenEntry entry;

    public WorldGenTooltip(WorldGenEntry entry) {
        this.entry = entry;
    }

    @Override
    public void onTooltip(IRecipeSlotView recipeSlotView, List<Component> tooltip) {
        tooltip.addAll(getItemStackTooltip(recipeSlotView.getSlotName().orElse(null), (ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient()));
    }

    private List<Component> getItemStackTooltip(String slotName, ItemStack itemStack) {
        List<String> tooltip = new LinkedList<>();
        if (itemStack != null && slotName != null && slotName.equals(WorldGenWrapper.ORE_SLOT_NAME)) {
            if (entry.isSilkTouchNeeded())
                tooltip.add(Conditional.silkTouch.toString());  

            List<String> biomes = entry.getBiomeRestrictions();
            if (biomes.size() > 0) {
                tooltip.add(TranslationHelper.translateAndFormat("dainty.worldgen.biomes") + ":");
                tooltip.addAll(biomes);
            }

            if (Settings.showDevData) {
                tooltip.add(TranslationHelper.translateAndFormat("dainty.worldgen.averageChunk") + ":");
                tooltip.add("" + entry.getAverageBlockCountPerChunk());
            }

        } else {
            tooltip.add(TranslationHelper.translateAndFormat("dainty.worldgen.average"));
            for (LootDrop dropItem : entry.getLootDrops(itemStack)) {
                String line = " - ";

                if (dropItem.fortuneLevel > 0) {
                    line += Enchantment.getFullname(RegistryHelper.getHolder(Registries.ENCHANTMENT, Enchantments.FORTUNE), dropItem.fortuneLevel).getString();
                } else {
                    line += TranslationHelper.translateAndFormat("dainty.worldgen.base");
                }

                if (dropItem.chance < 1f) {
                    line += " " + TranslationHelper.translateAndFormat("dainty.worldgen.chance", dropItem.formatChance() + "%");
                }

                if (dropItem.minDrop == dropItem.maxDrop) {
                    line += ": " + dropItem.minDrop;
                } else {
                    line += ": " + dropItem.minDrop + " - " + dropItem.maxDrop;
                }

                if (dropItem.isAffectedBy(Conditional.affectedByFortune)) {
                    line += " " + TranslationHelper.translateAndFormat("dainty.worldgen.affectedByFortune");
                }

                tooltip.add(line);
            }
        }
        return tooltip.stream().map(Component::literal).collect(Collectors.toList());
    }
}
