package violet.dainty.features.recipeviewer.core.gui.overlay.bookmarks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.IClientToggleState;
import violet.dainty.features.recipeviewer.core.common.gui.JeiTooltip;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableStatic;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiKeyMapping;
import violet.dainty.features.recipeviewer.core.gui.bookmarks.BookmarkList;
import violet.dainty.features.recipeviewer.core.gui.elements.GuiIconToggleButton;
import violet.dainty.features.recipeviewer.core.gui.input.UserInput;

public class BookmarkButton extends GuiIconToggleButton {
	public static BookmarkButton create(BookmarkOverlay bookmarkOverlay, BookmarkList bookmarkList, IClientToggleState toggleState, IInternalKeyMappings keyBindings) {
		Textures textures = Internal.getTextures();
		IDrawableStatic offIcon = textures.getBookmarkButtonDisabledIcon();
		IDrawableStatic onIcon = textures.getBookmarkButtonEnabledIcon();
		return new BookmarkButton(offIcon, onIcon, bookmarkOverlay, bookmarkList, toggleState, keyBindings);
	}

	private final BookmarkOverlay bookmarkOverlay;
	private final BookmarkList bookmarkList;
	private final IClientToggleState toggleState;
	private final IInternalKeyMappings keyBindings;

	private BookmarkButton(IDrawable offIcon, IDrawable onIcon, BookmarkOverlay bookmarkOverlay, BookmarkList bookmarkList, IClientToggleState toggleState, IInternalKeyMappings keyBindings) {
		super(offIcon, onIcon);
		this.bookmarkOverlay = bookmarkOverlay;
		this.bookmarkList = bookmarkList;
		this.toggleState = toggleState;
		this.keyBindings = keyBindings;
	}

	@Override
	protected void getTooltips(JeiTooltip tooltip) {
		tooltip.add(Component.translatable("dainty.tooltip.bookmarks"));
		IJeiKeyMapping bookmarkKey = keyBindings.getBookmark();
		if (bookmarkKey.isUnbound()) {
			MutableComponent noKey = Component.translatable("dainty.tooltip.bookmarks.usage.nokey");
			tooltip.add(noKey.withStyle(ChatFormatting.RED));
		} else if (!bookmarkOverlay.hasRoom()) {
			MutableComponent notEnoughSpace = Component.translatable("dainty.tooltip.bookmarks.not.enough.space");
			tooltip.add(notEnoughSpace.withStyle(ChatFormatting.GOLD));
		} else {
			tooltip.addKeyUsageComponent(
				"dainty.tooltip.bookmarks.usage.key",
				bookmarkKey
			);
		}
	}

	@Override
	protected boolean isIconToggledOn() {
		return bookmarkOverlay.isListDisplayed();
	}

	@Override
	protected boolean onMouseClicked(UserInput input) {
		if (!bookmarkList.isEmpty() && bookmarkOverlay.hasRoom()) {
			if (!input.isSimulate()) {
				toggleState.toggleBookmarkEnabled();
			}
			return true;
		}
		return false;
	}
}
