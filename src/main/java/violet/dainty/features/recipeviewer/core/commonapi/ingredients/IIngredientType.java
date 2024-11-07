package violet.dainty.features.recipeviewer.core.commonapi.ingredients;

import org.jetbrains.annotations.Nullable;

import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.ICodecHelper;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IModIngredientRegistration;

import java.util.Optional;

/**
 * A type of ingredient (i.e. ItemStack, FluidStack, etc.) handled by JEI.
 * Register new types with {@link IModIngredientRegistration#register}
 *
 * @see VanillaTypes for the built-in vanilla type {@link VanillaTypes#ITEM_STACK}
 */
@FunctionalInterface
public interface IIngredientType<T> {
	/**
	 * @return The class of the ingredient for this type.
	 */
	Class<? extends T> getIngredientClass();

	/**
	 * @return The unique ID for this type, used for serialization to and from disk.
	 *
	 * @see ICodecHelper#getIngredientTypeCodec()
	 *
	 * @since 19.1.0
	 */
	default String getUid() {
		Class<? extends T> ingredientClass = getIngredientClass();
		return ingredientClass.getName();
	}

	/**
	 * Helper to cast an unknown ingredient to this type if it matches.
	 *
	 * @since 11.5.0
	 */
	default Optional<T> castIngredient(@Nullable Object ingredient) {
		Class<? extends T> ingredientClass = getIngredientClass();
		if (ingredientClass.isInstance(ingredient)) {
			return Optional.of(ingredientClass.cast(ingredient));
		}
		return Optional.empty();
	}

	/**
	 * Helper to cast an unknown ingredient to this type if it matches.
	 *
	 * @since 19.19.5
	 */
	@Nullable
	default T getCastIngredient(@Nullable Object ingredient) {
		Class<? extends T> ingredientClass = getIngredientClass();
		if (ingredientClass.isInstance(ingredient)) {
			return ingredientClass.cast(ingredient);
		}
		return null;
	}
}
