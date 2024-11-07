package violet.dainty.features.recipeviewer.core.library.ingredients;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.mojang.serialization.Codec;

import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;
import violet.dainty.features.recipeviewer.core.core.collect.ListMultiMap;
import violet.dainty.features.recipeviewer.core.library.load.registration.LegacyUidCodec;

public class IngredientInfo<T> {
	private final IIngredientType<T> ingredientType;
	private final IIngredientHelper<T> ingredientHelper;
	private final IIngredientRenderer<T> ingredientRenderer;
	private final Codec<T> ingredientCodec;
	private final IngredientSet<T> ingredientSet;
	private final ListMultiMap<Object, String> aliases;

	public IngredientInfo(
		IIngredientType<T> ingredientType,
		Collection<T> ingredients,
		IIngredientHelper<T> ingredientHelper,
		IIngredientRenderer<T> ingredientRenderer,
		@Nullable Codec<T> ingredientCodec
	) {
		if (ingredientCodec == null) {
			//noinspection deprecation
			ingredientCodec = LegacyUidCodec.create(this);
		}

		this.ingredientType = ingredientType;
		this.ingredientHelper = ingredientHelper;
		this.ingredientRenderer = ingredientRenderer;
		this.ingredientCodec = ingredientCodec;

		this.ingredientSet = new IngredientSet<>(ingredientHelper, UidContext.Ingredient);
		this.ingredientSet.addAll(ingredients);

		this.aliases = new ListMultiMap<>();
	}

	public IIngredientType<T> getIngredientType() {
		return ingredientType;
	}

	public IIngredientHelper<T> getIngredientHelper() {
		return ingredientHelper;
	}

	public IIngredientRenderer<T> getIngredientRenderer() {
		return ingredientRenderer;
	}

	public Codec<T> getIngredientCodec() {
		return ingredientCodec;
	}

	@Unmodifiable
	public Collection<T> getAllIngredients() {
		return Collections.unmodifiableCollection(ingredientSet);
	}

	public void addIngredients(Collection<T> ingredients) {
		this.ingredientSet.addAll(ingredients);
	}

	public void removeIngredients(Collection<T> ingredients) {
		this.ingredientSet.removeAll(ingredients);
	}

	@SuppressWarnings({"removal"})
	@Deprecated(forRemoval = true)
	public Optional<T> getIngredientByLegacyUid(String uid) {
		return ingredientSet.getByLegacyUid(uid);
	}

	@Unmodifiable
	public Collection<String> getIngredientAliases(ITypedIngredient<T> ingredient) {
		Object uid = ingredientHelper.getUid(ingredient, UidContext.Ingredient);
		return aliases.get(uid);
	}

	public void addIngredientAlias(T ingredient, String alias) {
		Object uid = ingredientHelper.getUid(ingredient, UidContext.Ingredient);
		this.aliases.put(uid, alias);
	}

	public void addIngredientAlias(ITypedIngredient<T> ingredient, String alias) {
		Object uid = ingredientHelper.getUid(ingredient, UidContext.Ingredient);
		this.aliases.put(uid, alias);
	}

	public void addIngredientAliases(T ingredient, Collection<String> aliases) {
		Object uid = ingredientHelper.getUid(ingredient, UidContext.Ingredient);
		this.aliases.putAll(uid, aliases);
	}

	public void addIngredientAliases(ITypedIngredient<T> ingredient, Collection<String> aliases) {
		Object uid = ingredientHelper.getUid(ingredient, UidContext.Ingredient);
		this.aliases.putAll(uid, aliases);
	}
}
