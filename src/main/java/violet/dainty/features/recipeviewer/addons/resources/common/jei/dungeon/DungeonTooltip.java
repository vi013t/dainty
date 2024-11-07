package violet.dainty.features.recipeviewer.addons.resources.common.jei.dungeon;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.DungeonEntry;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotTooltipCallback;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;

public class DungeonTooltip implements IRecipeSlotTooltipCallback {
    private final DungeonEntry entry;

    public DungeonTooltip(DungeonEntry entry) {
        this.entry = entry;
    }

    @Override
    public void onTooltip(IRecipeSlotView recipeSlotView, List<Component> tooltip) {
        tooltip.add(entry.getChestDrop((ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient()).toStringTextComponent());
    }
}
