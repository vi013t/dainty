package violet.dainty.features.recipeviewer.core.common.platform;

import java.util.Optional;

import com.mojang.serialization.Codec;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IPlatformFluidHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;

public interface IPlatformFluidHelperInternal<T> extends IPlatformFluidHelper<T> {

	IIngredientRenderer<T> createRenderer(long capacity, boolean showCapacity, int width, int height);

	Optional<TextureAtlasSprite> getStillFluidSprite(T ingredient);

	Component getDisplayName(T ingredient);

	int getColorTint(T ingredient);

	long getAmount(T ingredient);

	DataComponentPatch getComponentsPatch(T ingredient);

	void getTooltip(ITooltipBuilder tooltip, T ingredient, TooltipFlag tooltipFlag);

	T copy(T ingredient);

	T copyWithAmount(T ingredient, long amount);

	T normalize(T ingredient);

	Optional<T> getContainedFluid(ITypedIngredient<?> ingredient);

	Codec<T> getCodec();
}
