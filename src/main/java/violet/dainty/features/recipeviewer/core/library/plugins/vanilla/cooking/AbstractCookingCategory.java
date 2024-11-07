package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking;

import com.mojang.serialization.Codec;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.HorizontalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.gui.placement.VerticalAlignment;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.ICodecHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.AbstractRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.util.RecipeUtil;

public abstract class AbstractCookingCategory<T extends AbstractCookingRecipe> extends AbstractRecipeCategory<RecipeHolder<T>> {
	protected final int regularCookTime;

	public AbstractCookingCategory(IGuiHelper guiHelper, RecipeType<RecipeHolder<T>> recipeType, Block icon, String translationKey, int regularCookTime) {
		this(guiHelper, recipeType, icon, translationKey, regularCookTime, 82, 54);
	}

	public AbstractCookingCategory(IGuiHelper guiHelper, RecipeType<RecipeHolder<T>> recipeType, Block icon, String translationKey, int regularCookTime, int width, int height) {
		super(
			recipeType,
			Component.translatable(translationKey),
			guiHelper.createDrawableItemLike(icon),
			width,
			height
		);
		this.regularCookTime = regularCookTime;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<T> recipeHolder, IFocusGroup focuses) {
		T recipe = recipeHolder.value();

		builder.addInputSlot(1, 1)
			.setStandardSlotBackground()
			.addIngredients(recipe.getIngredients().getFirst());

		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 37)
			.setStandardSlotBackground();

		builder.addOutputSlot(61, 19)
			.setOutputSlotBackground()
			.addItemStack(RecipeUtil.getResultItem(recipe));
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<T> recipeHolder, IFocusGroup focuses) {
		T recipe = recipeHolder.value();
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = regularCookTime;
		}
		builder.addAnimatedRecipeArrow(cookTime)
			.setPosition(26, 17);
		builder.addAnimatedRecipeFlame(300)
			.setPosition(1, 20);

		addExperience(builder, recipeHolder);
		addCookTime(builder, recipeHolder);
	}

	protected void addExperience(IRecipeExtrasBuilder builder, RecipeHolder<T> recipeHolder) {
		T recipe = recipeHolder.value();
		float experience = recipe.getExperience();
		if (experience > 0) {
			Component experienceString = Component.translatable("gui.dainty.category.smelting.experience", experience);
			builder.addText(experienceString, getWidth() - 20, 10)
				.setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.TOP)
				.setTextAlignment(HorizontalAlignment.RIGHT)
				.setColor(0xFF808080);
		}
	}

	protected void addCookTime(IRecipeExtrasBuilder builder, RecipeHolder<T> recipeHolder) {
		T recipe = recipeHolder.value();
		int cookTime = recipe.getCookingTime();
		if (cookTime <= 0) {
			cookTime = regularCookTime;
		}
		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			Component timeString = Component.translatable("gui.dainty.category.smelting.time.seconds", cookTimeSeconds);
			builder.addText(timeString, getWidth() - 20, 10)
				.setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
				.setTextAlignment(HorizontalAlignment.RIGHT)
				.setTextAlignment(VerticalAlignment.BOTTOM)
				.setColor(0xFF808080);
		}
	}

	@Override
	public boolean isHandled(RecipeHolder<T> recipeHolder) {
		T recipe = recipeHolder.value();
		return !recipe.isSpecial();
	}

	@Override
	public ResourceLocation getRegistryName(RecipeHolder<T> recipe) {
		return recipe.id();
	}

	@Override
	public Codec<RecipeHolder<T>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
		return codecHelper.getRecipeHolderCodec();
	}
}
