package violet.dainty.features.recipeviewer.addons.resources.common.jei.mob;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.addons.resources.api.drop.LootDrop;
import violet.dainty.features.recipeviewer.addons.resources.common.config.Settings;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.MobEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.BlankJEIRecipeCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.JEIConfig;
import violet.dainty.features.recipeviewer.addons.resources.common.reference.Resources;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;

public class MobCategory extends BlankJEIRecipeCategory<MobEntry> {
    protected static final int X_FIRST_ITEM = 97;
    protected static final int Y_FIRST_ITEM = 43;

    public MobCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 16, 16, 16, 16), new MobWrapper());
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("dainty.mob.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.MOB;
    }

    @Override
    public @NotNull RecipeType<MobEntry> getRecipeType() {  
        return JEIConfig.MOB_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull MobEntry recipe, @NotNull IFocusGroup focuses) {
        int xOffset = 0;;
        List<LootDrop> drops = recipe.getDrops();
        int dropCount = Math.min(drops.size(), Settings.ITEMS_PER_ROW * Settings.ITEMS_PER_COLUMN);
        for (int i = 0; i < Settings.ITEMS_PER_ROW; i++) {
            int yOffset = 0;
            for (int ii = 0; ii < Settings.ITEMS_PER_COLUMN; ii++) {
                int slotNumber = i + ii * Settings.ITEMS_PER_ROW;
                IRecipeSlotBuilder slotBuilder = builder
                    .addSlot(RecipeIngredientRole.OUTPUT, X_FIRST_ITEM + xOffset, Y_FIRST_ITEM + yOffset)
                    .setSlotName(String.valueOf(slotNumber))
                    .addTooltipCallback(new MobTooltip(recipe));
                if (slotNumber < dropCount) {
                    slotBuilder.addItemStacks(drops.get(slotNumber).getDrops());
                }
                yOffset += 80 / Settings.ITEMS_PER_COLUMN;
            }
            xOffset += 72 / Settings.ITEMS_PER_ROW;
        }
        if (recipe.hasSpawnEgg()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 151, 22).addItemStack(recipe.getSpawnEgg());
        }
    }
}