package violet.dainty.features.recipeviewer.core.gui.config;

import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import violet.dainty.features.recipeviewer.core.common.input.IInternalKeyMappings;
import violet.dainty.features.recipeviewer.core.common.input.keys.IJeiKeyMappingCategoryBuilder;
import violet.dainty.features.recipeviewer.core.common.input.keys.JeiKeyConflictContext;
import violet.dainty.features.recipeviewer.core.common.input.keys.JeiKeyModifier;
import violet.dainty.features.recipeviewer.core.common.input.keys.JeiMultiKeyMapping;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformInputHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.Translator;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiKeyMapping;

public final class InternalKeyMappings implements IInternalKeyMappings {
	private final IJeiKeyMapping toggleOverlay;
	private final IJeiKeyMapping focusSearch;
	private final IJeiKeyMapping toggleCheatMode;
	private final IJeiKeyMapping toggleEditMode;

	private final IJeiKeyMapping toggleCheatModeConfigButton;

	private final IJeiKeyMapping recipeBack;
	private final IJeiKeyMapping previousCategory;
	private final IJeiKeyMapping nextCategory;
	private final IJeiKeyMapping previousRecipePage;
	private final IJeiKeyMapping nextRecipePage;

	private final IJeiKeyMapping previousPage;
	private final IJeiKeyMapping nextPage;

	private final IJeiKeyMapping bookmark;
	private final IJeiKeyMapping toggleBookmarkOverlay;
	private final IJeiKeyMapping transferRecipeBookmark;
	private final IJeiKeyMapping maxTransferRecipeBookmark;

	private final IJeiKeyMapping showRecipe;
	private final IJeiKeyMapping showUses;

	private final IJeiKeyMapping cheatOneItem;
	private final IJeiKeyMapping cheatItemStack;

	private final IJeiKeyMapping toggleHideIngredient;
	private final IJeiKeyMapping toggleWildcardHideIngredient;

	private final IJeiKeyMapping hoveredClearSearchBar;
	private final IJeiKeyMapping previousSearch;
	private final IJeiKeyMapping nextSearch;

	private final IJeiKeyMapping copyRecipeId;

	private final IJeiKeyMapping closeRecipeGui;

	// internal only, unregistered and can't be changed because they match vanilla Minecraft hard-coded keys:
	private final IJeiKeyMapping escapeKey;
	private final IJeiKeyMapping leftClick;
	private final IJeiKeyMapping rightClick;
	private final IJeiKeyMapping enterKey;

