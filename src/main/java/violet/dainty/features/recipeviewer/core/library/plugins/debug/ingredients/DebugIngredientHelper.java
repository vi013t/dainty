package violet.dainty.features.recipeviewer.core.library.plugins.debug.ingredients;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;

import org.jetbrains.annotations.Nullable;

public class DebugIngredientHelper implements IIngredientHelper<DebugIngredient> {
	@Override
	public IIngredientType<DebugIngredient> getIngredientType() {
		return DebugIngredient.TYPE;
	}

	@Override
	public String getDisplayName(DebugIngredient ingredient) {
		return "JEI Debug Item #" + ingredient.number();
	}

	@SuppressWarnings("removal")
	@Override
	public String getUniqueId(DebugIngredient ingredient, UidContext context) {
		return "JEI_debug_" + ingredient.number();
	}

	@Override
	public Object getUid(DebugIngredient ingredient, UidContext context) {
		return ingredient.number();
	}

	@SuppressWarnings("removal")
	@Override
	public String getWildcardId(DebugIngredient ingredient) {
		return "JEI_debug";
	}

	@Override
	public Object getGroupingUid(DebugIngredient ingredient) {
		return DebugIngredient.class;
	}

	@Override
	public ResourceLocation getResourceLocation(DebugIngredient ingredient) {
		return ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "debug_" + ingredient.number());
	}

	@Override
	public DebugIngredient copyIngredient(DebugIngredient ingredient) {
		return ingredient.copy();
	}

	@Override
	public String getErrorInfo(@Nullable DebugIngredient ingredient) {
		if (ingredient == null) {
			return "debug ingredient: null";
		}
		return getDisplayName(ingredient);
	}
}
