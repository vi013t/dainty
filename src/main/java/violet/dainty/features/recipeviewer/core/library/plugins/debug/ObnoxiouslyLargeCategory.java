package violet.dainty.features.recipeviewer.core.library.plugins.debug;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.AbstractRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;

import java.util.Collection;
import java.util.Iterator;

public class ObnoxiouslyLargeCategory extends AbstractRecipeCategory<ObnoxiouslyLargeRecipe> {
	public static final RecipeType<ObnoxiouslyLargeRecipe> TYPE = RecipeType.create(ModIds.JEI_ID, "obnoxiously_large_recipe", ObnoxiouslyLargeRecipe.class);
	private static final int GRID_WIDTH = 300;
	private static final int GRID_HEIGHT = 300;

	private final IIngredientManager ingredientManager;
	private final IDrawable slotBackground;

	public ObnoxiouslyLargeCategory(IGuiHelper helper, Textures textures, IIngredientManager ingredientManager) {
		super(
			TYPE,
			Component.literal("Obnoxiously Large Recipe Category"),
			textures.getFlameIcon(),
			GRID_WIDTH + (2 * helper.getSlotDrawable().getWidth()),
			GRID_HEIGHT
		);
		this.ingredientManager = ingredientManager;
		this.slotBackground = helper.getSlotDrawable();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ObnoxiouslyLargeRecipe recipe, IFocusGroup focuses) {
		int slotWidth = slotBackground.getWidth();
		int slotHeight = slotBackground.getHeight();
		int xCount = GRID_WIDTH / slotWidth;
		int yCount = GRID_HEIGHT / slotHeight;
		int xOffset = (GRID_WIDTH - (xCount * slotWidth)) / 2;
		int yOffset = (GRID_HEIGHT - (yCount * slotHeight)) / 2;

		Collection<ItemStack> allItems = ingredientManager.getAllIngredients(VanillaTypes.ITEM_STACK);
		Iterator<ItemStack> iterator = allItems.iterator();

		for (int x = 0; x < xCount; x++) {
			for (int y = 0; y < yCount; y++) {
				int xPos = xOffset + (x * slotWidth);
				int yPos = yOffset + (y * slotHeight);
				ItemStack stack = iterator.next();
				builder.addInputSlot(xPos + 1, yPos + 1)
					.setStandardSlotBackground()
					.addItemStack(stack);
			}
		}

		builder.addOutputSlot(GRID_WIDTH + slotWidth, GRID_HEIGHT / 2)
			.setStandardSlotBackground()
			.addItemStack(iterator.next());
	}

	@Override
	public ResourceLocation getRegistryName(ObnoxiouslyLargeRecipe recipe) {
		return recipe.getRecipeId();
	}
}
