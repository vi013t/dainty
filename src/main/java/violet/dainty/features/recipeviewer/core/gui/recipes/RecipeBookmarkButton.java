package violet.dainty.features.recipeviewer.core.gui.recipes;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.commonapi.gui.IRecipeLayoutDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.BookmarkList;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.IBookmark;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.RecipeBookmark;
import violet.dainty.features.recipeviewer.core.gui.elements.GuiIconToggleButton;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

public class RecipeBookmarkButton extends GuiIconToggleButton {
	private final BookmarkList bookmarks;
	private final @Nullable IBookmark recipeBookmark;
	private boolean bookmarked;

	public static RecipeBookmarkButton create(
		IRecipeLayoutDrawable<?> recipeLayout,
		IIngredientManager ingredientManager,
		BookmarkList bookmarks
	) {
		IBookmark recipeBookmark = RecipeBookmark.create(recipeLayout, ingredientManager);
		return create(recipeLayout, bookmarks, recipeBookmark);
	}

	public static RecipeBookmarkButton create(
		IRecipeLayoutDrawable<?> recipeLayout,
		BookmarkList bookmarks,
		@Nullable IBookmark recipeBookmark
	) {
		Textures textures = Internal.getTextures();
		IDrawable icon = textures.getRecipeBookmark();
		Rect2i area = recipeLayout.getRecipeBookmarkButtonArea();
		Rect2i layoutArea = recipeLayout.getRect();
		area.setX(area.getX() + layoutArea.getX());
		area.setY(area.getY() + layoutArea.getY());

		RecipeBookmarkButton recipeBookmarkButton = new RecipeBookmarkButton(icon, bookmarks, recipeBookmark);
		recipeBookmarkButton.updateBounds(area);
		return recipeBookmarkButton;
	}

	private RecipeBookmarkButton(IDrawable icon, BookmarkList bookmarks, @Nullable IBookmark recipeBookmark) {
		super(icon, icon);

		this.bookmarks = bookmarks;
		this.recipeBookmark = recipeBookmark;

		if (recipeBookmark == null) {
			button.active = false;
			button.visible = false;
		}

		tick();
	}

	@Override
	protected void getTooltips(JeiTooltip tooltip) {
		if (recipeBookmark != null) {
			if (bookmarks.contains(recipeBookmark)) {
				tooltip.add(Component.translatable("dainty.tooltip.bookmarks.recipe.remove"));
			} else {
				tooltip.add(Component.translatable("dainty.tooltip.bookmarks.recipe.add"));
			}
		}
	}

	@Override
	public void tick() {
		bookmarked = recipeBookmark != null && bookmarks.contains(recipeBookmark);
	}

	@Override
	protected boolean isIconToggledOn() {
		return bookmarked;
	}

	@Override
	protected boolean onMouseClicked(UserInput input) {
		if (recipeBookmark != null) {
			if (!input.isSimulate()) {
				bookmarks.toggleBookmark(recipeBookmark);
			}
			return true;
		}
		return false;
	}

	@Override
	public void draw(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.draw(guiGraphics, mouseX, mouseY, partialTicks);
		if (bookmarked) {
			guiGraphics.fill(
				RenderType.gui(),
				button.getX(),
				button.getY(),
				button.getX() + button.getWidth(),
				button.getY() + button.getHeight(),
				0x1100FF00
			);
		}
	}

	public boolean isBookmarked() {
		return bookmarked;
	}
}
