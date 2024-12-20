package violet.dainty.features.recipeviewer.core.commonapi.recipe.category;

import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;

/**
 * Simple abstract implementation of {@link IRecipeCategory} to help simplify creating recipe categories.
 * @since 19.19.0
 */
public abstract class AbstractRecipeCategory<T> implements IRecipeCategory<T> {
	private final RecipeType<T> recipeType;
	private final Component title;
	private final IDrawable icon;
	private final int width;
	private final int height;

	/**
	 * @since 19.19.0
	 */
	public AbstractRecipeCategory(RecipeType<T> recipeType, Component title, IDrawable icon, int width, int height) {
		this.recipeType = recipeType;
		this.title = title;
		this.icon = icon;
		this.width = width;
		this.height = height;
	}

	@Override
	public final RecipeType<T> getRecipeType() {
		return recipeType;
	}

	@Override
	public final Component getTitle() {
		return title;
	}

	@Override
	public final IDrawable getIcon() {
		return icon;
	}

	@Override
	public final int getWidth() {
		return width;
	}

	@Override
	public final int getHeight() {
		return height;
	}
}
