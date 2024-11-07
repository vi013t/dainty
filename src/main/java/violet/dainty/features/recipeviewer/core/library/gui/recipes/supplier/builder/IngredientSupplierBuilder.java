package violet.dainty.features.recipeviewer.core.library.gui.recipes.supplier.builder;

import java.util.EnumMap;
import java.util.Map;

import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IIngredientAcceptor;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientSupplier;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.gui.recipes.RecipeLayoutIngredientSupplier;

/**
 * Minimal version of {@link IRecipeLayoutBuilder} that can only return the ingredients,
 * but doesn't bother building real slots or anything else for drawing on screen.
 */
public class IngredientSupplierBuilder implements IRecipeLayoutBuilder {
	private final IIngredientManager ingredientManager;
	private final Map<RecipeIngredientRole, IngredientSlotBuilder> ingredientSlotBuilders;

	public IngredientSupplierBuilder(IIngredientManager ingredientManager) {
		this.ingredientManager = ingredientManager;
		this.ingredientSlotBuilders = new EnumMap<>(RecipeIngredientRole.class);
	}

	@Override
	public IRecipeSlotBuilder addSlot(RecipeIngredientRole role, int x, int y) {
		return addSlot(role);
	}

	@Override
	public IRecipeSlotBuilder addSlot(RecipeIngredientRole role) {
		IngredientSlotBuilder slot = ingredientSlotBuilders.get(role);
		if (slot == null) {
			slot = new IngredientSlotBuilder(ingredientManager);
			ingredientSlotBuilders.put(role, slot);
		}
		return slot;
	}

	@SuppressWarnings("removal")
	@Override
	public IRecipeSlotBuilder addSlotToWidget(RecipeIngredientRole role, violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<?> widgetFactory) {
		return addSlot(role);
	}

	@Override
	public IIngredientAcceptor<?> addInvisibleIngredients(RecipeIngredientRole role) {
		return addSlot(role);
	}

	@Override
	public void moveRecipeTransferButton(int posX, int posY) {

	}

	@Override
	public void setShapeless() {

	}

	@Override
	public void setShapeless(int posX, int posY) {

	}

	@Override
	public void createFocusLink(IIngredientAcceptor<?>... slots) {

	}

	public IIngredientSupplier buildIngredientSupplier() {
		return new RecipeLayoutIngredientSupplier(this.ingredientSlotBuilders);
	}
}
