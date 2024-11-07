package violet.dainty.features.recipeviewer.core.library.plugins.debug;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeLayoutBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.IRecipeSlotBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.gui.drawable.IDrawable;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IPlatformFluidHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusGroup;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeIngredientRole;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;

import java.util.List;

public class DebugFocusRecipeCategory<F> implements IRecipeCategory<DebugRecipe> {
	public static final RecipeType<DebugRecipe> TYPE = RecipeType.create(ModIds.JEI_ID, "debug_focus", DebugRecipe.class);
	public static final int RECIPE_WIDTH = 160;
	public static final int RECIPE_HEIGHT = 60;
	private final IPlatformFluidHelper<F> platformFluidHelper;
	private final Component localizedName;

	public DebugFocusRecipeCategory(IPlatformFluidHelper<F> platformFluidHelper) {
		this.platformFluidHelper = platformFluidHelper;
		this.localizedName = Component.literal("debug_focus");
	}

	@Override
	public RecipeType<DebugRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public int getWidth() {
		return RECIPE_WIDTH;
	}

	@Override
	public int getHeight() {
		return RECIPE_HEIGHT;
	}

	@Override
	public IDrawable getIcon() {
		return null;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DebugRecipe recipe, IFocusGroup focuses) {
		IRecipeSlotBuilder inputSlot = builder.addInputSlot(0, 0)
			.addItemStacks(List.of(
				new ItemStack(Items.BUCKET),
				new ItemStack(Items.WATER_BUCKET),
				new ItemStack(Items.LAVA_BUCKET),
				new ItemStack(Items.POWDER_SNOW_BUCKET),
				new ItemStack(Items.AXOLOTL_BUCKET),
				new ItemStack(Items.SALMON_BUCKET),
				new ItemStack(Items.COD_BUCKET),
				new ItemStack(Items.PUFFERFISH_BUCKET),
				new ItemStack(Items.TROPICAL_FISH_BUCKET)
			));

		long bucketVolume = platformFluidHelper.bucketVolume();
		IRecipeSlotBuilder outputSlot = builder.addOutputSlot(20, 0)
			.addItemStack(ItemStack.EMPTY)
			.addIngredients(platformFluidHelper.getFluidIngredientType(), List.of(
				platformFluidHelper.create(Fluids.WATER.defaultFluidState().holder(), bucketVolume),
				platformFluidHelper.create(Fluids.LAVA.defaultFluidState().holder(), bucketVolume)
			))
			.addItemStacks(List.of(
				new ItemStack(Items.SNOW_BLOCK),
				new ItemStack(Items.AXOLOTL_SPAWN_EGG),
				new ItemStack(Items.SALMON),
				new ItemStack(Items.COD),
				new ItemStack(Items.PUFFERFISH),
				new ItemStack(Items.TROPICAL_FISH)
			));

		builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
			.addItemStacks(List.of(
				new ItemStack(Items.ACACIA_BOAT),
				new ItemStack(Items.ACACIA_BUTTON),
				new ItemStack(Items.ACACIA_DOOR),
				new ItemStack(Items.ACACIA_LOG),
				new ItemStack(Items.ACACIA_PLANKS),
				new ItemStack(Items.ACACIA_FENCE),
				new ItemStack(Items.ACACIA_FENCE_GATE),
				new ItemStack(Items.ACACIA_LEAVES),
				new ItemStack(Items.ACACIA_PRESSURE_PLATE)
			));

		builder.createFocusLink(inputSlot, outputSlot);
	}

	@Override
	public ResourceLocation getRegistryName(DebugRecipe recipe) {
		return recipe.getRegistryName();
	}
}
