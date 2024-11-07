package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.util.RecipeErrorUtil;
import violet.dainty.features.recipeviewer.core.library.util.RecipeUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public final class CategoryRecipeValidator<T extends Recipe<?>> {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final int INVALID_COUNT = -1;
	private final IRecipeCategory<RecipeHolder<T>> recipeCategory;
	private final IIngredientManager ingredientManager;
	private final int maxInputs;

	public CategoryRecipeValidator(IRecipeCategory<RecipeHolder<T>> recipeCategory, IIngredientManager ingredientManager, int maxInputs) {
		this.recipeCategory = recipeCategory;
		this.ingredientManager = ingredientManager;
		this.maxInputs = maxInputs;
	}

	public boolean isRecipeValid(RecipeHolder<T> recipeHolder) {
		return hasValidInputsAndOutputs(recipeHolder);
	}

	public boolean isRecipeHandled(RecipeHolder<T> recipeHolder) {
		return this.recipeCategory.isHandled(recipeHolder);
	}

	@SuppressWarnings("ConstantConditions")
	private boolean hasValidInputsAndOutputs(RecipeHolder<T> recipeHolder) {
		T recipe = recipeHolder.value();
		if (recipe.isSpecial()) {
			return true;
		}
		ItemStack recipeOutput = RecipeUtil.getResultItem(recipe);
		if (recipeOutput == null || recipeOutput.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipeHolder, recipeCategory, ingredientManager);
				LOGGER.debug("Skipping Recipe because it has no output. {}", recipeInfo);
			}
			return false;
		}
		List<Ingredient> ingredients = recipe.getIngredients();
		if (ingredients == null) {
			if (LOGGER.isDebugEnabled()) {
				String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipeHolder, recipeCategory, ingredientManager);
				LOGGER.debug("Skipping Recipe because it has no input Ingredients. {}", recipeInfo);
			}
			return false;
		}
		int inputCount = getInputCount(ingredients);
		if (inputCount == INVALID_COUNT) {
			if (LOGGER.isDebugEnabled()) {
				String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipeHolder, recipeCategory, ingredientManager);
				LOGGER.debug("Skipping Recipe because it contains invalid inputs. {}", recipeInfo);
			}
			return false;
		} else if (inputCount > maxInputs) {
			if (LOGGER.isDebugEnabled()) {
				String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipeHolder, recipeCategory, ingredientManager);
				LOGGER.debug("Skipping Recipe because it has too many inputs. {}", recipeInfo);
			}
			return false;
		} else if (inputCount == 0 && maxInputs > 0) {
			if (LOGGER.isDebugEnabled()) {
				String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipeHolder, recipeCategory, ingredientManager);
				LOGGER.debug("Skipping Recipe because it has no inputs. {}", recipeInfo);
			}
			return false;
		}
		return true;
	}

	@SuppressWarnings("ConstantConditions")
	private static int getInputCount(List<Ingredient> ingredientList) {
		int inputCount = 0;
		for (Ingredient ingredient : ingredientList) {
			ItemStack[] input = ingredient.getItems();
			if (input == null) {
				return INVALID_COUNT;
			} else {
				inputCount++;
			}
		}
		return inputCount;
	}
}
