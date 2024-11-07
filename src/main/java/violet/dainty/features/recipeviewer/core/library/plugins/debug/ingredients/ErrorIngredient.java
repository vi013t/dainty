package violet.dainty.features.recipeviewer.core.library.plugins.debug.ingredients;

import com.mojang.serialization.Codec;

import violet.dainty.features.recipeviewer.core.common.codecs.EnumCodec;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;

public record ErrorIngredient(CrashType crashType) {
	public static final IIngredientType<ErrorIngredient> TYPE = () -> ErrorIngredient.class;

	public static final Codec<ErrorIngredient> CODEC = EnumCodec.create(CrashType.class)
		.xmap(ErrorIngredient::new, ErrorIngredient::crashType);

	public enum CrashType {
		RenderBreakVertexBufferCrash, TooltipCrash
	}
}