	public InternalKeyMappings(Consumer<KeyMapping> registerMethod) {
		IPlatformInputHelper inputHelper = Services.PLATFORM.getInputHelper();

		IJeiKeyMapping showRecipe1;
		IJeiKeyMapping showRecipe2;
		IJeiKeyMapping showUses1;
		IJeiKeyMapping showUses2;
		IJeiKeyMapping cheatOneItem1;
		IJeiKeyMapping cheatOneItem2;
		IJeiKeyMapping cheatItemStack1;
		IJeiKeyMapping cheatItemStack2;

		String overlaysCategoryName = Translator.translateToLocal("dainty.key.category.overlays");
		IJeiKeyMappingCategoryBuilder overlay = inputHelper.createKeyMappingCategoryBuilder(overlaysCategoryName);

		String mouseHoverCategoryName = Translator.translateToLocal("dainty.key.category.mouse.hover");
		IJeiKeyMappingCategoryBuilder mouseHover = inputHelper.createKeyMappingCategoryBuilder(mouseHoverCategoryName);

		String searchCategoryName = Translator.translateToLocal("dainty.key.category.search");
		IJeiKeyMappingCategoryBuilder search = inputHelper.createKeyMappingCategoryBuilder(searchCategoryName);

		String cheatModeCategoryName = Translator.translateToLocal("dainty.key.category.cheat.mode");
		IJeiKeyMappingCategoryBuilder cheat = inputHelper.createKeyMappingCategoryBuilder(cheatModeCategoryName);

		String hoverConfigButtonCategoryName = Translator.translateToLocal("dainty.key.category.hover.config.button");
		IJeiKeyMappingCategoryBuilder hoverConfig = inputHelper.createKeyMappingCategoryBuilder(hoverConfigButtonCategoryName);

		String editModeCategoryName = Translator.translateToLocal("dainty.key.category.edit.mode");
		IJeiKeyMappingCategoryBuilder editMode = inputHelper.createKeyMappingCategoryBuilder(editModeCategoryName);

		String recipeCategoryName = Translator.translateToLocal("dainty.key.category.recipe.gui");
		IJeiKeyMappingCategoryBuilder recipeCategory = inputHelper.createKeyMappingCategoryBuilder(recipeCategoryName);

		String devToolsCategoryName = Translator.translateToLocal("dainty.key.category.dev.tools");
		IJeiKeyMappingCategoryBuilder devTools = inputHelper.createKeyMappingCategoryBuilder(devToolsCategoryName);

		// Overlay
		toggleOverlay = overlay.createMapping("key.dainty.toggleOverlay")
			.setContext(JeiKeyConflictContext.GUI)
			.setModifier(JeiKeyModifier.CONTROL_OR_COMMAND)
			.buildKeyboardKey(GLFW.GLFW_KEY_O)
			.register(registerMethod);

		focusSearch = overlay.createMapping("key.dainty.focusSearch")
			.setContext(JeiKeyConflictContext.GUI)
			.setModifier(JeiKeyModifier.CONTROL_OR_COMMAND)
			.buildKeyboardKey(GLFW.GLFW_KEY_F)
			.register(registerMethod);

		previousPage = overlay.createMapping("key.dainty.previousPage")
			.setContext(JeiKeyConflictContext.GUI)
			.buildUnbound()
			.register(registerMethod);

		nextPage = overlay.createMapping("key.dainty.nextPage")
			.setContext(JeiKeyConflictContext.GUI)
			.buildUnbound()
			.register(registerMethod);

		toggleBookmarkOverlay = overlay.createMapping("key.dainty.toggleBookmarkOverlay")
			.setContext(JeiKeyConflictContext.GUI)
			.buildUnbound()
			.register(registerMethod);

		// Mouse Hover
		bookmark = mouseHover.createMapping("key.dainty.bookmark")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER)
			.buildKeyboardKey(GLFW.GLFW_KEY_A)
			.register(registerMethod);

