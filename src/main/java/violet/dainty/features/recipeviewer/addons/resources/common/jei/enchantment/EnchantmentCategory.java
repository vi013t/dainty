package violet.dainty.features.recipeviewer.addons.resources.common.jei.enchantment;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.BlankJEIRecipeCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.JEIConfig;
import violet.dainty.features.recipeviewer.addons.resources.common.reference.Resources;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;

public class EnchantmentCategory extends BlankJEIRecipeCategory<EnchantmentWrapper> {
    private static final int ITEM_X = 13;
    private static final int ITEM_Y = 12;

    public EnchantmentCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 32, 0, 16, 16), null);
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("dainty.enchantments.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.ENCHANTMENT; 
    }

    @Override
    public @NotNull RecipeType<EnchantmentWrapper> getRecipeType() {
        return JEIConfig.ENCHANTMENT_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull EnchantmentWrapper recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, ITEM_X, ITEM_Y).addItemStack(recipe.itemStack);
    }

    @Override
    public void draw(EnchantmentWrapper recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        recipe.drawInfo(recipe, getBackground().getWidth(), getBackground().getHeight(), guiGraphics, mouseX, mouseY);
    }

    @Override
    public @NotNull List<Component> getTooltipStrings(EnchantmentWrapper recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return recipe.getTooltipStrings(recipe, mouseX, mouseY);
    }
}
