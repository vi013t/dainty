package violet.dainty.features.recipeviewer.core.gui.overlay;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.elements.OffsetDrawable;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableRect2i;
import violet.dainty.features.recipeviewer.core.common.util.SafeIngredientUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.rendering.BatchRenderElement;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IEditModeConfig;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.core.collect.ListMultiMap;
import violet.dainty.features.recipeviewer.core.gui.overlay.elements.IElement;

public class IngredientListRenderer {
	private static final int BLACKLIST_COLOR = 0xDDFF0000;
	private static final int WILDCARD_BLACKLIST_COLOR = 0xDDFFA500;

	private final List<IngredientListSlot> slots = new ArrayList<>();
	private final ListMultiMap<IIngredientType<?>, BatchRenderElement<?>> renderElementsByType = new ListMultiMap<>();
	private final List<IDrawable> renderOverlays = new ArrayList<>();
	private final IIngredientManager ingredientManager;
	private final boolean searchable;

	private int blocked = 0;

	public IngredientListRenderer(IIngredientManager ingredientManager, boolean searchable) {
		this.ingredientManager = ingredientManager;
		this.searchable = searchable;
	}

	public void clear() {
		slots.clear();
		renderElementsByType.clear();
		renderOverlays.clear();
		blocked = 0;
	}

	public int size() {
		return slots.size() - blocked;
	}

	public void add(IngredientListSlot ingredientListSlot) {
		slots.add(ingredientListSlot);
		addRenderElement(ingredientListSlot);
	}

	private void addRenderElement(IngredientListSlot ingredientListSlot) {
		ingredientListSlot.getOptionalElement()
			.ifPresent(element -> {
				ITypedIngredient<?> typedIngredient = element.getTypedIngredient();
				IIngredientType<?> ingredientType = typedIngredient.getType();
				ImmutableRect2i renderArea = ingredientListSlot.getRenderArea();
				BatchRenderElement<?> batchRenderElement = new BatchRenderElement<>(typedIngredient.getIngredient(), renderArea.x(), renderArea.y());
				renderElementsByType.put(ingredientType, batchRenderElement);
				IDrawable renderOverlay = element.createRenderOverlay();
				if (renderOverlay != null) {
					renderOverlays.add(OffsetDrawable.create(renderOverlay, renderArea.x(), renderArea.y()));
				}
			});
	}

	public Stream<IngredientListSlot> getSlots() {
		return slots.stream()
			.filter(s -> !s.isBlocked());
	}

	public void set(final int startIndex, List<IElement<?>> ingredientList) {
		blocked = 0;
		renderElementsByType.clear();
		renderOverlays.clear();

		ListIterator<IElement<?>> elementIterator = ingredientList.listIterator(startIndex);

		for (IngredientListSlot ingredientListSlot : slots) {
			if (ingredientListSlot.isBlocked()) {
				ingredientListSlot.clear();
				blocked++;
			} else if (elementIterator.hasNext()) {
				IElement<?> element = elementIterator.next();
				while (!element.isVisible() && elementIterator.hasNext()) {
					element = elementIterator.next();
				}
				if (element.isVisible()) {
					ingredientListSlot.setElement(element);
					addRenderElement(ingredientListSlot);
				} else {
					ingredientListSlot.clear();
				}
			} else {
				ingredientListSlot.clear();
			}
		}
	}

	public void render(GuiGraphics guiGraphics) {
		if (searchable && Internal.getClientToggleState().isEditModeEnabled()) {
			renderEditMode(guiGraphics);
		}

		for (Map.Entry<IIngredientType<?>, List<BatchRenderElement<?>>> entry : renderElementsByType.entrySet()) {
			renderBatch(guiGraphics, entry);
		}

		for (IDrawable overlay : renderOverlays) {
			overlay.draw(guiGraphics);
		}
	}

	private <T> void renderBatch(GuiGraphics guiGraphics, Map.Entry<IIngredientType<?>, List<BatchRenderElement<?>>> entry) {
		@SuppressWarnings("unchecked")
		IIngredientType<T> type = (IIngredientType<T>) entry.getKey();
		IIngredientRenderer<T> ingredientRenderer = ingredientManager.getIngredientRenderer(type);
		@SuppressWarnings("unchecked")
		List<BatchRenderElement<T>> elements = (List<BatchRenderElement<T>>) (Object) entry.getValue();
		SafeIngredientUtil.renderBatch(guiGraphics, type, ingredientRenderer, elements);
	}

	private void renderEditMode(GuiGraphics guiGraphics) {
		IEditModeConfig editModeConfig = Internal.getJeiRuntime().getEditModeConfig();

		for (IngredientListSlot slot : slots) {
			slot.getOptionalElement()
				.ifPresent(element -> {
					renderEditMode(guiGraphics, slot.getArea(), slot.getPadding(), element.getTypedIngredient(), editModeConfig);
				});
		}

		RenderSystem.enableBlend();
	}

	private static <T> void renderEditMode(GuiGraphics guiGraphics, ImmutableRect2i area, int padding, ITypedIngredient<T> typedIngredient, IEditModeConfig config) {
		Set<IEditModeConfig.HideMode> hideModes = config.getIngredientHiddenUsingConfigFile(typedIngredient);
		if (!hideModes.isEmpty()) {
			boolean wildcard = hideModes.contains(IEditModeConfig.HideMode.WILDCARD);
			boolean single = hideModes.contains(IEditModeConfig.HideMode.SINGLE);
			if (wildcard && single) {
				guiGraphics.fill(
					RenderType.guiOverlay(),
					area.getX() + padding,
					area.getY() + padding,
					area.getX() + 16 + padding,
					area.getY() + 8 + padding,
					WILDCARD_BLACKLIST_COLOR
				);
				guiGraphics.fill(
					RenderType.guiOverlay(),
					area.getX() + padding,
					area.getY() + 8 + padding,
					area.getX() + 16 + padding,
					area.getY() + 16 + padding,
					BLACKLIST_COLOR
				);
			} else if (wildcard) {
				guiGraphics.fill(
					RenderType.guiOverlay(),
					area.getX() + padding,
					area.getY() + padding,
					area.getX() + 16 + padding,
					area.getY() + 16 + padding,
					WILDCARD_BLACKLIST_COLOR
				);
			} else if (single) {
				guiGraphics.fill(
					RenderType.guiOverlay(),
					area.getX() + padding,
					area.getY() + padding,
					area.getX() + 16 + padding,
					area.getY() + 16 + padding,
					BLACKLIST_COLOR
				);
			}
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		}
	}
}
