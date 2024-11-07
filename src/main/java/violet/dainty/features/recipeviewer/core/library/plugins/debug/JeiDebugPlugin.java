package violet.dainty.features.recipeviewer.core.library.plugins.debug;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.DebugConfig;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformFluidHelperInternal;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformScreenHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.common.util.MathUtil;
import violet.dainty.features.recipeviewer.core.common.util.RegistryUtil;
import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.JeiPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.gui.handlers.IGuiContainerHandler;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IPlatformFluidHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientTypeWithSubtypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IAdvancedRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IExtraIngredientRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IGuiHandlerRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IIngredientAliasRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IModInfoRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IModIngredientRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeCatalystRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeCategoryRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.ISubtypeRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IClickableIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiRuntime;
import violet.dainty.features.recipeviewer.core.library.plugins.debug.ingredients.DebugIngredient;
import violet.dainty.features.recipeviewer.core.library.plugins.debug.ingredients.DebugIngredientHelper;
import violet.dainty.features.recipeviewer.core.library.plugins.debug.ingredients.DebugIngredientListFactory;
import violet.dainty.features.recipeviewer.core.library.plugins.debug.ingredients.DebugIngredientRenderer;
import violet.dainty.features.recipeviewer.core.library.plugins.debug.ingredients.ErrorIngredient;
import violet.dainty.features.recipeviewer.core.library.plugins.debug.ingredients.ErrorIngredientHelper;
import violet.dainty.features.recipeviewer.core.library.plugins.debug.ingredients.ErrorIngredientListFactory;
import violet.dainty.features.recipeviewer.core.library.plugins.debug.ingredients.ErrorIngredientRenderer;

