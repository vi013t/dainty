package violet.dainty.features.recipeviewer.core.neoforge.input;

import violet.dainty.features.recipeviewer.core.common.input.keys.IJeiKeyMappingBuilder;
import violet.dainty.features.recipeviewer.core.common.input.keys.IJeiKeyMappingCategoryBuilder;

public class ForgeJeiKeyMappingCategoryBuilder implements IJeiKeyMappingCategoryBuilder {
	private final String category;

	public ForgeJeiKeyMappingCategoryBuilder(String category) {
		this.category = category;
	}

	@Override
	public IJeiKeyMappingBuilder createMapping(String description) {
		return new ForgeJeiKeyMappingBuilder(category, description);
	}
}
