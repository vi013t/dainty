package violet.dainty.features.recipeviewer.core.library.gui.widgets;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import violet.dainty.features.recipeviewer.core.common.util.ImmutablePoint2i;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableSize2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;

import java.util.List;

@SuppressWarnings("removal")
public class ScrollGridWidgetFactory<R> implements violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IScrollGridWidgetFactory<R> {
	private final int columns;
	private final int visibleRows;
	private ImmutablePoint2i position;

	public ScrollGridWidgetFactory(int columns, int visibleRows) {
		this.columns = columns;
		this.visibleRows = visibleRows;
		this.position = ImmutablePoint2i.ORIGIN;
	}

	@Override
	public void setPosition(int x, int y) {
		position = new ImmutablePoint2i(x, y);
	}

	@Override
	public ScreenRectangle getArea() {
		ImmutableSize2i size = ScrollGridRecipeWidget.calculateSize(columns, visibleRows);
		return new ScreenRectangle(position.x(), position.y(), size.width(), size.height());
	}

	@Override
	public void createWidgetForSlots(IRecipeExtrasBuilder builder, R recipe, List<IRecipeSlotDrawable> slots) {
		ScrollGridRecipeWidget widget = ScrollGridRecipeWidget.create(slots, columns, visibleRows);
		widget.setPosition(position.x(), position.y());
		builder.addSlottedWidget(widget, slots);
		builder.addInputHandler(widget);
	}
}
