package violet.dainty.features.recipeviewer.core.library.load.registration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;
import violet.dainty.features.recipeviewer.core.library.ingredients.IngredientInfo;

@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class LegacyUidCodec {
	@SuppressWarnings("removal")
	public static <T> Codec<T> create(IngredientInfo<T> ingredientInfo) {
		return Codec.STRING.flatXmap(
			uid -> {
				return ingredientInfo.getIngredientByLegacyUid(uid)
					.map(DataResult::success)
					.orElseGet(() -> {
						return DataResult.error(() -> "Failed to find ingredient with uid: " + uid);
					});
			},
			ingredient -> {
				IIngredientHelper<T> ingredientHelper = ingredientInfo.getIngredientHelper();
				String uniqueId = ingredientHelper.getUniqueId(ingredient, UidContext.Ingredient);
				return DataResult.success(uniqueId);
			}
		);
	}
}
