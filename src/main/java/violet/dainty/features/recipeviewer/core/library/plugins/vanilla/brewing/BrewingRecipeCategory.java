package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.brewing;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ITickTimer;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableAnimated;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableStatic;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.AbstractRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiBrewingRecipe;

public class BrewingRecipeCategory extends AbstractRecipeCategory<IJeiBrewingRecipe> {
	private final IDrawable background;
	private final IDrawableAnimated arrow;
	private final IDrawableAnimated bubbles;
	private final IDrawableStatic blazeHeat;

	public BrewingRecipeCategory(IGuiHelper guiHelper) {
		super(
			RecipeTypes.BREWING,
			Component.translatable("gui.dainty.category.brewing"),
			guiHelper.createDrawableItemLike(Blocks.BREWING_STAND),
			114,
			61
		);
		Textures textures = Internal.getTextures();
		background = textures.getBrewingStandBackground();

		arrow = guiHelper.createAnimatedDrawable(textures.getBrewingStandArrow(),400, IDrawableAnimated.StartDirection.TOP, false);

		ITickTimer bubblesTickTimer = new BrewingBubblesTickTimer(guiHelper);
		bubbles = guiHelper.createAnimatedDrawable(textures.getBrewingStandBubbles(), bubblesTickTimer, IDrawableAnimated.StartDirection.BOTTOM);

		blazeHeat = textures.getBrewingStandBlazeHeat();
	}

	@Override
	public void draw(IJeiBrewingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		background.draw(guiGraphics, 0, 1);
		blazeHeat.draw(guiGraphics, 5, 30);
		bubbles.draw(guiGraphics, 9, 1);
		arrow.draw(guiGraphics, 43, 3);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, IJeiBrewingRecipe recipe, IFocusGroup focuses) {
		int brewingSteps = recipe.getBrewingSteps();
		String brewingStepsString = brewingSteps < Integer.MAX_VALUE ? Integer.toString(brewingSteps) : "?";
		Component steps = Component.translatable("gui.dainty.category.brewing.steps", brewingStepsString);

		builder.addText(steps, 42, 12)
			.setPosition(70, 28)
			.setColor(0xFF808080);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IJeiBrewingRecipe recipe, IFocusGroup focuses) {
		List<ItemStack> potionInputs = recipe.getPotionInputs();

		builder.addInputSlot(1, 37)
			.addItemStacks(potionInputs);

		builder.addInputSlot(24, 44)
			.addItemStacks(potionInputs);

		builder.addInputSlot(47, 37)
			.addItemStacks(potionInputs);

		builder.addInputSlot(24, 3)
			.addItemStacks(recipe.getIngredients());

		builder.addOutputSlot(81, 3)
			.addItemStack(recipe.getPotionOutput())
			.setStandardSlotBackground();
	}

	@Override
	public @Nullable ResourceLocation getRegistryName(IJeiBrewingRecipe recipe) {
		return recipe.getUid();
	}

	private static class BrewingBubblesTickTimer implements ITickTimer {
		/**
		 * Similar to {@link BrewingStandScreen#BUBBLELENGTHS}
		 */
		@SuppressWarnings("JavadocReference")
		private static final int[] BUBBLE_LENGTHS = new int[]{29, 23, 18, 13, 9, 5, 0};
		private final ITickTimer internalTimer;

		public BrewingBubblesTickTimer(IGuiHelper guiHelper) {
			this.internalTimer = guiHelper.createTickTimer(14, BUBBLE_LENGTHS.length - 1, false);
		}

		@Override
		public int getValue() {
			int timerValue = this.internalTimer.getValue();
			return BUBBLE_LENGTHS[timerValue];
		}

		@Override
		public int getMaxValue() {
			return BUBBLE_LENGTHS[0];
		}
	}
}
