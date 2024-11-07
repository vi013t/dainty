package violet.dainty.features.recipeviewer.addons.resources.common.jei.villager;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.AbstractVillagerEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.reference.Resources;
import violet.dainty.features.recipeviewer.addons.resources.common.util.Font;
import violet.dainty.features.recipeviewer.addons.resources.common.util.RenderHelper;
import violet.dainty.features.recipeviewer.addons.resources.common.util.TranslationHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.IRecipeCategoryExtension;


public class VillagerWrapper implements IRecipeCategoryExtension<AbstractVillagerEntry> {
    private IFocus<ItemStack> focus;

    public void setFocus(IFocus<ItemStack> focus) {
        this.focus = focus;
    }

    @Override
    public void drawInfo(AbstractVillagerEntry recipe, int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // TODO: Fix scissoring
        // RenderHelper.scissor(poseStack,7, 43, 59, 79);
        AbstractVillager villager = recipe.getVillagerEntity();
        RenderHelper.renderEntity(  
            guiGraphics,
            37, 118, 36.0F,
            38 - mouseX,
            80 - mouseY,
            villager
        );
        //RenderHelper.stopScissor();

        int y = VillagerCategory.Y_ITEM_DISTANCE * (6 - recipe.getPossibleLevels(focus).size()) / 2;
        int i;
        for (i = 0; i < recipe.getPossibleLevels(focus).size(); i++) {
            RenderHelper.drawTexture(guiGraphics, 130, y + i * VillagerCategory.Y_ITEM_DISTANCE, 0, 120, 20, 20, Resources.Gui.Jei.VILLAGER.getResource());
            RenderHelper.drawTexture(guiGraphics, VillagerCategory.X_FIRST_ITEM, y + i * VillagerCategory.Y_ITEM_DISTANCE, 22, 120, 18, 18, Resources.Gui.Jei.VILLAGER.getResource());
            RenderHelper.drawTexture(guiGraphics, VillagerCategory.X_FIRST_ITEM + VillagerCategory.X_ITEM_DISTANCE, y + i * VillagerCategory.Y_ITEM_DISTANCE, 22, 120, 18, 18, Resources.Gui.Jei.VILLAGER.getResource());
            RenderHelper.drawTexture(guiGraphics, VillagerCategory.X_ITEM_RESULT, y + i * VillagerCategory.Y_ITEM_DISTANCE, 22, 120, 18, 18, Resources.Gui.Jei.VILLAGER.getResource());
        }
        if (recipe.hasLevels()) {
            i = 0;
            for (Object oLevel : recipe.getPossibleLevels(focus)) {
                Integer level = (Integer) oLevel;
                Font.normal.print(guiGraphics, "lv. " + (level + 1), 72, y + i++ * VillagerCategory.Y_ITEM_DISTANCE + 6);
            }
        }

        Font.normal.print(guiGraphics, TranslationHelper.translateAndFormat(recipe.getDisplayName()), 5, 5);
        if (recipe.hasPois()) {
            Font.normal.splitPrint(guiGraphics, TranslationHelper.translateAndFormat("dainty.villager.poi"), 5, 18, 45);
            RenderHelper.drawTexture(guiGraphics, 49, 18, 22, 120, 18, 18, Resources.Gui.Jei.VILLAGER.getResource());
        }
    }
}
