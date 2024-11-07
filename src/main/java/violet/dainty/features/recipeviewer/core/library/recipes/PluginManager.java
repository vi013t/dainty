package violet.dainty.features.recipeviewer.core.library.recipes;

import com.google.common.base.Stopwatch;

import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocus;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.IRecipeManagerPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.recipes.collect.RecipeTypeData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PluginManager {
	private static final Logger LOGGER = LogManager.getLogger();

	private List<IRecipeManagerPlugin> plugins = new ArrayList<>();

	public PluginManager(IRecipeManagerPlugin internalRecipeManagerPlugin) {
		this.plugins.add(internalRecipeManagerPlugin);
	}

	public <T> Stream<T> getRecipes(RecipeTypeData<T> recipeTypeData, IFocusGroup focusGroup, boolean includeHidden) {
		IRecipeCategory<T> recipeCategory = recipeTypeData.getRecipeCategory();

		Stream<T> recipes = this.plugins.stream()
			.flatMap(p -> getPluginRecipeStream(p, recipeCategory, focusGroup))
			.distinct();

		if (!includeHidden) {
			Set<T> hiddenRecipes = recipeTypeData.getHiddenRecipes();
			Predicate<T> notHidden = ((Predicate<T>) hiddenRecipes::contains).negate();

			recipes = recipes.filter(notHidden);
		}
		return recipes;
	}

	public Stream<RecipeType<?>> getRecipeTypes(IFocusGroup focusGroup) {
		return this.plugins.stream()
			.flatMap(p -> getPluginRecipeTypeStream(p, focusGroup))
			.distinct();
	}

	private Stream<RecipeType<?>> getPluginRecipeTypeStream(IRecipeManagerPlugin plugin, IFocusGroup focuses) {
		List<IFocus<?>> allFocuses = focuses.getAllFocuses();
		return allFocuses.stream()
			.flatMap(focus -> getRecipeTypes(plugin, focus));
	}

	private <T> Stream<T> getPluginRecipeStream(IRecipeManagerPlugin plugin, IRecipeCategory<T> recipeCategory, IFocusGroup focuses) {
		if (!focuses.isEmpty()) {
			List<IFocus<?>> allFocuses = focuses.getAllFocuses();
			return allFocuses.stream()
				.flatMap(focus -> getRecipes(plugin, recipeCategory, focus));
		}
		return getRecipes(plugin, recipeCategory);
	}

	private Stream<RecipeType<?>> getRecipeTypes(IRecipeManagerPlugin plugin, IFocus<?> focus) {
		return safeCallPlugin(
			plugin,
			() -> plugin.getRecipeTypes(focus).stream(),
			Stream.of()
		);
	}

	private <T> Stream<T> getRecipes(IRecipeManagerPlugin plugin, IRecipeCategory<T> recipeCategory) {
		return safeCallPlugin(
			plugin,
			() -> plugin.getRecipes(recipeCategory).stream(),
			Stream.of()
		);
	}

	private <T> Stream<T> getRecipes(IRecipeManagerPlugin plugin, IRecipeCategory<T> recipeCategory, IFocus<?> focus) {
		return safeCallPlugin(
			plugin,
			() -> plugin.getRecipes(recipeCategory, focus).stream(),
			Stream.of()
		);
	}

	private <T> T safeCallPlugin(IRecipeManagerPlugin plugin, Supplier<T> supplier, T defaultValue) {
		Stopwatch stopWatch = Stopwatch.createStarted();
		try {
			T result = supplier.get();
			stopWatch.stop();
			if (stopWatch.elapsed(TimeUnit.MILLISECONDS) > 10) {
				LOGGER.warn("Recipe registry plugin is slow, took {}. {}", stopWatch, plugin.getClass());
			}
			return result;
		} catch (RuntimeException | LinkageError e) {
			LOGGER.error("Recipe registry plugin crashed, it is being disabled: {}", plugin.getClass(), e);
			// make a copy, in order to avoid modifying the current stream
			this.plugins = new ArrayList<>(this.plugins);
			this.plugins.remove(plugin);
			return defaultValue;
		}
	}

	public void addAll(List<IRecipeManagerPlugin> plugins) {
		this.plugins.addAll(plugins);
	}
}