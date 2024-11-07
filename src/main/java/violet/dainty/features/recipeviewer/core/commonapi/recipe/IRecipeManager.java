package violet.dainty.features.recipeviewer.core.commonapi.recipe;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.IRecipeLayoutDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IScalableDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientSupplier;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiRuntime;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The {@link IRecipeManager} offers several functions for retrieving and handling recipes.
 * Get the instance from {@link IJeiRuntime#getRecipeManager()}.
 */
public interface IRecipeManager {
	/**
	 * Create a recipe lookup for the given recipe type.
	 *
	 * {@link IRecipeLookup} is a helper class that lets you choose
	 * the results you want, and then get them.
	 *
	 * @since 9.5.0
	 */
	<R> IRecipeLookup<R> createRecipeLookup(RecipeType<R> recipeType);

	/**
	 * Create a recipe category lookup for the given recipe type.
	 *
	 * {@link IRecipeCategoriesLookup} is a helper class that lets you choose
	 * the results you want, and then get them.
	 *
	 * @since 9.5.0
	 */
	IRecipeCategoriesLookup createRecipeCategoryLookup();

	/**
	 * Get a recipe category for the given recipe type.
	 *
	 * For more complex queries, use {@link #createRecipeCategoryLookup()}
	 *
	 * @since 19.1.0
	 */
	<T> IRecipeCategory<T> getRecipeCategory(RecipeType<T> recipeType);

	/**
	 * Create a recipe catalyst lookup for the given recipe type.
	 *
	 * {@link IRecipeCatalystLookup} is a helper class that lets you choose
	 * the results you want, and then get them.
	 *
	 * @since 9.5.0
	 */
	IRecipeCatalystLookup createRecipeCatalystLookup(RecipeType<?> recipeType);

	/**
	 * Hides recipes so that they will not be displayed.
	 * This can be used by mods that create recipe progression.
	 *
	 * @param recipeType the recipe type for this recipe.
	 * @param recipes    the recipes to hide.
	 *
	 * @see #unhideRecipes(RecipeType, Collection)
	 * @see RecipeTypes for all the built-in recipe types that are added by JEI.
	 *
	 * @since 9.5.0
	 */
	<T> void hideRecipes(RecipeType<T> recipeType, Collection<T> recipes);

	/**
	 * Unhides recipes that were hidden by {@link #hideRecipes(RecipeType, Collection)}
	 * This can be used by mods that create recipe progression.
	 *
	 * @param recipeType the recipe type for this recipe.
	 * @param recipes    the recipes to unhide.
	 *
	 * @see #hideRecipes(RecipeType, Collection)
	 * @see RecipeTypes for all the built-in recipe types that are added by JEI.
	 *
	 * @since 9.5.0
	 */
	<T> void unhideRecipes(RecipeType<T> recipeType, Collection<T> recipes);

	/**
	 * Add new recipes while the game is running.
	 *
	 * @see RecipeTypes for all the built-in recipe types that are added by JEI.
	 *
	 * @since 9.5.0
	 */
	<T> void addRecipes(RecipeType<T> recipeType, List<T> recipes);

	/**
	 * Hide an entire recipe category of recipes from JEI.
	 * This can be used by mods that create recipe progression.
	 *
	 * @param recipeType the unique ID for the recipe category
	 * @see #unhideRecipeCategory(RecipeType)
	 *
	 * @since 9.5.0
	 */
	void hideRecipeCategory(RecipeType<?> recipeType);

	/**
	 * Unhides a recipe category that was hidden by {@link #hideRecipeCategory(RecipeType)}.
	 * This can be used by mods that create recipe progression.
	 *
	 * @param recipeType the unique ID for the recipe category
	 * @see #hideRecipeCategory(RecipeType)
	 *
	 * @since 9.5.0
	 */
	void unhideRecipeCategory(RecipeType<?> recipeType);

	/**
	 * Returns a drawable recipe layout, for addons that want to draw the layouts somewhere.
	 * If there is something wrong and the recipe layout crashes, this will display an error recipe instead.
	 *
	 * @param recipeCategory the recipe category that the recipe belongs to
	 * @param recipe         the specific recipe to draw.
	 * @param focusGroup     the focuses of the recipe layout.
	 *
	 * @since 19.19.6
	 */
	<T> IRecipeLayoutDrawable<T> createRecipeLayoutDrawableOrShowError(
		IRecipeCategory<T> recipeCategory,
		T recipe,
		IFocusGroup focusGroup
	);

	/**
	 * Returns a drawable recipe layout, for addons that want to draw the layouts somewhere.
	 *
	 * @param recipeCategory the recipe category that the recipe belongs to
	 * @param recipe         the specific recipe to draw.
	 * @param focusGroup     the focuses of the recipe layout.
	 *
	 * @since 11.5.0
	 */
	<T> Optional<IRecipeLayoutDrawable<T>> createRecipeLayoutDrawable(
		IRecipeCategory<T> recipeCategory,
		T recipe,
		IFocusGroup focusGroup
	);

	/**
	 * Returns a drawable recipe layout, for addons that want to draw the layouts somewhere.
	 * Use a custom background to draw behind the recipe.
	 *
	 * @param recipeCategory the recipe category that the recipe belongs to
	 * @param recipe         the specific recipe to draw.
	 * @param focusGroup     the focuses of the recipe layout.
	 * @param background     the background image to draw behind the recipe layout.
	 * @param borderSize     the number of pixels that the background should extend beyond the recipe layout on all sides
	 *
	 * @since 19.4.0
	 */
	<T> Optional<IRecipeLayoutDrawable<T>> createRecipeLayoutDrawable(
			IRecipeCategory<T> recipeCategory,
			T recipe,
			IFocusGroup focusGroup,
			IScalableDrawable background,
			int borderSize
	);

	/**
	 * Returns a drawable recipe slot, for addons that want to draw the slots somewhere.
	 *
	 * @param role                  the recipe ingredient role of this slot
	 * @param ingredients           a non-null list of optional ingredients for the slot
	 * @param focusedIngredients    indexes of the focused ingredients in "ingredients"
	 * @param ingredientCycleOffset the starting index for cycling the list of ingredients when rendering.
	 * @since 19.19.1
	 */
	IRecipeSlotDrawable createRecipeSlotDrawable(
		RecipeIngredientRole role,
		List<Optional<ITypedIngredient<?>>> ingredients,
		Set<Integer> focusedIngredients,
		int ingredientCycleOffset
	);

	/**
	 * Returns a drawable recipe slot, for addons that want to draw the slots somewhere.
	 *
	 * @param role                  the recipe ingredient role of this slot
	 * @param ingredients           a non-null list of optional ingredients for the slot
	 * @param focusedIngredients    indexes of the focused ingredients in "ingredients"
	 * @param xPos                  the x position of the slot on the screen
	 * @param yPos                  the y position of the slot on the screen
	 * @param ingredientCycleOffset the starting index for cycling the list of ingredients when rendering.
	 * @since 11.5.0
	 * @deprecated use {@link #createRecipeSlotDrawable(RecipeIngredientRole, List, Set, int)} and then set the position
	 */
	@Deprecated(since = "19.19.1")
	default IRecipeSlotDrawable createRecipeSlotDrawable(
		RecipeIngredientRole role,
		List<Optional<ITypedIngredient<?>>> ingredients,
		Set<Integer> focusedIngredients,
		int xPos,
		int yPos,
		int ingredientCycleOffset
	) {
		IRecipeSlotDrawable recipeSlotDrawable = createRecipeSlotDrawable(role, ingredients, focusedIngredients, ingredientCycleOffset);
		recipeSlotDrawable.setPosition(xPos, yPos);
		return recipeSlotDrawable;
	}

	/**
	 * Get the ingredients for a given recipe.
	 * @since 19.9.0
	 */
	<T> IIngredientSupplier getRecipeIngredients(IRecipeCategory<T> recipeCategory, T recipe);

	/**
	 * Get the registered recipe type for the given unique id.
	 * <p>
	 * This is useful for integrating with other mods that do not share their
	 * recipe types directly from their API.
	 *
	 * @see RecipeType#getUid()
	 * @since 19.11.0
	 */
	<T> Optional<RecipeType<T>> getRecipeType(ResourceLocation recipeUid, Class<? extends T> recipeClass);

	/**
	 * Get the registered recipe type for the given unique id.
	 * <p>
	 * This is useful for integrating with other mods that do not share their
	 * recipe types directly from their API.
	 *
	 * @see RecipeType#getUid()
	 * @since 11.2.3
	 */
	Optional<RecipeType<?>> getRecipeType(ResourceLocation recipeUid);
}