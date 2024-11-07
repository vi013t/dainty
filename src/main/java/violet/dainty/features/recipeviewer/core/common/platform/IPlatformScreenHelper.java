package violet.dainty.features.recipeviewer.core.common.platform;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.world.inventory.Slot;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;

public interface IPlatformScreenHelper {
	Optional<Slot> getSlotUnderMouse(AbstractContainerScreen<?> containerScreen);

	int getGuiLeft(AbstractContainerScreen<?> containerScreen);

	int getGuiTop(AbstractContainerScreen<?> containerScreen);

	int getXSize(AbstractContainerScreen<?> containerScreen);

	int getYSize(AbstractContainerScreen<?> containerScreen);

	ImmutableRect2i getBookArea(RecipeUpdateListener containerScreen);

	ImmutableRect2i getToastsArea();

	List<RecipeBookTabButton> getTabButtons(RecipeBookComponent recipeBookComponent);

	void setFocused(AbstractWidget widget, boolean value);
}
