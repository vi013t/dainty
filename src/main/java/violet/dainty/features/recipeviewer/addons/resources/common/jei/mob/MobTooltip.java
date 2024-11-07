package violet.dainty.features.recipeviewer.addons.resources.common.jei.mob;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.LootDrop;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.MobEntry;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotTooltipCallback;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotView;

public class MobTooltip implements IRecipeSlotTooltipCallback {
    private final MobEntry entry;

    public MobTooltip(MobEntry entry) {
        this.entry = entry;
    }

    @Override
    public void onTooltip(@NotNull IRecipeSlotView recipeSlotView, @NotNull List<Component> tooltip) {
        LootDrop lootDrop = this.entry.getDrops().get(Integer.parseInt(recipeSlotView.getSlotName().orElse("0")));
        tooltip.add(lootDrop.toStringTextComponent());
        List<Component> list = getToolTip((ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient());
        if (list != null)
            tooltip.addAll(list);
    }

    public List<Component> getToolTip(ItemStack stack) {
        for (LootDrop item : this.entry.getDrops()) {
            if (stack.is(item.item.getItem()))
                return item.getTooltipText();
            if (item.canBeCooked() && stack.is(item.smeltedItem.getItem()))
                return item.getTooltipText(true);
        }
        return null;
    }
}
