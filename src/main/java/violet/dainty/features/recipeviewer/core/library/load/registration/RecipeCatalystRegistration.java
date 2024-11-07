package violet.dainty.features.recipeviewer.core.library.load.registration;

import com.google.common.collect.ImmutableListMultimap;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeCatalystRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.core.collect.ListMultiMap;
import violet.dainty.features.recipeviewer.core.library.ingredients.TypedIngredient;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecipeCatalystRegistration implements IRecipeCatalystRegistration {
	private final ListMultiMap<RecipeType<?>, ITypedIngredient<?>> recipeCatalysts = new ListMultiMap<>();
	private final IIngredientManager ingredientManager;
	private final IJeiHelpers jeiHelpers;

	public RecipeCatalystRegistration(IIngredientManager ingredientManager, IJeiHelpers jeiHelpers) {
		this.ingredientManager = ingredientManager;
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public IIngredientManager getIngredientManager() {
		return ingredientManager;
	}

	@Override
	public IJeiHelpers getJeiHelpers() {
		return jeiHelpers;
	}

	@Override
	public <T> void addRecipeCatalyst(IIngredientType<T> ingredientType, T ingredient, RecipeType<?>... recipeTypes) {
		ErrorUtil.checkNotEmpty(recipeTypes, "recipeTypes");
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(ingredient, "ingredient");

		for (RecipeType<?> recipeType : recipeTypes) {
			ErrorUtil.checkNotNull(recipeType, "recipeType");
			@Nullable ITypedIngredient<T> typedIngredient = TypedIngredient.createAndFilterInvalid(this.ingredientManager, ingredientType, ingredient, true);
			if (typedIngredient == null) {
				throw new IllegalArgumentException("Recipe catalyst must be a valid ingredient");
			}
			this.recipeCatalysts.put(recipeType, typedIngredient);
		}
	}

	@Override
	public void addRecipeCatalysts(RecipeType<?> recipeType, ItemLike... ingredients) {
		ErrorUtil.checkNotNull(recipeType, "recipeType");
		ErrorUtil.checkNotNull(ingredients, "ingredients");

		for (ItemLike itemLike : ingredients) {
			ItemStack itemStack = itemLike.asItem().getDefaultInstance();
			@Nullable ITypedIngredient<ItemStack> typedIngredient = TypedIngredient.createAndFilterInvalid(this.ingredientManager, VanillaTypes.ITEM_STACK, itemStack, true);
			if (typedIngredient == null) {
				throw new IllegalArgumentException("Recipe catalyst must be a valid ingredient");
			}
			this.recipeCatalysts.put(recipeType, typedIngredient);
		}
	}

	@Override
	public <T> void addRecipeCatalysts(RecipeType<?> recipeType, IIngredientType<T> ingredientType, List<T> ingredients) {
		ErrorUtil.checkNotNull(recipeType, "recipeType");
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(ingredients, "ingredients");

		for (T ingredient : ingredients) {
			@Nullable ITypedIngredient<T> typedIngredient = TypedIngredient.createAndFilterInvalid(this.ingredientManager, ingredientType, ingredient, true);
			if (typedIngredient == null) {
				throw new IllegalArgumentException("Recipe catalyst must be a valid ingredient");
			}
			this.recipeCatalysts.put(recipeType, typedIngredient);
		}
	}

	public ImmutableListMultimap<RecipeType<?>, ITypedIngredient<?>> getRecipeCatalysts() {
		return recipeCatalysts.toImmutable();
	}
}
