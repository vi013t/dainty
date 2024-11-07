package violet.dainty.features.recipeviewer.core.library.load.registration;

import com.google.common.collect.ImmutableListMultimap;

import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.IRecipeManagerPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.IRecipeManagerPluginHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.advanced.ISimpleRecipeManagerPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.IRecipeCategoryDecorator;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IAdvancedRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiFeatures;
import violet.dainty.features.recipeviewer.core.core.collect.ListMultiMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public class AdvancedRegistration implements IAdvancedRegistration {
	private static final Logger LOGGER = LogManager.getLogger();

	private final List<IRecipeManagerPlugin> recipeManagerPlugins = new ArrayList<>();
	private final ListMultiMap<RecipeType<?>, IRecipeCategoryDecorator<?>> recipeCategoryDecorators = new ListMultiMap<>();
	private final IJeiHelpers jeiHelpers;
	private final IJeiFeatures jeiFeatures;
	private final IRecipeManagerPluginHelper pluginHelper;

	public AdvancedRegistration(IJeiHelpers jeiHelpers, IJeiFeatures jeiFeatures, IRecipeManagerPluginHelper pluginHelper) {
		this.jeiHelpers = jeiHelpers;
		this.jeiFeatures = jeiFeatures;
		this.pluginHelper = pluginHelper;
	}

	@Override
	public void addRecipeManagerPlugin(IRecipeManagerPlugin recipeManagerPlugin) {
		ErrorUtil.checkNotNull(recipeManagerPlugin, "recipeManagerPlugin");

		LOGGER.info("Added recipe manager plugin: {}", recipeManagerPlugin.getClass());
		recipeManagerPlugins.add(recipeManagerPlugin);
	}

	@Override
	public <T> void addTypedRecipeManagerPlugin(RecipeType<T> recipeType, ISimpleRecipeManagerPlugin<T> recipeManagerPlugin) {
		ErrorUtil.checkNotNull(recipeType, "recipeType");
		ErrorUtil.checkNotNull(recipeManagerPlugin, "recipeManagerPlugin");

		TypedRecipeManagerPluginAdapter<T> adapter = new TypedRecipeManagerPluginAdapter<>(pluginHelper, recipeType, recipeManagerPlugin);
		LOGGER.info("Added typed recipe manager plugin: {}", recipeManagerPlugin.getClass());
		recipeManagerPlugins.add(adapter);
	}

	@Override
	public <T> void addRecipeCategoryDecorator(RecipeType<T> recipeType, IRecipeCategoryDecorator<T> decorator) {
		ErrorUtil.checkNotNull(recipeType, "recipeType");
		ErrorUtil.checkNotNull(decorator, "decorator");

		LOGGER.info("Added recipe category decorator: {} for recipe type: {}", decorator.getClass(), recipeType.getUid());
		recipeCategoryDecorators.put(recipeType, decorator);
	}

	@Override
	public IJeiHelpers getJeiHelpers() {
		return jeiHelpers;
	}

	@Override
	public IJeiFeatures getJeiFeatures() {
		return jeiFeatures;
	}

	@Override
	public IRecipeManagerPluginHelper getRecipeManagerPluginHelper() {
		return pluginHelper;
	}

	@Unmodifiable
	public List<IRecipeManagerPlugin> getRecipeManagerPlugins() {
		return List.copyOf(recipeManagerPlugins);
	}

	@Unmodifiable
	public ImmutableListMultimap<RecipeType<?>, IRecipeCategoryDecorator<?>> getRecipeCategoryDecorators() {
		return recipeCategoryDecorators.toImmutable();
	}
}
