package violet.dainty.features.recipeviewer.addons.resources.common.jei.plant;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.addons.resources.common.entry.PlantEntry;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.BlankJEIRecipeCategory;
import violet.dainty.features.recipeviewer.addons.resources.common.jei.JEIConfig;
import violet.dainty.features.recipeviewer.addons.resources.common.reference.Resources;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;


public class PlantCategory extends BlankJEIRecipeCategory<PlantEntry> {
    private static final int GRASS_X = 80;
    private static final int GRASS_Y = 11;
    private static final int OUTPUT_X = 7;
    private static final int OUTPUT_SCALE = 20;
    private static final int OUTPUT_Y = 61;

    public PlantCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 0, 16, 16, 16), new PlantWrapper());
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("dainty.plant.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.PLANT;
    }

    @Override
    public @NotNull RecipeType<PlantEntry> getRecipeType() { 
        return JEIConfig.PLANT_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull PlantEntry recipe, @NotNull IFocusGroup focuses) {
        PlantTooltip plantTooltip = new PlantTooltip(recipe);
        builder.addSlot(RecipeIngredientRole.INPUT, GRASS_X, GRASS_Y)
            .addItemStack(recipe.getPlantItemStack())
            .addTooltipCallback(plantTooltip);

        int xOffset = 0;
        int yOffset = 0;
        for (int i = 0; i < recipe.getLootDropStacks().size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X + xOffset, OUTPUT_Y + yOffset)
                .addItemStack(recipe.getLootDropStacks().get(i))
                .addTooltipCallback(plantTooltip);
            xOffset += OUTPUT_SCALE;
            if (xOffset > 147) {
                xOffset = 0;
                yOffset += OUTPUT_SCALE;
            }
        }
    }
}