@JeiPlugin
public class JeiDebugPlugin implements IModPlugin {
	private @Nullable DebugRecipeCategory<?> debugRecipeCategory;

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "debug");
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		if (DebugConfig.isDebugModeEnabled()) {
			DebugIngredientHelper ingredientHelper = new DebugIngredientHelper();
			DebugIngredientRenderer ingredientRenderer = new DebugIngredientRenderer(ingredientHelper);
			registration.register(DebugIngredient.TYPE, Collections.emptyList(), ingredientHelper, ingredientRenderer, DebugIngredient.CODEC);

			if (DebugConfig.isCrashingTestIngredientsEnabled()) {
				ErrorIngredientHelper errorIngredientHelper = new ErrorIngredientHelper();
				ErrorIngredientRenderer errorIngredientRenderer = new ErrorIngredientRenderer(errorIngredientHelper);
				Collection<ErrorIngredient> errorIngredients = ErrorIngredientListFactory.create();
				registration.register(ErrorIngredient.TYPE, errorIngredients, errorIngredientHelper, errorIngredientRenderer, ErrorIngredient.CODEC);
			}
		}
	}

	@Override
	public void registerExtraIngredients(IExtraIngredientRegistration registration) {
		if (DebugConfig.isDebugModeEnabled()) {
			registration.addExtraIngredients(DebugIngredient.TYPE, DebugIngredientListFactory.create(0, 10));
		}
	}

	@Override
	public void registerIngredientAliases(IIngredientAliasRegistration registration) {
		registration.addAlias(
			VanillaTypes.ITEM_STACK,
			new ItemStack(Items.PANDA_SPAWN_EGG),
			"dainty.alias.panda.spawn.egg"
		);

		registration.addAlias(
			VanillaTypes.ITEM_STACK,
			new ItemStack(Items.VILLAGER_SPAWN_EGG),
			"dainty.alias.villager.spawn.egg"
		);

		registration.addAliases(
			VanillaTypes.ITEM_STACK,
			List.of(
				new ItemStack(Items.STRUCTURE_VOID),
				new ItemStack(Items.BARRIER)
			),
			"nothing"
		);

		registration.addAliases(
			VanillaTypes.ITEM_STACK,
			List.of(
				new ItemStack(Items.GOLDEN_HOE),
				new ItemStack(Items.DIAMOND_BLOCK)
			),
			List.of("shiny", "valuable", "Expensive", "expansive", "extensive")
		);

		IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
		registerFluidAliases(registration, fluidHelper);
	}

	private <T> void registerFluidAliases(IIngredientAliasRegistration registration, IPlatformFluidHelper<T> fluidHelper) {
		@SuppressWarnings("deprecation")
		Holder.Reference<Fluid> water = Fluids.WATER.builtInRegistryHolder();
		registration.addAliases(
			fluidHelper.getFluidIngredientType(),
			fluidHelper.create(water, fluidHelper.bucketVolume()),
			List.of("wet", "aqua", "sea", "ocean")
		);
	}

	@Override
	public void registerModInfo(IModInfoRegistration registration) {
		registration.addModAliases(ModIds.JEI_ID, "jei");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		if (DebugConfig.isDebugModeEnabled()) {
			IJeiHelpers jeiHelpers = registration.getJeiHelpers();
			IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
			IPlatformFluidHelper<?> platformFluidHelper = jeiHelpers.getPlatformFluidHelper();
			IIngredientManager ingredientManager = jeiHelpers.getIngredientManager();
			Textures textures = Internal.getTextures();
			this.debugRecipeCategory = new DebugRecipeCategory<>(guiHelper, platformFluidHelper, ingredientManager);
			registration.addRecipeCategories(
				debugRecipeCategory,
				new DebugFocusRecipeCategory<>(platformFluidHelper),
				new ObnoxiouslyLargeCategory(guiHelper, textures, ingredientManager)
			);
		}
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		if (DebugConfig.isDebugModeEnabled()) {
			registration.addItemStackInfo(List.of(
				new ItemStack(Blocks.OAK_DOOR),
				new ItemStack(Blocks.SPRUCE_DOOR),
				new ItemStack(Blocks.BIRCH_DOOR),
				new ItemStack(Blocks.JUNGLE_DOOR),
				new ItemStack(Blocks.ACACIA_DOOR),
				new ItemStack(Blocks.DARK_OAK_DOOR)
				),
				Component.translatable("description.dainty.wooden.door.1"), // actually 2 lines
				Component.translatable("description.dainty.wooden.door.2"),
				Component.translatable("description.dainty.wooden.door.3")
			);

			IJeiHelpers jeiHelpers = registration.getJeiHelpers();
			IPlatformFluidHelper<?> platformFluidHelper = jeiHelpers.getPlatformFluidHelper();
			registerFluidRecipes(registration, platformFluidHelper);
			registration.addIngredientInfo(new DebugIngredient(1), DebugIngredient.TYPE, Component.literal("debug"));
			registration.addIngredientInfo(new DebugIngredient(2), DebugIngredient.TYPE,
				Component.literal("debug colored").withStyle(ChatFormatting.AQUA),
				Component.literal("debug\\nSplit and colored").withStyle(ChatFormatting.LIGHT_PURPLE),
				Component.translatable("description.dainty.debug.formatting.1", "various"),
				Component.translatable("description.dainty.debug.formatting.1", "various\\nsplit"),
				Component.translatable("description.dainty.debug.formatting.1", Component.literal("various colored").withStyle(ChatFormatting.RED)),
				Component.translatable("description.dainty.debug.formatting.1",
					Component.literal("various\\nsplit colored").withStyle(ChatFormatting.DARK_AQUA)
				),
				Component.translatable("description.dainty.debug.formatting.1", "\\nSplitting at the start"),
				Component.translatable("description.dainty.debug.formatting.1", "various all colored").withStyle(ChatFormatting.RED),
				Component.translatable("description.dainty.debug.formatting.1",
					Component.translatable("description.dainty.debug.formatting.3", "various").withStyle(ChatFormatting.DARK_AQUA)
				),
				Component.translatable("description.dainty.debug.formatting.2",
					Component.literal("multiple").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.ITALIC),
					Component.literal("various").withStyle(ChatFormatting.RED)
				).withStyle(ChatFormatting.BLUE),
				Component.translatable("description.dainty.debug.formatting.1",
					Component.translatable("description.dainty.debug.formatting.3",
						Component.translatable("description.dainty.debug.formatting.2",
							Component.literal("multiple").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.ITALIC),
							Component.literal("various").withStyle(ChatFormatting.RED)
						).withStyle(ChatFormatting.DARK_AQUA)
					)
				)
			);

			registration.addRecipes(DebugRecipeCategory.TYPE, List.of(
				new DebugRecipe(),
				new DebugRecipe()
			));

			registration.addRecipes(DebugFocusRecipeCategory.TYPE, List.of(
				new DebugRecipe()
			));

			RecipeHolder<SmithingRecipe> testRecipeWithoutTemplate = new RecipeHolder<>(
				ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "test_recipe_without_template"),
				new SmithingTrimRecipe(Ingredient.EMPTY, Ingredient.of(new ItemStack(Items.APPLE)), Ingredient.of(new ItemStack(Items.BAKED_POTATO)))
			);
			registration.addRecipes(RecipeTypes.SMITHING, List.of(
				testRecipeWithoutTemplate
			));

			registration.addRecipes(ObnoxiouslyLargeCategory.TYPE, List.of(new ObnoxiouslyLargeRecipe()));
		}
	}

	private <T> void registerFluidRecipes(IRecipeRegistration registration, IPlatformFluidHelper<T> platformFluidHelper) {
		long bucketVolume = platformFluidHelper.bucketVolume();
		T fluidIngredient = platformFluidHelper.create(Fluids.WATER.defaultFluidState().holder(), bucketVolume);
		registration.addIngredientInfo(fluidIngredient, platformFluidHelper.getFluidIngredientType(), Component.literal("water"));

		fluidIngredient = platformFluidHelper.create(Fluids.LAVA.defaultFluidState().holder(), 1);
		registration.addIngredientInfo(fluidIngredient, platformFluidHelper.getFluidIngredientType(), Component.literal("small amount of lava that should still show as 1 bucket"));
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		if (DebugConfig.isDebugModeEnabled()) {
			IJeiHelpers jeiHelpers = registration.getJeiHelpers();
			IIngredientManager ingredientManager = jeiHelpers.getIngredientManager();

			registration.addGuiContainerHandler(BrewingStandScreen.class, new IGuiContainerHandler<>() {
				@Override
				public List<Rect2i> getGuiExtraAreas(BrewingStandScreen containerScreen) {
					int widthMovement = (int) ((System.currentTimeMillis() / 100) % 100);
					int size = 25 + widthMovement;
					IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
					int guiLeft = screenHelper.getGuiLeft(containerScreen);
					int xSize = screenHelper.getXSize(containerScreen);
					int guiTop = screenHelper.getGuiTop(containerScreen);
					return List.of(
						new Rect2i(guiLeft + xSize, guiTop + 40, size, size)
					);
				}

				@Override
				public Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse(BrewingStandScreen containerScreen, double mouseX, double mouseY) {
					Rect2i area = new Rect2i(0, 0, 10, 10);
					if (MathUtil.contains(area, mouseX, mouseY)) {
						return ingredientManager.createTypedIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.BOW))
							.map(item -> new DebugClickableIngredient<>(item, area));
					}
					return Optional.empty();
				}
			});

			registration.addGhostIngredientHandler(BrewingStandScreen.class, new DebugGhostIngredientHandler<>(ingredientManager));
			registration.addGhostIngredientHandler(BrewingStandScreen.class, new DebugGhostIngredientHandlerTwo<>(ingredientManager));
		}
	}

	private record DebugClickableIngredient<T>(
		ITypedIngredient<T> typedIngredient,
		Rect2i area
	) implements IClickableIngredient<T> {

		@SuppressWarnings("removal")
		@Override
		public ITypedIngredient<T> getTypedIngredient() {
			return typedIngredient;
		}

		@Override
		public IIngredientType<T> getIngredientType() {
			return typedIngredient.getType();
		}

		@Override
		public T getIngredient() {
			return typedIngredient.getIngredient();
		}

		@Override
		public Rect2i getArea() {
			return area;
		}
	}

	@Override
	public <T> void registerFluidSubtypes(ISubtypeRegistration registration, IPlatformFluidHelper<T> platformFluidHelper) {
		Fluid water = Fluids.WATER;
		IIngredientTypeWithSubtypes<Fluid, T> ingredientType = platformFluidHelper.getFluidIngredientType();
		FluidSubtypeHandlerTest<T> subtype = new FluidSubtypeHandlerTest<>(ingredientType);
		registration.registerSubtypeInterpreter(ingredientType, water, subtype);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		if (DebugConfig.isDebugModeEnabled()) {
			IPlatformFluidHelper<?> fluidHelper = Services.PLATFORM.getFluidHelper();
			registerRecipeCatalysts(registration, fluidHelper);
		}
	}

	private <T> void registerRecipeCatalysts(IRecipeCatalystRegistration registration, IPlatformFluidHelper<T> fluidHelper) {
		long bucketVolume = fluidHelper.bucketVolume();

		registration.addRecipeCatalyst(DebugIngredient.TYPE, new DebugIngredient(7), DebugRecipeCategory.TYPE);
		registration.addRecipeCatalyst(fluidHelper.getFluidIngredientType(), fluidHelper.create(Fluids.WATER.defaultFluidState().holder(), bucketVolume), DebugRecipeCategory.TYPE);
		registration.addRecipeCatalyst(Items.STICK, DebugRecipeCategory.TYPE);

		RegistryUtil.getRegistry(Registries.ITEM)
			.stream()
			.limit(300)
			.forEach(item -> {
				ItemStack catalystIngredient = new ItemStack(item);
				if (!catalystIngredient.isEmpty()) {
					registration.addRecipeCatalyst(catalystIngredient, DebugRecipeCategory.TYPE);
				}
			});
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {
		if (DebugConfig.isDebugModeEnabled()) {
			IJeiHelpers jeiHelpers = registration.getJeiHelpers();

			jeiHelpers
				.getAllRecipeTypes()
				.filter(r -> r.getUid().getNamespace().equals(ModIds.JEI_ID))
				.forEach(r -> registration.addRecipeCategoryDecorator(r, DebugCategoryDecorator.getInstance()));

			registration.addTypedRecipeManagerPlugin(RecipeTypes.CRAFTING, new DebugSimpleRecipeManagerPlugin(jeiHelpers));
		}
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		if (DebugConfig.isDebugModeEnabled()) {
			ErrorUtil.assertMainThread();
			Registry<Enchantment> registry = RegistryUtil.getRegistry(Registries.ENCHANTMENT);
			Enchantment enchantment = registry.get(Enchantments.FIRE_ASPECT);
			assert enchantment != null;
			if (debugRecipeCategory != null) {
				debugRecipeCategory.setRuntime(jeiRuntime);
			}
			IIngredientManager ingredientManager = jeiRuntime.getIngredientManager();
			ingredientManager.addIngredientsAtRuntime(DebugIngredient.TYPE, DebugIngredientListFactory.create(10, 20));
		}
	}
}
