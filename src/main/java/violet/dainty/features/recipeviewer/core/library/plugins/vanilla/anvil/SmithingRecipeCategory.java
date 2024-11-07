package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.anvil;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.block.Blocks;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.ingredient.IRecipeSlotDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.ICodecHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IRecipeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.AbstractRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.vanilla.smithing.IExtendableSmithingRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmithingRecipeCategory extends AbstractRecipeCategory<RecipeHolder<SmithingRecipe>> implements IExtendableSmithingRecipeCategory {
	private final Map<Class<? extends SmithingRecipe>, ISmithingCategoryExtension<?>> extensions = new HashMap<>();

	public SmithingRecipeCategory(IGuiHelper guiHelper) {
		super(
			RecipeTypes.SMITHING,
			Blocks.SMITHING_TABLE.getName(),
			guiHelper.createDrawableItemLike(Blocks.SMITHING_TABLE),
			108,
			28
		);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<SmithingRecipe> recipeHolder, IFocusGroup focuses) {
		SmithingRecipe recipe = recipeHolder.value();

		ISmithingCategoryExtension<? super SmithingRecipe> extension = getExtension(recipe);
		if (extension == null) {
			return;
		}

		IRecipeSlotBuilder templateSlot = builder.addInputSlot(1, 6)
			.setStandardSlotBackground();

		IRecipeSlotBuilder baseSlot = builder.addInputSlot(19, 6)
			.setStandardSlotBackground();

		IRecipeSlotBuilder additionSlot = builder.addInputSlot(37, 6)
			.setStandardSlotBackground();

		IRecipeSlotBuilder outputSlot = builder.addOutputSlot(91, 6)
			.setStandardSlotBackground();

		extension.setTemplate(recipe, templateSlot);
		extension.setBase(recipe, baseSlot);
		extension.setAddition(recipe, additionSlot);
		extension.setOutput(recipe, outputSlot);
	}

	@Override
	public void onDisplayedIngredientsUpdate(RecipeHolder<SmithingRecipe> recipeHolder, List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses) {
		SmithingRecipe recipe = recipeHolder.value();
		ISmithingCategoryExtension<? super SmithingRecipe> extension = getExtension(recipe);
		if (extension == null) {
			return;
		}

		IRecipeSlotDrawable templateSlot = recipeSlots.getFirst();
		IRecipeSlotDrawable baseSlot = recipeSlots.get(1);
		IRecipeSlotDrawable additionSlot = recipeSlots.get(2);
		IRecipeSlotDrawable outputSlot = recipeSlots.get(3);
		extension.onDisplayedIngredientsUpdate(
			recipe,
			templateSlot,
			baseSlot,
			additionSlot,
			outputSlot,
			focuses
		);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<SmithingRecipe> recipe, IFocusGroup focuses) {
		builder.addRecipeArrow().setPosition(61, 6);
	}

	@Override
	public boolean isHandled(RecipeHolder<SmithingRecipe> recipeHolder) {
		SmithingRecipe recipe = recipeHolder.value();
		var extension = getExtension(recipe);
		return extension != null;
	}

	@Override
	public ResourceLocation getRegistryName(RecipeHolder<SmithingRecipe> recipe) {
		return recipe.id();
	}

	@Override
	public Codec<RecipeHolder<SmithingRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
		return codecHelper.getRecipeHolderCodec();
	}

	@Override
	public <R extends SmithingRecipe> void addExtension(Class<? extends R> recipeClass, ISmithingCategoryExtension<R> extension) {
		ErrorUtil.checkNotNull(recipeClass, "recipeClass");
		ErrorUtil.checkNotNull(extension, "extension");
		if (extensions.containsKey(recipeClass)) {
			throw new IllegalArgumentException("An extension has already been registered for: " + recipeClass);
		}
		extensions.put(recipeClass, extension);
	}

	@Nullable
	private <R extends SmithingRecipe> ISmithingCategoryExtension<? super R> getExtension(SmithingRecipe recipe) {
		{
			ISmithingCategoryExtension<?> extension = extensions.get(recipe.getClass());
			if (extension != null) {
				//noinspection unchecked
				return (ISmithingCategoryExtension<? super R>) extension;
			}
		}
		for (Map.Entry<Class<? extends SmithingRecipe>, ISmithingCategoryExtension<?>> e : extensions.entrySet()) {
			if (e.getKey().isInstance(recipe)) {
				//noinspection unchecked
				return (ISmithingCategoryExtension<? super R>) e.getValue();
			}
		}
		return null;
	}
}