package violet.dainty.features.recipeviewer.core.common.config;

import java.nio.file.Path;

import violet.dainty.features.recipeviewer.core.common.config.file.ConfigSchemaBuilder;
import violet.dainty.features.recipeviewer.core.common.config.file.FileWatcher;
import violet.dainty.features.recipeviewer.core.common.config.file.IConfigSchema;
import violet.dainty.features.recipeviewer.core.common.config.file.IConfigSchemaBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.HorizontalAlignment;

public class JeiClientConfigs implements IJeiClientConfigs {
	private final IClientConfig clientConfig;
	private final IIngredientFilterConfig ingredientFilterConfig;
	private final IIngredientGridConfig ingredientListConfig;
	private final IIngredientGridConfig bookmarkListConfig;

	private final IConfigSchema schema;

	public JeiClientConfigs(Path configFile) {
		IConfigSchemaBuilder builder = new ConfigSchemaBuilder(configFile, "dainty.config.client");

		clientConfig = new ClientConfig(builder);
		ingredientFilterConfig = new IngredientFilterConfig(builder);
		ingredientListConfig = new IngredientGridConfig("ingredientList", builder, HorizontalAlignment.RIGHT);
		bookmarkListConfig = new IngredientGridConfig("bookmarkList", builder, HorizontalAlignment.LEFT);

		schema = builder.build();
	}

	public void register(FileWatcher fileWatcher, ConfigManager configManager) {
		schema.register(fileWatcher, configManager);
	}

	@Override
	public IClientConfig getClientConfig() {
		return clientConfig;
	}

	@Override
	public IIngredientFilterConfig getIngredientFilterConfig() {
		return ingredientFilterConfig;
	}

	@Override
	public IIngredientGridConfig getIngredientListConfig() {
		return ingredientListConfig;
	}

	@Override
	public IIngredientGridConfig getBookmarkListConfig() {
		return bookmarkListConfig;
	}
}
