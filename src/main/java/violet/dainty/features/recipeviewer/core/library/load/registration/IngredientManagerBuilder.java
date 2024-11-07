package violet.dainty.features.recipeviewer.core.library.load.registration;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;

import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IColorHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeManager;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IExtraIngredientRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IIngredientAliasRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IModIngredientRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.ingredients.IngredientInfo;
import violet.dainty.features.recipeviewer.core.library.ingredients.IngredientManager;
import violet.dainty.features.recipeviewer.core.library.ingredients.RegisteredIngredients;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.SequencedMap;

public class IngredientManagerBuilder implements IModIngredientRegistration, IIngredientAliasRegistration, IExtraIngredientRegistration {
	private final SequencedMap<IIngredientType<?>, IngredientInfo<?>> ingredientInfos = new LinkedHashMap<>();
	private final ISubtypeManager subtypeManager;
	private final IColorHelper colorHelper;

	public IngredientManagerBuilder(ISubtypeManager subtypeManager, IColorHelper colorHelper) {
		this.subtypeManager = subtypeManager;
		this.colorHelper = colorHelper;
	}

	@SuppressWarnings("removal")
	@Override
	public <V> void register(IIngredientType<V> ingredientType, Collection<V> allIngredients, IIngredientHelper<V> ingredientHelper, IIngredientRenderer<V> ingredientRenderer) {
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(allIngredients, "allIngredients");
		ErrorUtil.checkNotNull(ingredientHelper, "ingredientHelper");
		ErrorUtil.checkNotNull(ingredientRenderer, "ingredientRenderer");
		Preconditions.checkArgument(ingredientRenderer.getWidth() == 16,
			"the default ingredient renderer registered here will be used for drawing " +
				"ingredients in the ingredient list, and it must have a width of 16"
		);
		Preconditions.checkArgument(ingredientRenderer.getHeight() == 16,
			"the default ingredient renderer registered here will be used for drawing " +
				"ingredients in the ingredient list, and it must have a height of 16"
		);
		if (ingredientInfos.containsKey(ingredientType)) {
			throw new IllegalArgumentException("Ingredient type has already been registered: " + ingredientType.getUid());
		}

		ingredientInfos.put(ingredientType, new IngredientInfo<>(ingredientType, allIngredients, ingredientHelper, ingredientRenderer, null));
	}

	@Override
	public <V> void register(
		IIngredientType<V> ingredientType,
		Collection<V> allIngredients,
		IIngredientHelper<V> ingredientHelper,
		IIngredientRenderer<V> ingredientRenderer,
		Codec<V> ingredientCodec
	) {
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(allIngredients, "allIngredients");
		ErrorUtil.checkNotNull(ingredientHelper, "ingredientHelper");
		ErrorUtil.checkNotNull(ingredientRenderer, "ingredientRenderer");
		ErrorUtil.checkNotNull(ingredientCodec, "ingredientCodec");
		Preconditions.checkArgument(ingredientRenderer.getWidth() == 16,
			"the default ingredient renderer registered here will be used for drawing " +
				"ingredients in the ingredient list, and it must have a width of 16"
		);
		Preconditions.checkArgument(ingredientRenderer.getHeight() == 16,
			"the default ingredient renderer registered here will be used for drawing " +
				"ingredients in the ingredient list, and it must have a height of 16"
		);

		if (ingredientInfos.containsKey(ingredientType)) {
			throw new IllegalArgumentException("Ingredient type has already been registered: " + ingredientType.getIngredientClass());
		}

		ingredientInfos.put(ingredientType, new IngredientInfo<>(ingredientType, allIngredients, ingredientHelper, ingredientRenderer, ingredientCodec));
	}

	@Override
	public <V> void addExtraIngredients(IIngredientType<V> ingredientType, Collection<V> extraIngredients) {
		ErrorUtil.checkNotNull(ingredientType, "ingredientType");
		ErrorUtil.checkNotNull(extraIngredients, "extraIngredients");

		IngredientInfo<?> ingredientInfo = ingredientInfos.get(ingredientType);
		if (ingredientInfo == null) {
			throw new IllegalArgumentException("Ingredient type has not been registered: " + ingredientType.getUid());
		}
		@SuppressWarnings("unchecked")
		IngredientInfo<V> castIngredientInfo = (IngredientInfo<V>) ingredientInfo;
		castIngredientInfo.addIngredients(extraIngredients);
	}

	@Override
	public <I> void addAlias(IIngredientType<I> type, I ingredient, String alias) {
		ErrorUtil.checkNotNull(type, "type");
		ErrorUtil.checkNotNull(ingredient, "ingredient");
		ErrorUtil.checkNotNull(alias, "alias");

		@SuppressWarnings("unchecked")
		IngredientInfo<I> ingredientInfo = (IngredientInfo<I>) ingredientInfos.get(type);
		ingredientInfo.addIngredientAlias(ingredient, alias);
	}

