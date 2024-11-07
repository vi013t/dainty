package violet.dainty.features.recipeviewer.core.commonapi.helpers;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusFactory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IVanillaRecipeFactory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientVisibility;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * {@link IJeiHelpers} provides helpers and tools for addon mods.
 * <p>
 * An instance is passed to your {@link IModPlugin}'s registration methods.
 */
public interface IJeiHelpers {
	/**
	 * Helps with the implementation of GUIs.
	 */
	IGuiHelper getGuiHelper();

	/**
	 * Helps with getting itemStacks from recipes.
	 */
	IStackHelper getStackHelper();

	/**
	 * Helps with getting the mod name from a mod ID.
	 */
	IModIdHelper getModIdHelper();

	/**
	 * Helps with creating focuses.
	 *
	 * @since 9.4.0
	 */
	IFocusFactory getFocusFactory();

	/**
	 * Helps with getting colors of ingredients.
	 *
	 * @since 11.5.0
	 */
	IColorHelper getColorHelper();

	/**
	 * Helps with handling fluid ingredients on multiple platforms (Forge and Fabric).
	 *
	 * @since 10.1.0
	 */
	IPlatformFluidHelper<?> getPlatformFluidHelper();

	/**
	 * Get the registered recipe type for the given unique id.
	 * <p>
	 * This is useful for integrating with other mods that do not share their
	 * recipe types directly from their API.
	 *
	 * @see RecipeType#getUid()
	 * @since 19.11.0
	 */
	<T> Optional<RecipeType<T>> getRecipeType(ResourceLocation uid, Class<? extends T> recipeClass);

	/**
	 * Get the registered recipe type for the given unique id.
	 * <p>
	 * This is useful for integrating with other mods that do not share their
	 * recipe types directly from their API.
	 *
	 * @see RecipeType#getUid()
	 * @since 11.4.0
	 */
	Optional<RecipeType<?>> getRecipeType(ResourceLocation uid);

	/**
	 * Get all registered recipe types.
	 *
	 * @since 15.1.0
	 */
	Stream<RecipeType<?>> getAllRecipeTypes();

	/**
	 * The ingredient manager, with information about all registered ingredients.
	 *
	 * @since 11.5.0
	 */
	IIngredientManager getIngredientManager();

	/**
	 * Helps with implementing various {@link Codec}s.
	 *
	 * @since 19.9.0
	 */
	ICodecHelper getCodecHelper();

	/**
	 * The {@link IVanillaRecipeFactory} allows creation of vanilla recipes.
	 *
	 * @since 19.15.0
	 */
	IVanillaRecipeFactory getVanillaRecipeFactory();

	/**
	 * The {@link IIngredientVisibility} allows mod plugins to do advanced filtering of
	 * ingredients based on what is visible in JEI.
	 *
	 * @since 19.18.4
	 */
	IIngredientVisibility getIngredientVisibility();
}
