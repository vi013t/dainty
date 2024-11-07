package violet.dainty.features.recipeviewer.addons.resources.common.jei.plant;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.PlantDrop;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.PlantEntry;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotTooltipCallback;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;

public class PlantTooltip implements IRecipeSlotTooltipCallback {
    private final PlantEntry entry;

    public PlantTooltip(PlantEntry entry) {
        this.entry = entry;
    }

    @Override
    public void onTooltip(IRecipeSlotView recipeSlotView, @NotNull List<Component> tooltip) {
        if (recipeSlotView.getRole() != RecipeIngredientRole.INPUT) {
            tooltip.add(getChanceString((ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient()));
        }
    }

    public float getChance(ItemStack itemStack) {
        PlantDrop drop = entry.getDrop(itemStack);
        return switch (drop.getDropKind()) {
            case chance -> drop.getChance();
            case weight -> (float) drop.getWeight() / entry.getTotalWeight();
            case minMax -> Float.NaN;
            default -> 0;
        };
    }

    public int[] getMinMax(ItemStack itemStack) {
        PlantDrop drop = entry.getDrop(itemStack);
        return new int[]{drop.getMinDrop(), drop.getMaxDrop()};
    }

    private Component getChanceString(ItemStack itemStack) {
        float chance = getChance(itemStack);
        String toPrint;
        if (Float.isNaN(chance)) {
            int[] minMax = this.getMinMax(itemStack);
            toPrint = minMax[0] + (minMax[0] == minMax[1] ? "" : " - " + minMax[1]);
        } else {
            toPrint = String.format("%2.2f", chance * 100).replace(",", ".") + "%";
        }
        return Component.literal(toPrint);
    }
}
