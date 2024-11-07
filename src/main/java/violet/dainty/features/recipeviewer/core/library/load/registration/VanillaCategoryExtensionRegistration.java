package violet.dainty.features.recipeviewer.core.library.load.registration;

import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.vanilla.crafting.IExtendableCraftingRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.vanilla.smithing.IExtendableSmithingRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IVanillaCategoryExtensionRegistration;
import violet.dainty.features.recipeviewer.core.library.runtime.JeiHelpers;

public class VanillaCategoryExtensionRegistration implements IVanillaCategoryExtensionRegistration {
	private final IExtendableCraftingRecipeCategory craftingCategory;
	private final IExtendableSmithingRecipeCategory smithingCategory;
	private final JeiHelpers jeiHelpers;

	public VanillaCategoryExtensionRegistration(
		IExtendableCraftingRecipeCategory craftingCategory,
		IExtendableSmithingRecipeCategory smithingCategory,
		JeiHelpers jeiHelpers
	) {
		this.craftingCategory = craftingCategory;
		this.smithingCategory = smithingCategory;
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public IExtendableCraftingRecipeCategory getCraftingCategory() {
		return craftingCategory;
	}

	@Override
	public IExtendableSmithingRecipeCategory getSmithingCategory() {
		return smithingCategory;
	}

	@Override
	public IJeiHelpers getJeiHelpers() {
		return jeiHelpers;
	}
}
