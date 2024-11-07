package violet.dainty.features.recipeviewer.core.library.plugins.jei.info;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IIngredientAcceptor;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.widgets.IRecipeExtrasBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.AbstractRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiIngredientInfoRecipe;

public class IngredientInfoRecipeCategory extends AbstractRecipeCategory<IJeiIngredientInfoRecipe> {
	private static final int recipeWidth = 170;
	private static final int recipeHeight = 125;

	public IngredientInfoRecipeCategory(Textures textures) {
		super(
			RecipeTypes.INFORMATION,
			Component.translatable("gui.dainty.category.itemInformation"),
			textures.getInfoIcon(),
			recipeWidth,
			recipeHeight
		);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IJeiIngredientInfoRecipe recipe, IFocusGroup focuses) {
		int xPos = (recipeWidth - 16) / 2;

		IRecipeSlotBuilder inputSlotBuilder = builder.addInputSlot(xPos, 1)
			.setStandardSlotBackground();

		IIngredientAcceptor<?> outputSlotBuilder = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);

		for (ITypedIngredient<?> typedIngredient : recipe.getIngredients()) {
			inputSlotBuilder.addTypedIngredient(typedIngredient);
			outputSlotBuilder.addTypedIngredient(typedIngredient);
		}
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, IJeiIngredientInfoRecipe recipe, IFocusGroup focuses) {
		int yPos = 22;
		int height = recipeHeight - yPos;
		builder.addScrollBoxWidget(
				recipeWidth,
				height,
				0,
				yPos
			)
			.setContents(recipe.getDescription());
	}

	@Override
	public @Nullable ResourceLocation getRegistryName(IJeiIngredientInfoRecipe recipe) {
		return null;
	}

}
