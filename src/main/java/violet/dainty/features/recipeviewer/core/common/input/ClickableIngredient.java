package violet.dainty.features.recipeviewer.core.common.input;

import net.minecraft.client.renderer.Rect2i;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IClickableIngredient;

public class ClickableIngredient<V> implements IClickableIngredient<V> {
	private final ITypedIngredient<V> value;
	private final ImmutableRect2i area;

	public ClickableIngredient(ITypedIngredient<V> value, ImmutableRect2i area) {
		ErrorUtil.checkNotNull(value, "value");
		this.value = value;
		this.area = area;
	}

	@SuppressWarnings("removal")
	@Override
	public ITypedIngredient<V> getTypedIngredient() {
		return value;
	}

	@Override
	public IIngredientType<V> getIngredientType() {
		return value.getType();
	}

	@Override
	public V getIngredient() {
		return value.getIngredient();
	}

	@Override
	public Rect2i getArea() {
		return area.toMutable();
	}
}