		showRecipe1 = mouseHover.createMapping("key.dainty.showRecipe")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER)
			.buildKeyboardKey(GLFW.GLFW_KEY_R)
			.register(registerMethod);

		showRecipe2 = mouseHover.createMapping("key.dainty.showRecipe2")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER)
			.buildMouseLeft()
			.register(registerMethod);

		showUses1 = mouseHover.createMapping("key.dainty.showUses")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER)
			.buildKeyboardKey(GLFW.GLFW_KEY_U)
			.register(registerMethod);

		showUses2 = mouseHover.createMapping("key.dainty.showUses2")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER)
			.buildMouseRight()
			.register(registerMethod);

		transferRecipeBookmark = mouseHover.createMapping("key.dainty.transferRecipeBookmark")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER)
			.setModifier(JeiKeyModifier.SHIFT)
			.buildMouseLeft()
			.register(registerMethod);

		maxTransferRecipeBookmark = mouseHover.createMapping("key.dainty.maxTransferRecipeBookmark")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER)
			.setModifier(JeiKeyModifier.CONTROL_OR_COMMAND)
			.buildMouseLeft()
			.register(registerMethod);

		// Search Bar
		hoveredClearSearchBar = search.createMapping("key.dainty.clearSearchBar")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER_SEARCH)
			.buildMouseRight()
			.register(registerMethod);

		previousSearch = search.createMapping("key.dainty.previousSearch")
			.setContext(JeiKeyConflictContext.GUI)
			.buildKeyboardKey(GLFW.GLFW_KEY_UP)
			.register(registerMethod);

		nextSearch = search.createMapping("key.dainty.nextSearch")
			.setContext(JeiKeyConflictContext.GUI)
			.buildKeyboardKey(GLFW.GLFW_KEY_DOWN)
			.register(registerMethod);

		// Cheat Mode
		toggleCheatMode = cheat.createMapping("key.dainty.toggleCheatMode")
			.setContext(JeiKeyConflictContext.GUI)
			.buildUnbound()
			.register(registerMethod);

		cheatOneItem1 = cheat.createMapping("key.dainty.cheatOneItem")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER_CHEAT_MODE)
			.buildMouseLeft()
			.register(registerMethod);

		cheatOneItem2 = cheat.createMapping("key.dainty.cheatOneItem2")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER_CHEAT_MODE)
			.buildMouseRight()
			.register(registerMethod);

		cheatItemStack1 = cheat.createMapping("key.dainty.cheatItemStack")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER_CHEAT_MODE)
			.setModifier(JeiKeyModifier.SHIFT)
			.buildMouseLeft()
			.register(registerMethod);

		cheatItemStack2 = cheat.createMapping("key.dainty.cheatItemStack2")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER_CHEAT_MODE)
			.buildMouseMiddle()
			.register(registerMethod);

		// Hovering over config button
		toggleCheatModeConfigButton = hoverConfig.createMapping("key.dainty.toggleCheatModeConfigButton")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER_CONFIG_BUTTON)
			.setModifier(JeiKeyModifier.CONTROL_OR_COMMAND)
			.buildMouseLeft()
			.register(registerMethod);

		// Edit Mode
		toggleEditMode = editMode.createMapping("key.dainty.toggleEditMode")
			.setContext(JeiKeyConflictContext.GUI)
			.buildUnbound()
			.register(registerMethod);

		toggleHideIngredient = editMode.createMapping("key.dainty.toggleHideIngredient")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER)
			.setModifier(JeiKeyModifier.CONTROL_OR_COMMAND)
			.buildMouseLeft()
			.register(registerMethod);

		toggleWildcardHideIngredient = editMode.createMapping("key.dainty.toggleWildcardHideIngredient")
			.setContext(JeiKeyConflictContext.JEI_GUI_HOVER)
			.setModifier(JeiKeyModifier.CONTROL_OR_COMMAND)
			.buildMouseRight()
			.register(registerMethod);

		// Recipes
		recipeBack = recipeCategory.createMapping("key.dainty.recipeBack")
			.setContext(JeiKeyConflictContext.GUI)
			.buildKeyboardKey(GLFW.GLFW_KEY_BACKSPACE)
			.register(registerMethod);

		previousRecipePage = recipeCategory.createMapping("key.dainty.previousRecipePage")
			.setContext(JeiKeyConflictContext.GUI)
			.buildKeyboardKey(GLFW.GLFW_KEY_PAGE_UP)
			.register(registerMethod);

		nextRecipePage = recipeCategory.createMapping("key.dainty.nextRecipePage")
			.setContext(JeiKeyConflictContext.GUI)
			.buildKeyboardKey(GLFW.GLFW_KEY_PAGE_DOWN)
			.register(registerMethod);

		previousCategory = recipeCategory.createMapping("key.dainty.previousCategory")
			.setContext(JeiKeyConflictContext.GUI)
			.setModifier(JeiKeyModifier.SHIFT)
			.buildKeyboardKey(GLFW.GLFW_KEY_PAGE_UP)
			.register(registerMethod);

		nextCategory = recipeCategory.createMapping("key.dainty.nextCategory")
			.setContext(JeiKeyConflictContext.GUI)
			.setModifier(JeiKeyModifier.SHIFT)
			.buildKeyboardKey(GLFW.GLFW_KEY_PAGE_DOWN)
			.register(registerMethod);

		closeRecipeGui = recipeCategory.createMapping("key.dainty.closeRecipeGui")
			.setContext(JeiKeyConflictContext.GUI)
			.buildKeyboardKey(GLFW.GLFW_KEY_ESCAPE)
			.register(registerMethod);

		// Dev Tools
		copyRecipeId = devTools.createMapping("key.dainty.copy.recipe.id")
			.setContext(JeiKeyConflictContext.GUI)
			.buildUnbound()
			.register(registerMethod);

		showRecipe = new JeiMultiKeyMapping(showRecipe1, showRecipe2);
		showUses = new JeiMultiKeyMapping(showUses1, showUses2);
		cheatOneItem = new JeiMultiKeyMapping(cheatOneItem1, cheatOneItem2);
		cheatItemStack = new JeiMultiKeyMapping(cheatItemStack1, cheatItemStack2);

		String jeiHiddenInternalCategoryName = "dainty.key.category.hidden.internal";
		IJeiKeyMappingCategoryBuilder jeiHidden = inputHelper.createKeyMappingCategoryBuilder(jeiHiddenInternalCategoryName);

		escapeKey = jeiHidden.createMapping("key.dainty.internal.escape.key")
			.setContext(JeiKeyConflictContext.GUI)
			.buildKeyboardKey(GLFW.GLFW_KEY_ESCAPE);

		leftClick = jeiHidden.createMapping("key.dainty.internal.left.click")
			.setContext(JeiKeyConflictContext.GUI)
			.buildMouseLeft();

		rightClick = jeiHidden.createMapping("key.dainty.internal.right.click")
			.setContext(JeiKeyConflictContext.GUI)
			.buildMouseRight();

		enterKey = new JeiMultiKeyMapping(
			jeiHidden.createMapping("key.dainty.internal.enter.key")
				.setContext(JeiKeyConflictContext.GUI)
				.buildKeyboardKey(GLFW.GLFW_KEY_ENTER),

			jeiHidden.createMapping("key.dainty.internal.enter.key2")
				.setContext(JeiKeyConflictContext.GUI)
				.buildKeyboardKey(GLFW.GLFW_KEY_KP_ENTER)
		);
	}

	@Override
	public IJeiKeyMapping getToggleOverlay() {
		return toggleOverlay;
	}

	@Override
	public IJeiKeyMapping getFocusSearch() {
		return focusSearch;
	}

	@Override
	public IJeiKeyMapping getToggleCheatMode() {
		return toggleCheatMode;
	}

	@Override
	public IJeiKeyMapping getToggleEditMode() {
		return toggleEditMode;
	}

	@Override
	public IJeiKeyMapping getToggleCheatModeConfigButton() {
		return toggleCheatModeConfigButton;
	}

	@Override
	public IJeiKeyMapping getRecipeBack() {
		return recipeBack;
	}

	@Override
	public IJeiKeyMapping getPreviousCategory() {
		return previousCategory;
	}

	@Override
	public IJeiKeyMapping getNextCategory() {
		return nextCategory;
	}

	@Override
	public IJeiKeyMapping getPreviousRecipePage() {
		return previousRecipePage;
	}

	@Override
	public IJeiKeyMapping getNextRecipePage() {
		return nextRecipePage;
	}

	@Override
	public IJeiKeyMapping getPreviousPage() {
		return previousPage;
	}

	@Override
	public IJeiKeyMapping getNextPage() {
		return nextPage;
	}

	@Override
	public IJeiKeyMapping getCloseRecipeGui() {
		return closeRecipeGui;
	}

	@Override
	public IJeiKeyMapping getBookmark() {
		return bookmark;
	}

	@Override
	public IJeiKeyMapping getToggleBookmarkOverlay() {
		return toggleBookmarkOverlay;
	}

	@Override
	public IJeiKeyMapping getShowRecipe() {
		return showRecipe;
	}

	@Override
	public IJeiKeyMapping getShowUses() {
		return showUses;
	}

	@Override
	public IJeiKeyMapping getTransferRecipeBookmark() {
		return transferRecipeBookmark;
	}

	@Override
	public IJeiKeyMapping getMaxTransferRecipeBookmark() {
		return maxTransferRecipeBookmark;
	}

	@Override
	public IJeiKeyMapping getCheatOneItem() {
		return cheatOneItem;
	}

	@Override
	public IJeiKeyMapping getCheatItemStack() {
		return cheatItemStack;
	}

	@Override
	public IJeiKeyMapping getToggleHideIngredient() {
		return toggleHideIngredient;
	}

	@Override
	public IJeiKeyMapping getToggleWildcardHideIngredient() {
		return toggleWildcardHideIngredient;
	}

	@Override
	public IJeiKeyMapping getHoveredClearSearchBar() {
		return hoveredClearSearchBar;
	}

	@Override
	public IJeiKeyMapping getPreviousSearch() {
		return previousSearch;
	}

	@Override
	public IJeiKeyMapping getNextSearch() {
		return nextSearch;
	}

	@Override
	public IJeiKeyMapping getCopyRecipeId() {
		return copyRecipeId;
	}

	@Override
	public IJeiKeyMapping getEscapeKey() {
		return escapeKey;
	}

	@Override
	public IJeiKeyMapping getLeftClick() {
		return leftClick;
	}

	@Override
	public IJeiKeyMapping getRightClick() {
		return rightClick;
	}

	@Override
	public IJeiKeyMapping getEnterKey() {
		return enterKey;
	}
}
