package violet.dainty.features.recipeviewer.core.common.config.file;

import java.util.List;

import violet.dainty.features.recipeviewer.core.commonapi.runtime.config.IJeiConfigValueSerializer;

public interface IConfigCategoryBuilder {
	ConfigValue<Boolean> addBoolean(String path, boolean defaultValue);
	ConfigValue<Integer> addInteger(String path, int defaultValue, int minValue, int maxValue);
	<T extends Enum<T>> ConfigValue<T> addEnum(String path, T defaultValue);
	<T> ConfigValue<List<T>> addList(String path, List<T> defaultValue, IJeiConfigValueSerializer<List<T>> listSerializer);
}
