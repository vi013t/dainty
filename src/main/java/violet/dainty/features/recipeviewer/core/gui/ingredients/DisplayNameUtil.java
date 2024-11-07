package violet.dainty.features.recipeviewer.core.gui.ingredients;

import violet.dainty.features.recipeviewer.core.common.util.StringUtil;
import violet.dainty.features.recipeviewer.core.common.util.Translator;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;

public final class DisplayNameUtil {
	private DisplayNameUtil() {
	}

	public static <T> String getLowercaseDisplayNameForSearch(T ingredient, IIngredientHelper<T> ingredientHelper) {
		String displayName = ingredientHelper.getDisplayName(ingredient);
		displayName = StringUtil.removeChatFormatting(displayName);
		displayName = Translator.toLowercaseWithLocale(displayName);
		return displayName;
	}

}
