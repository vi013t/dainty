package violet.dainty.features.recipeviewer.core.gui.config.file.serializers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.config.file.serializers.DeserializeResult;
import violet.dainty.features.recipeviewer.core.common.config.file.serializers.LegacyTypedIngredientSerializer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusFactory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.config.IJeiConfigValueSerializer;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.RecipeBookmark;

@Deprecated
public class LegacyRecipeBookmarkSerializer {
	private static final String SEPARATOR = "#";

	private final IRecipeManager recipeManager;
	private final IFocusFactory focusFactory;
	private final LegacyTypedIngredientSerializer ingredientSerializer;

	public LegacyRecipeBookmarkSerializer(
		IRecipeManager recipeManager,
		IFocusFactory focusFactory,
		LegacyTypedIngredientSerializer ingredientSerializer
	) {
		this.recipeManager = recipeManager;
		this.focusFactory = focusFactory;
		this.ingredientSerializer = ingredientSerializer;
	}

	public IJeiConfigValueSerializer.IDeserializeResult<RecipeBookmark<?, ?>> deserialize(String string) {
		String[] parts = string.split(SEPARATOR);
		if (parts.length != 3) {
			String error = "string must be 3 parts";
			return new DeserializeResult<>(null, error);
		}
		ResourceLocation recipeTypeUid;
		try {
			recipeTypeUid = ResourceLocation.parse(parts[0]);
		} catch (RuntimeException e) {
			String error = "recipe type uid must be a valid resource location: %s\n%s".formatted(string, e.getMessage());
			return new DeserializeResult<>(null, error);
		}
		ResourceLocation recipeUid;
		try {
			recipeUid = ResourceLocation.parse(parts[1]);
		} catch (RuntimeException e) {
			String error = "recipe uid must be a valid resource location: %s\n%s".formatted(string, e.getMessage());
			return new DeserializeResult<>(null, error);
		}
		IJeiConfigValueSerializer.IDeserializeResult<ITypedIngredient<?>> deserialized = ingredientSerializer.deserialize(parts[2]);
		Optional<ITypedIngredient<?>> outputResult = deserialized.getResult();
		if (outputResult.isEmpty()) {
			List<String> errors = deserialized.getErrors();
			return new DeserializeResult<>(null, errors);
		}
		Optional<RecipeType<?>> recipeTypeResult = recipeManager.getRecipeType(recipeTypeUid);
		if (recipeTypeResult.isEmpty()) {
			String error = "could not find a recipe type matching the given uid: %s".formatted(recipeTypeUid);
			return new DeserializeResult<>(null, error);
		}

		ITypedIngredient<?> output = outputResult.get();
		RecipeType<?> recipeType = recipeTypeResult.get();

		IRecipeCategory<?> recipeCategory = recipeManager.getRecipeCategory(recipeType);
		return createBookmark(string, recipeCategory, recipeUid, output);
	}

	private <T> DeserializeResult<RecipeBookmark<?, ?>> createBookmark(String string, IRecipeCategory<T> recipeCategory, ResourceLocation recipeUid, ITypedIngredient<?> output) {
		IFocus<?> focus = focusFactory.createFocus(RecipeIngredientRole.OUTPUT, output);

		Optional<T> recipeResult = findRecipe(recipeCategory, List.of(focus), recipeUid);
		if (recipeResult.isEmpty()) {
			String error = "could not find a recipe for this string: %s".formatted(string);
			return new DeserializeResult<>(null, error);
		}

		T recipe = recipeResult.get();
		RecipeBookmark<T, ?> recipeBookmark = new RecipeBookmark<>(recipeCategory, recipe, recipeUid, output, true);
		return new DeserializeResult<>(recipeBookmark);
	}

	private <T> Optional<T> findRecipe(IRecipeCategory<T> recipeCategory, List<IFocus<?>> focus, ResourceLocation recipeUid) {
		RecipeType<T> recipeType = recipeCategory.getRecipeType();
		return recipeManager.createRecipeLookup(recipeType)
			.limitFocus(focus)
			.get()
			.filter(r -> Objects.equals(recipeCategory.getRegistryName(r), recipeUid))
			.findFirst();
	}
}