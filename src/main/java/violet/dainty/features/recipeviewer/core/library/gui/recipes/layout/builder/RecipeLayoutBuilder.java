package violet.dainty.features.recipeviewer.core.library.gui.recipes.layout.builder;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.util.ImmutablePoint2i;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IIngredientAcceptor;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IScalableDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.IRecipeCategoryDecorator;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.core.collect.ListMultiMap;
import violet.dainty.features.recipeviewer.core.core.util.Pair;
import violet.dainty.features.recipeviewer.core.library.gui.ingredients.CycleTicker;
import violet.dainty.features.recipeviewer.core.library.gui.recipes.OutputSlotTooltipCallback;
import violet.dainty.features.recipeviewer.core.library.gui.recipes.RecipeLayout;
import violet.dainty.features.recipeviewer.core.library.gui.recipes.ShapelessIcon;
import violet.dainty.features.recipeviewer.core.library.ingredients.DisplayIngredientAcceptor;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecipeLayoutBuilder<T> implements IRecipeLayoutBuilder {
	private final List<RecipeSlotBuilder> visibleSlots = new ArrayList<>();
	private final List<List<RecipeSlotBuilder>> focusLinkedSlots = new ArrayList<>();

	private final IIngredientManager ingredientManager;
	private final IRecipeCategory<T> recipeCategory;
	private final T recipe;

	private boolean shapeless = false;
	private int shapelessX = -1;
	private int shapelessY = -1;
	private int recipeTransferX = -1;
	private int recipeTransferY = -1;
	private int nextSlotIndex = 0;

	public RecipeLayoutBuilder(IRecipeCategory<T> recipeCategory, T recipe, IIngredientManager ingredientManager) {
		this.recipeCategory = recipeCategory;
		this.recipe = recipe;
		this.ingredientManager = ingredientManager;
	}

	@Override
	public IRecipeSlotBuilder addSlot(RecipeIngredientRole role) {
		RecipeSlotBuilder slot = new RecipeSlotBuilder(ingredientManager, nextSlotIndex++, role);

		if (role == RecipeIngredientRole.OUTPUT) {
			addOutputSlotTooltipCallback(slot);
		}

		this.visibleSlots.add(slot);
		return slot;
	}

	@SuppressWarnings("removal")
	@Override
	@Deprecated
	public IRecipeSlotBuilder addSlotToWidget(RecipeIngredientRole role, violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<?> widgetFactory) {
		RecipeSlotBuilder slot = new RecipeSlotBuilder(ingredientManager, nextSlotIndex++, role)
			.assignToWidgetFactory(widgetFactory);

		if (role == RecipeIngredientRole.OUTPUT) {
			addOutputSlotTooltipCallback(slot);
		}

		this.visibleSlots.add(slot);
		return slot;
	}

	private void addOutputSlotTooltipCallback(RecipeSlotBuilder slot) {
		ResourceLocation recipeName = recipeCategory.getRegistryName(recipe);
		if (recipeName != null) {
			RecipeType<T> recipeType = recipeCategory.getRecipeType();
			OutputSlotTooltipCallback callback = new OutputSlotTooltipCallback(recipeName, recipeType);
			slot.addRichTooltipCallback(callback);
		}
	}

	@Override
	public IIngredientAcceptor<?> addInvisibleIngredients(RecipeIngredientRole role) {
		return new RecipeSlotBuilder(ingredientManager, nextSlotIndex++, role);
	}

	@Override
	public void moveRecipeTransferButton(int posX, int posY) {
		this.recipeTransferX = posX;
		this.recipeTransferY = posY;
	}

	@Override
	public void setShapeless() {
		this.shapeless = true;
	}

	@Override
	public void setShapeless(int posX, int posY) {
		this.shapeless = true;
		this.shapelessX = posX;
		this.shapelessY = posY;
	}

	@Override
	public void createFocusLink(IIngredientAcceptor<?>... slots) {
		List<RecipeSlotBuilder> builders = new ArrayList<>();
		// The focus-linked slots should have the same number of ingredients.
		// Users can technically add more ingredients to the slots later,
		// but it's probably not worth the effort of enforcing this very strictly.
		int count = -1;
		for (IIngredientAcceptor<?> slot : slots) {
			RecipeSlotBuilder builder = (RecipeSlotBuilder) slot;
			builders.add(builder);

			DisplayIngredientAcceptor displayIngredientAcceptor = builder.getIngredientAcceptor();
			List<@Nullable ITypedIngredient<?>> allIngredients = displayIngredientAcceptor.getAllIngredients();
			int ingredientCount = allIngredients.size();
			if (count == -1) {
				count = ingredientCount;
			} else if (count != ingredientCount) {
				IntSummaryStatistics stats = Arrays.stream(slots)
					.map(RecipeSlotBuilder.class::cast)
					.map(RecipeSlotBuilder::getIngredientAcceptor)
					.map(DisplayIngredientAcceptor::getAllIngredients)
					.mapToInt(Collection::size)
					.summaryStatistics();
				throw new IllegalArgumentException(
					"All slots must have the same number of ingredients in order to create a focus link. " +
						String.format("slot stats: %s", stats)
				);
			}
		}

		this.focusLinkedSlots.add(builders);
	}

	@SuppressWarnings("removal")
	public RecipeLayout<T> buildRecipeLayout(
		IFocusGroup focuses,
		Collection<IRecipeCategoryDecorator<T>> decorators,
		IScalableDrawable recipeBackground,
		int recipeBorderPadding
	) {
		ShapelessIcon shapelessIcon = createShapelessIcon(recipeCategory);
		ImmutablePoint2i recipeTransferButtonPosition = getRecipeTransferButtonPosition(recipeCategory, recipeBorderPadding);

		List<Pair<Integer, IRecipeSlotDrawable>> recipeCategorySlots = new ArrayList<>();
		List<Pair<Integer, IRecipeSlotDrawable>> allSlots = new ArrayList<>();
		ListMultiMap<violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<?>, Pair<Integer, IRecipeSlotDrawable>> widgetSlots = new ListMultiMap<>();

		CycleTicker cycleTicker = CycleTicker.createWithRandomOffset();

		Set<RecipeSlotBuilder> focusLinkedSlots = new HashSet<>();
		for (List<RecipeSlotBuilder> linkedSlots : this.focusLinkedSlots) {
			IntSet focusMatches = new IntArraySet();
			for (RecipeSlotBuilder slot : linkedSlots) {
				focusMatches.addAll(slot.getMatches(focuses));
			}
			for (RecipeSlotBuilder slotBuilder : linkedSlots) {
				if (!visibleSlots.contains(slotBuilder)) {
					continue;
				}
				violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<?> assignedWidget = slotBuilder.getAssignedWidget();
				Pair<Integer, IRecipeSlotDrawable> slotDrawable = slotBuilder.build(focusMatches, cycleTicker);
				if (assignedWidget == null) {
					recipeCategorySlots.add(slotDrawable);
				} else {
					widgetSlots.put(assignedWidget, slotDrawable);
				}
				allSlots.add(slotDrawable);
			}
			focusLinkedSlots.addAll(linkedSlots);
		}

		for (RecipeSlotBuilder slotBuilder : visibleSlots) {
			if (!focusLinkedSlots.contains(slotBuilder)) {
				violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<?> assignedWidget = slotBuilder.getAssignedWidget();
				Pair<Integer, IRecipeSlotDrawable> slotDrawable = slotBuilder.build(focuses, cycleTicker);
				if (assignedWidget == null) {
					recipeCategorySlots.add(slotDrawable);
				} else {
					widgetSlots.put(assignedWidget, slotDrawable);
				}
				allSlots.add(slotDrawable);
			}
		}

		RecipeLayout<T> recipeLayout = new RecipeLayout<>(
			recipeCategory,
			decorators,
			recipe,
			recipeBackground,
			recipeBorderPadding,
			shapelessIcon,
			recipeTransferButtonPosition,
			sortSlots(recipeCategorySlots),
			sortSlots(allSlots),
			cycleTicker,
			focuses
		);

		for (Map.Entry<violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<?>, List<Pair<Integer, IRecipeSlotDrawable>>> e : widgetSlots.entrySet()) {
			@SuppressWarnings("unchecked")
			violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<T> factory = (violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.ISlottedWidgetFactory<T>) e.getKey();
			List<IRecipeSlotDrawable> slots = sortSlots(e.getValue());
			factory.createWidgetForSlots(recipeLayout, recipe, slots);
		}

		return recipeLayout;
	}

	private static List<IRecipeSlotDrawable> sortSlots(List<Pair<Integer, IRecipeSlotDrawable>> indexedSlots) {
		List<Pair<Integer, IRecipeSlotDrawable>> sortedPairs = new ArrayList<>(indexedSlots);
		sortedPairs.sort(Comparator.comparingInt(Pair::first));

		List<IRecipeSlotDrawable> iRecipeSlotDrawables = new ArrayList<>(sortedPairs.size());
		for (Pair<Integer, IRecipeSlotDrawable> indexedSlot : sortedPairs) {
			IRecipeSlotDrawable second = indexedSlot.second();
			iRecipeSlotDrawables.add(second);
		}
		return iRecipeSlotDrawables;
	}

	@Nullable
	private ShapelessIcon createShapelessIcon(IRecipeCategory<?> recipeCategory) {
		if (!shapeless) {
			return null;
		}
		IDrawable icon = Internal.getTextures().getShapelessIcon();
		final int x;
		final int y;
		if (this.shapelessX >= 0 && this.shapelessY >= 0) {
			x = this.shapelessX;
			y = this.shapelessY;
		} else {
			// align to top-right
			x = recipeCategory.getWidth() - icon.getWidth();
			y = 0;
		}
		return new ShapelessIcon(icon, x, y);
	}

	private ImmutablePoint2i getRecipeTransferButtonPosition(IRecipeCategory<?> recipeCategory, int recipeBorderPadding) {
		if (this.recipeTransferX >= 0 && this.recipeTransferY >= 0) {
			return new ImmutablePoint2i(
				this.recipeTransferX,
				this.recipeTransferY
			);
		}
		return new ImmutablePoint2i(
			recipeCategory.getWidth() + recipeBorderPadding + RecipeLayout.RECIPE_BUTTON_SPACING,
			recipeCategory.getHeight() + recipeBorderPadding - RecipeLayout.RECIPE_BUTTON_SIZE
		);
	}
}
