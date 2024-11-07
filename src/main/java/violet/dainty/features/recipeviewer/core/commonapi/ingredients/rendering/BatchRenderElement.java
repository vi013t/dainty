package violet.dainty.features.recipeviewer.core.commonapi.ingredients.rendering;

import net.minecraft.client.gui.GuiGraphics;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;

import java.util.List;

/**
 * A single ingredient to render in a batch render operation.
 *
 * @see IIngredientRenderer#renderBatch(GuiGraphics, List)
 *
 * @since 19.14.0
 */
public record BatchRenderElement<T>(T ingredient, int x, int y) {}
