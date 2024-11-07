package violet.dainty.features.recipeviewer.addons.resources.common.jei.villager;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.addons.resources.common.collection.TradeList;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.AbstractVillagerEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.BlankJEIRecipeCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.JEIConfig;
import violet.dainty.features.recipeviewer.addons.resources.common.reference.Resources;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;

public class VillagerCategory extends BlankJEIRecipeCategory<AbstractVillagerEntry> {
    protected static final int X_FIRST_ITEM = 95;
    protected static final int X_ITEM_DISTANCE = 18;
    protected static final int X_ITEM_RESULT = 150;
    protected static final int Y_ITEM_DISTANCE = 22;

    public VillagerCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 0, 0, 16, 16), new VillagerWrapper());
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("dainty.villager.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.VILLAGER;
    }

    @Override
    public @NotNull RecipeType<AbstractVillagerEntry> getRecipeType() {  
        return JEIConfig.VILLAGER_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull AbstractVillagerEntry recipe, @NotNull IFocusGroup focuses) {
        if (recipe.hasPois()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 50, 19)
                .addItemStacks(recipe.getPois());
        }

        IFocus<ItemStack> focus = focuses.getFocuses(VanillaTypes.ITEM_STACK).findFirst().orElse(null);
        ((VillagerWrapper)recipeCategoryExtension).setFocus(focus);
        List<Integer> possibleLevels = recipe.getPossibleLevels(focus);
        int y = 1 + Y_ITEM_DISTANCE * (6 - possibleLevels.size()) / 2;
        for (int i = 0; i < possibleLevels.size(); i++) {
            TradeList tradeList = recipe.getVillagerTrades(possibleLevels.get(i)).getFocusedList(focus);
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + X_FIRST_ITEM, y + i * Y_ITEM_DISTANCE)
                .addItemStacks(tradeList.getCostAs());
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + X_FIRST_ITEM + X_ITEM_DISTANCE, y + i * Y_ITEM_DISTANCE)
                .addItemStacks(tradeList.getCostBs());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 1 + X_ITEM_RESULT, y + i * Y_ITEM_DISTANCE)
                .addItemStacks(tradeList.getResults());
        }
    }
}