	@Override
	public <I> void addAlias(ITypedIngredient<I> typedIngredient, String alias) {
		ErrorUtil.checkNotNull(typedIngredient, "typedIngredient");
		ErrorUtil.checkNotNull(alias, "alias");

		@SuppressWarnings("unchecked")
		IngredientInfo<I> ingredientInfo = (IngredientInfo<I>) ingredientInfos.get(typedIngredient.getType());
		ingredientInfo.addIngredientAlias(typedIngredient, alias);
	}

	@Override
	public <I> void addAliases(IIngredientType<I> type, I ingredient, Collection<String> aliases) {
		ErrorUtil.checkNotNull(type, "type");
		ErrorUtil.checkNotNull(ingredient, "ingredient");
		ErrorUtil.checkNotNull(aliases, "aliases");

		@SuppressWarnings("unchecked")
		IngredientInfo<I> ingredientInfo = (IngredientInfo<I>) ingredientInfos.get(type);
		ingredientInfo.addIngredientAliases(ingredient, aliases);
	}

	@Override
	public <I> void addAliases(ITypedIngredient<I> typedIngredient, Collection<String> aliases) {
		ErrorUtil.checkNotNull(typedIngredient, "typedIngredient");
		ErrorUtil.checkNotNull(aliases, "aliases");

		@SuppressWarnings("unchecked")
		IngredientInfo<I> ingredientInfo = (IngredientInfo<I>) ingredientInfos.get(typedIngredient.getType());
		ingredientInfo.addIngredientAliases(typedIngredient, aliases);
	}

	@Override
	public <I> void addAliases(IIngredientType<I> type, Collection<I> ingredients, String alias) {
		ErrorUtil.checkNotNull(type, "type");
		ErrorUtil.checkNotNull(ingredients, "ingredients");
		ErrorUtil.checkNotNull(alias, "alias");

		@SuppressWarnings("unchecked")
		IngredientInfo<I> ingredientInfo = (IngredientInfo<I>) ingredientInfos.get(type);
		for (I ingredient : ingredients) {
			ingredientInfo.addIngredientAlias(ingredient, alias);
		}
	}

	@Override
	public <I> void addAliases(Collection<ITypedIngredient<I>> typedIngredients, String alias) {
		ErrorUtil.checkNotNull(typedIngredients, "typedIngredients");
		ErrorUtil.checkNotNull(alias, "alias");

		IngredientInfo<I> ingredientInfo = null;
		for (ITypedIngredient<I> typedIngredient : typedIngredients) {
			IIngredientType<I> ingredientType = typedIngredient.getType();
			if (ingredientInfo == null) {
				//noinspection unchecked
				ingredientInfo = (IngredientInfo<I>) ingredientInfos.get(ingredientType);
			}
			ingredientInfo.addIngredientAlias(typedIngredient, alias);
		}
	}

	@Override
	public <I> void addAliases(IIngredientType<I> type, Collection<I> ingredients, Collection<String> aliases) {
		ErrorUtil.checkNotNull(type, "type");
		ErrorUtil.checkNotNull(ingredients, "ingredients");
		ErrorUtil.checkNotNull(aliases, "aliases");

		@SuppressWarnings("unchecked")
		IngredientInfo<I> ingredientInfo = (IngredientInfo<I>) ingredientInfos.get(type);
		for (I ingredient : ingredients) {
			ingredientInfo.addIngredientAliases(ingredient, aliases);
		}
	}

	@Override
	public <I> void addAliases(Collection<ITypedIngredient<I>> typedIngredients, Collection<String> aliases) {
		ErrorUtil.checkNotNull(typedIngredients, "typedIngredients");
		ErrorUtil.checkNotNull(aliases, "aliases");

		IngredientInfo<I> ingredientInfo = null;
		for (ITypedIngredient<I> typedIngredient : typedIngredients) {
			IIngredientType<I> ingredientType = typedIngredient.getType();
			if (ingredientInfo == null) {
				//noinspection unchecked
				ingredientInfo = (IngredientInfo<I>) ingredientInfos.get(ingredientType);
			}
			ingredientInfo.addIngredientAliases(typedIngredient, aliases);
		}
	}

	@Override
	public ISubtypeManager getSubtypeManager() {
		return subtypeManager;
	}

	@Override
	public IColorHelper getColorHelper() {
		return colorHelper;
	}

	public IIngredientManager build() {
		RegisteredIngredients registeredIngredients = new RegisteredIngredients(ingredientInfos);
		return new IngredientManager(registeredIngredients);
	}
}
