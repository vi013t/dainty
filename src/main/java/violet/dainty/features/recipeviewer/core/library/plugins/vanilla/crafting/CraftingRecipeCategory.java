package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting;

import java.util.List;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.common.util.ImmutableSize2i;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawableStatic;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.ICraftingGridHelper;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotsView;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.ICodecHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.AbstractRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.vanilla.crafting.IExtendableCraftingRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.recipes.CraftingExtensionHelper;

public class CraftingRecipeCategory extends AbstractRecipeCategory<RecipeHolder<CraftingRecipe>> implements IExtendableCraftingRecipeCategory {
	public static final int width = 116;
	public static final int height = 54;

	private final IGuiHelper guiHelper;
	private final ICraftingGridHelper craftingGridHelper;
	private final CraftingExtensionHelper extendableHelper = new CraftingExtensionHelper();

	public CraftingRecipeCategory(IGuiHelper guiHelper) {
		super(
			RecipeTypes.CRAFTING,
			Component.translatable("gui.dainty.category.craftingTable"),
			guiHelper.createDrawableItemLike(Blocks.CRAFTING_TABLE),
			width,
			height
		);
		this.guiHelper = guiHelper;
		craftingGridHelper = guiHelper.createCraftingGridHelper();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CraftingRecipe> recipeHolder, IFocusGroup focuses) {
		var recipeExtension = this.extendableHelper.getRecipeExtension(recipeHolder);
		recipeExtension.setRecipe(recipeHolder, builder, craftingGridHelper, focuses);
	}

	@Override
	public void onDisplayedIngredientsUpdate(RecipeHolder<CraftingRecipe> recipeHolder, List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses) {
		var recipeExtension = this.extendableHelper.getRecipeExtension(recipeHolder);
		recipeExtension.onDisplayedIngredientsUpdate(recipeHolder, recipeSlots, focuses);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<CraftingRecipe> recipeHolder, IFocusGroup focuses) {
		var recipeExtension = this.extendableHelper.getRecipeExtension(recipeHolder);
		recipeExtension.createRecipeExtras(recipeHolder, builder, craftingGridHelper, focuses);
	}

	@Override
	public void draw(RecipeHolder<CraftingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		var extension = this.extendableHelper.getRecipeExtension(recipeHolder);
		int recipeWidth = this.getWidth();
		int recipeHeight = this.getHeight();
		extension.drawInfo(recipeHolder, recipeWidth, recipeHeight, guiGraphics, mouseX, mouseY);

		IDrawableStatic recipeArrow = guiHelper.getRecipeArrow();
		recipeArrow.draw(guiGraphics, 61, (height - recipeArrow.getHeight()) / 2);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<CraftingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		var extension = this.extendableHelper.getRecipeExtension(recipeHolder);
		extension.getTooltip(tooltip, recipeHolder, mouseX, mouseY);
	}

	@SuppressWarnings({"removal"})
	@Override
	public List<Component> getTooltipStrings(RecipeHolder<CraftingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		var extension = this.extendableHelper.getRecipeExtension(recipeHolder);
		return extension.getTooltipStrings(recipeHolder, mouseX, mouseY);
	}

	@SuppressWarnings("removal")
	@Override
	public boolean handleInput(RecipeHolder<CraftingRecipe> recipeHolder, double mouseX, double mouseY, InputConstants.Key input) {
		var extension = this.extendableHelper.getRecipeExtension(recipeHolder);
		return extension.handleInput(recipeHolder, mouseX, mouseY, input);
	}

	@Override
	public boolean isHandled(RecipeHolder<CraftingRecipe> recipeHolder) {
		return this.extendableHelper.getOptionalRecipeExtension(recipeHolder)
			.isPresent();
	}

	@Override
	public <R extends CraftingRecipe> void addExtension(Class<? extends R> recipeClass, ICraftingCategoryExtension<R> extension) {
		ErrorUtil.checkNotNull(recipeClass, "recipeClass");
		ErrorUtil.checkNotNull(extension, "extension");
		extendableHelper.addRecipeExtension(recipeClass, extension);
	}

	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getRegistryName(RecipeHolder<CraftingRecipe> recipeHolder) {
		ErrorUtil.checkNotNull(recipeHolder, "recipeHolder");
		return this.extendableHelper.getOptionalRecipeExtension(recipeHolder)
			.flatMap(extension -> extension.getRegistryName(recipeHolder))
			.orElseGet(recipeHolder::id);
	}

	@Override
	public Codec<RecipeHolder<CraftingRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
		return codecHelper.getRecipeHolderCodec();
	}

	public ImmutableSize2i getRecipeSize(RecipeHolder<CraftingRecipe> recipeHolder) {
		ErrorUtil.checkNotNull(recipeHolder, "recipeHolder");
		return this.extendableHelper.getOptionalRecipeExtension(recipeHolder)
			.map(extension -> {
				int width = extension.getWidth(recipeHolder);
				int height = extension.getHeight(recipeHolder);
				return new ImmutableSize2i(width, height);
			})
			.orElse(ImmutableSize2i.EMPTY);
	}
}
