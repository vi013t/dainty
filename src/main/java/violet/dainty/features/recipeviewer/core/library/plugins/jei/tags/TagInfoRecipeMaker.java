package violet.dainty.features.recipeviewer.core.library.plugins.jei.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformRenderHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.RegistryUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.Tags;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.ingredients.TypedIngredient;

public record TagInfoRecipeMaker<B, I>(
	IIngredientType<I> ingredientType,
	RecipeType<ITagInfoRecipe> recipeType,
	Function<B, I> baseToIngredient,
	ResourceKey<? extends Registry<B>> registryKey
) {
	private static final Logger LOGGER = LogManager.getLogger();

	public void addRecipes(IRecipeRegistration registration) {
		IIngredientManager ingredientManager = registration.getIngredientManager();
		List<ITagInfoRecipe> recipes = createTagInfoRecipes(ingredientType, registryKey, baseToIngredient, ingredientManager);
		registration.addRecipes(recipeType, recipes);
	}

	private static <B, I> List<ITagInfoRecipe> createTagInfoRecipes(IIngredientType<I> ingredientType, ResourceKey<? extends Registry<B>> registryKey, Function<B, I> baseToIngredient, IIngredientManager ingredientManager) {
		Registry<B> registry = RegistryUtil.getRegistry(registryKey);
		return registry
			.getTagNames()
			.<ITagInfoRecipe>mapMulti((tagKey, acceptor) -> {
				if (tagKey.location().getPath().equals(Tags.HIDDEN_FROM_RECIPE_VIEWERS.getPath())) {
					return;
				}
				List<ITypedIngredient<I>> ingredients = getIngredients(registry, tagKey, ingredientType, baseToIngredient, ingredientManager);
				if (!ingredients.isEmpty()) {
					acceptor.accept(new TagInfoRecipe<>(tagKey, ingredients));
				} else {
					IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
					Component tagName = renderHelper.getName(tagKey);
					LOGGER.debug("No valid ingredients found for {} tag: {} ({})", registryKey.location(), tagName.getString(), tagKey.location());
				}
			})
			.toList();
	}

	private static <B, I> List<ITypedIngredient<I>> getIngredients(Registry<B> registry, TagKey<B> tagKey, IIngredientType<I> ingredientType, Function<B, I> baseToIngredient, IIngredientManager ingredientManager) {
		List<ITypedIngredient<I>> ingredients = new ArrayList<>();
		for (Holder<B> i : registry.getTagOrEmpty(tagKey)) {
			B value = i.value();
			I ingredient = baseToIngredient.apply(value);
			ITypedIngredient<I> typedIngredient = TypedIngredient.createAndFilterInvalid(ingredientManager, ingredientType, ingredient, false);
			if (typedIngredient != null) {
				ingredients.add(typedIngredient);
			}
		}

		return ingredients;
	}
}
