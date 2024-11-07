package violet.dainty.features.recipeviewer.addons.resources.common.jei.worldgen;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.addons.resources.api.render.ColorHelper;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.WorldGenEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.BlankJEIRecipeCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.JEIConfig;
import violet.dainty.features.recipeviewer.addons.resources.common.reference.Resources;
import violet.dainty.features.recipeviewer.addons.resources.common.util.RenderHelper;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;

public class WorldGenCategory extends BlankJEIRecipeCategory<WorldGenEntry> {
    protected static final int X_ITEM = 6;
    protected static final int Y_ITEM = 22;
    protected static final int X_DROP_ITEM = 6;
    protected static final int Y_DROP_ITEM = 67;
    private static final int DROP_ITEM_COUNT = 8;

    public WorldGenCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 32, 16, 16, 16), new WorldGenWrapper());
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("dainty.worldgen.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.WORLD_GEN;
    }

    @Override
    public @NotNull RecipeType<WorldGenEntry> getRecipeType() {
        return JEIConfig.WORLD_GEN_TYPE;
    }

    @Override
    public void draw(@NotNull WorldGenEntry recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        RenderHelper.drawLine(guiGraphics, WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET, WorldGenWrapper.X_OFFSET + WorldGenWrapper.X_AXIS_SIZE, WorldGenWrapper.Y_OFFSET, ColorHelper.GRAY);  
        RenderHelper.drawLine(guiGraphics, WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET, WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET - WorldGenWrapper.Y_AXIS_SIZE, ColorHelper.GRAY);
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull WorldGenEntry recipe, @NotNull IFocusGroup focuses) {
        WorldGenTooltip worldGenTooltip = new WorldGenTooltip(recipe);
        builder.addSlot(RecipeIngredientRole.OUTPUT, X_ITEM, Y_ITEM)
            .addItemStacks(recipe.getBlocks())
            .setSlotName(WorldGenWrapper.ORE_SLOT_NAME)
            .addTooltipCallback(worldGenTooltip);

        for (int i = 0; i < Math.min(DROP_ITEM_COUNT, recipe.getDrops().size()); i++)
            builder.addSlot(RecipeIngredientRole.OUTPUT, X_DROP_ITEM + i * 18, Y_DROP_ITEM)
                .addItemStack(recipe.getDrops().get(i))
                .addTooltipCallback(worldGenTooltip);
    }
}