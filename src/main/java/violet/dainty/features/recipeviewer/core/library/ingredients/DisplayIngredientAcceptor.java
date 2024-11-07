package violet.dainty.features.recipeviewer.core.library.ingredients;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformFluidHelperInternal;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IIngredientAcceptor;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientTypeWithSubtypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DisplayIngredientAcceptor implements IIngredientAcceptor<DisplayIngredientAcceptor> {
	private final IIngredientManager ingredientManager;
	/**
	 * A list of ingredients, including "blank" ingredients represented by {@link Optional#empty()}.
	 * Blank ingredients are drawn as "nothing" in a rotation of ingredients, but aren't considered in lookups.
	 */
	private final List<@Nullable ITypedIngredient<?>> ingredients = new ArrayList<>();

	public DisplayIngredientAcceptor(IIngredientManager ingredientManager) {
		this.ingredientManager = ingredientManager;
	}

	@Override
	public DisplayIngredientAcceptor addIngredientsUnsafe(List<?> ingredients) {
		Preconditions.checkNotNull(ingredients, "ingredients");

		for (Object ingredient : ingredients) {
			@Nullable ITypedIngredient<?> typedIngredient = TypedIngredient.createAndFilterInvalid(ingredientManager, ingredient, false);
			this.ingredients.add(typedIngredient);
		}

		return this;
	}

	@Override
	public <T> DisplayIngredientAcceptor addIngredients(IIngredientType<T> ingredientType, List<@Nullable T> ingredients) {
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		Preconditions.checkNotNull(ingredients, "ingredients");

		List<@Nullable ITypedIngredient<T>> typedIngredients = TypedIngredient.createAndFilterInvalidList(ingredientManager, ingredientType, ingredients, false);
		this.ingredients.addAll(typedIngredients);

		return this;
	}

	@Override
	public DisplayIngredientAcceptor addIngredients(Ingredient ingredient) {
		Preconditions.checkNotNull(ingredient, "ingredient");

		List<@Nullable ITypedIngredient<ItemStack>> typedIngredients = TypedIngredient.createAndFilterInvalidList(ingredientManager, ingredient, false);
		this.ingredients.addAll(typedIngredients);

		return this;
	}

	@Override
	public <T> DisplayIngredientAcceptor addIngredient(IIngredientType<T> ingredientType, T ingredient) {
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(ingredient, "ingredient");

		addIngredientInternal(ingredientType, ingredient);
		return this;
	}

	@Override
	public <I> DisplayIngredientAcceptor addTypedIngredient(ITypedIngredient<I> typedIngredient) {
		ErrorUtil.checkNotNull(typedIngredient, "typedIngredient");

		@Nullable ITypedIngredient<I> copy = TypedIngredient.defensivelyCopyTypedIngredientFromApi(ingredientManager, typedIngredient);
		this.ingredients.add(copy);

		return this;
	}

	@SuppressWarnings("deprecation")
	@Override
	public DisplayIngredientAcceptor addFluidStack(Fluid fluid) {
		IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
		return addFluidInternal(fluidHelper, fluid.builtInRegistryHolder(), fluidHelper.bucketVolume(), DataComponentPatch.EMPTY);
	}

	@SuppressWarnings("deprecation")
	@Override
	public DisplayIngredientAcceptor addFluidStack(Fluid fluid, long amount) {
		IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
		return addFluidInternal(fluidHelper, fluid.builtInRegistryHolder(), amount, DataComponentPatch.EMPTY);
	}

	@SuppressWarnings("deprecation")
	@Override
	public DisplayIngredientAcceptor addFluidStack(Fluid fluid, long amount, DataComponentPatch componentPatch) {
		IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
		return addFluidInternal(fluidHelper, fluid.builtInRegistryHolder(), amount, componentPatch);
	}

	private <T> DisplayIngredientAcceptor addFluidInternal(IPlatformFluidHelperInternal<T> fluidHelper, Holder<Fluid> fluid, long amount, DataComponentPatch tag) {
		T fluidStack = fluidHelper.create(fluid, amount, tag);
		IIngredientTypeWithSubtypes<Fluid, T> fluidIngredientType = fluidHelper.getFluidIngredientType();
		addIngredientInternal(fluidIngredientType, fluidStack);
		return this;
	}

	@Override
	public DisplayIngredientAcceptor addTypedIngredients(List<ITypedIngredient<?>> ingredients) {
		ErrorUtil.checkNotNull(ingredients, "ingredients");

		for (ITypedIngredient<?> typedIngredient : ingredients) {
			this.addTypedIngredient(typedIngredient);
		}
		return this;
	}

	@Override
	public DisplayIngredientAcceptor addOptionalTypedIngredients(List<Optional<ITypedIngredient<?>>> ingredients) {
		ErrorUtil.checkNotNull(ingredients, "ingredients");

		for (Optional<ITypedIngredient<?>> o : ingredients) {
			if (o.isPresent()) {
				this.addTypedIngredient(o.get());
			} else {
				this.ingredients.add(null);
			}
		}

		return this;
	}

	private <T> void addIngredientInternal(IIngredientType<T> ingredientType, @Nullable T ingredient) {
		@Nullable ITypedIngredient<T> result = TypedIngredient.createAndFilterInvalid(ingredientManager, ingredientType, ingredient, false);
		this.ingredients.add(result);
	}

	@UnmodifiableView
	public List<@Nullable ITypedIngredient<?>> getAllIngredients() {
		return Collections.unmodifiableList(this.ingredients);
	}

	public IntSet getMatches(IFocusGroup focusGroup, RecipeIngredientRole role) {
		List<IFocus<?>> focuses = focusGroup.getFocuses(role).toList();
		IntSet results = new IntOpenHashSet();
		for (IFocus<?> focus : focuses) {
			getMatches(focus, results);
		}
		return results;
	}

	private <T> void getMatches(IFocus<T> focus, IntSet results) {
		List<@Nullable ITypedIngredient<?>> ingredients = getAllIngredients();
		if (ingredients.isEmpty()) {
			return;
		}

		ITypedIngredient<T> focusValue = focus.getTypedValue();
		IIngredientType<T> ingredientType = focusValue.getType();
		IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(ingredientType);
		Object focusUid = ingredientHelper.getUid(focusValue, UidContext.Ingredient);

		for (int i = 0; i < ingredients.size(); i++) {
			@Nullable ITypedIngredient<?> typedIngredient = ingredients.get(i);
			if (typedIngredient == null) {
				continue;
			}
			@Nullable ITypedIngredient<T> ingredient = typedIngredient.cast(ingredientType);
			if (ingredient == null) {
				continue;
			}
			Object uniqueId = ingredientHelper.getUid(ingredient, UidContext.Ingredient);
			if (focusUid.equals(uniqueId)) {
				results.add(i);
			}
		}
	}
}
