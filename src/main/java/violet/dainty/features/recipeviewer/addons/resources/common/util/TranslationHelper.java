package violet.dainty.features.recipeviewer.addons.resources.common.util;

import net.minecraft.client.resources.language.I18n;

public class TranslationHelper {
    public static String translateAndFormat(String key, Object... params) {
        return I18n.get(key, params);
    }

    public static String getLocalPageInfo(int page, int lastPage) {
        return translateAndFormat("dainty.page") + " " + (page + 1) + " " + translateAndFormat("dainty.of") + " " + (lastPage + 1);
    }

    public static boolean canTranslate(String key) {
        return I18n.exists(key);
    }

    public static String tryDimensionTranslate(String dimension) {
        if (TranslationHelper.canTranslate("dainty.dim." + dimension)) {
            dimension = "dainty.dim." + dimension;
        }
        return TranslationHelper.translateAndFormat(dimension);
    }
}
