package violet.dainty.features.recipeviewer.core.common.config.file;

public interface IConfigSchemaBuilder {

	IConfigCategoryBuilder addCategory(String name);

	IConfigSchema build();
}
