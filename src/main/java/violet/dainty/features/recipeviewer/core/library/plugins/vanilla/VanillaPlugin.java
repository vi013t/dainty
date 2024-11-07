package violet.dainty.features.recipeviewer.core.library.plugins.vanilla;

import com.mojang.serialization.Codec;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.gui.screens.inventory.CrafterScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.gui.screens.inventory.SmokerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import net.minecraft.world.item.crafting.ShulkerBoxColoring;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.item.crafting.SuspiciousStewRecipe;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.gui.textures.Textures;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformFluidHelperInternal;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformRecipeHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.common.util.RegistryUtil;
import violet.dainty.features.recipeviewer.core.common.util.StackHelper;
import violet.dainty.features.recipeviewer.core.commonapi.IModPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.JeiPlugin;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;
import violet.dainty.features.recipeviewer.core.commonapi.constants.RecipeTypes;
import violet.dainty.features.recipeviewer.core.commonapi.constants.VanillaTypes;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IColorHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeManager;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.vanilla.crafting.IExtendableCraftingRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.extensions.vanilla.smithing.IExtendableSmithingRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.transfer.IRecipeTransferHandlerHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IJeiBrewingRecipe;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IVanillaRecipeFactory;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IGuiHandlerRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IModInfoRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IModIngredientRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeCatalystRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeCategoryRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IRecipeTransferRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.ISubtypeRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.registration.IVanillaCategoryExtensionRegistration;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.anvil.AnvilRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.anvil.AnvilRecipeMaker;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.anvil.SmithingRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.anvil.SmithingTransformCategoryExtension;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.anvil.SmithingTrimCategoryExtension;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.brewing.BrewingRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.compostable.CompostableRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.compostable.CompostingRecipeMaker;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking.BlastingCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking.CampfireCookingCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking.FurnaceSmeltingCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking.SmokingCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking.fuel.FuelRecipeMaker;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.cooking.fuel.FurnaceFuelCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.CraftingCategoryExtension;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.CraftingRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.VanillaRecipes;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.replacers.ShieldDecorationRecipeMaker;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.replacers.ShulkerBoxColoringRecipeMaker;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.replacers.SuspiciousStewRecipeMaker;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.crafting.replacers.TippedArrowRecipeMaker;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.gui.InventoryEffectRendererGuiHandler;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.gui.RecipeBookGuiHandler;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.gui.ToastGuiHandler;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.ItemStackHelper;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.ItemStackListFactory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.fluid.FluidIngredientHelper;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.fluid.FluidStackListFactory;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.subtypes.EnchantedBookSubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.subtypes.FireworkRocketSubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.subtypes.InstrumentSubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.subtypes.LightSubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.subtypes.OminousBottleSubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.subtypes.PaintingSubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.subtypes.PotionSubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.subtypes.SuspiciousStewSubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.library.plugins.vanilla.stonecutting.StoneCuttingRecipeCategory;
import violet.dainty.features.recipeviewer.core.library.render.FluidTankRenderer;
import violet.dainty.features.recipeviewer.core.library.render.ItemStackRenderer;
import violet.dainty.features.recipeviewer.core.library.transfer.PlayerRecipeTransferHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

@JeiPlugin
public class VanillaPlugin implements IModPlugin {
	private static final Logger LOGGER = LogManager.getLogger();

	@Nullable
	private CraftingRecipeCategory craftingCategory;
	@Nullable
	private IRecipeCategory<RecipeHolder<StonecutterRecipe>> stonecuttingCategory;
	@Nullable
	private IRecipeCategory<RecipeHolder<SmeltingRecipe>> furnaceCategory;
	@Nullable
	private IRecipeCategory<RecipeHolder<SmokingRecipe>> smokingCategory;
	@Nullable
	private IRecipeCategory<RecipeHolder<BlastingRecipe>> blastingCategory;
	@Nullable
	private IRecipeCategory<RecipeHolder<CampfireCookingRecipe>> campfireCategory;
	@Nullable
	private SmithingRecipeCategory smithingCategory;

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "minecraft");
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(Items.TIPPED_ARROW, PotionSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(Items.POTION, PotionSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(Items.SPLASH_POTION, PotionSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(Items.LINGERING_POTION, PotionSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(Items.ENCHANTED_BOOK, EnchantedBookSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(Items.LIGHT, LightSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(Items.PAINTING, PaintingSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(Items.GOAT_HORN, InstrumentSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(Items.FIREWORK_ROCKET, FireworkRocketSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(Items.SUSPICIOUS_STEW, SuspiciousStewSubtypeInterpreter.INSTANCE);
		registration.registerSubtypeInterpreter(Items.OMINOUS_BOTTLE, OminousBottleSubtypeInterpreter.INSTANCE);
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		ISubtypeManager subtypeManager = registration.getSubtypeManager();
		IColorHelper colorHelper = registration.getColorHelper();

		StackHelper stackHelper = new StackHelper(subtypeManager);
		ItemStackHelper itemStackHelper = new ItemStackHelper(stackHelper, colorHelper);
		List<ItemStack> itemStacks = ItemStackListFactory.create(stackHelper, itemStackHelper);
		ItemStackRenderer itemStackRenderer = new ItemStackRenderer();
		registration.register(
			VanillaTypes.ITEM_STACK,
			itemStacks,
			itemStackHelper,
			itemStackRenderer,
			ItemStack.STRICT_SINGLE_ITEM_CODEC
		);

		IPlatformFluidHelperInternal<?> platformFluidHelper = Services.PLATFORM.getFluidHelper();
		registerFluidIngredients(registration, platformFluidHelper);
	}

	@Override
	public void registerModInfo(IModInfoRegistration registration) {
		registration.addModAliases(ModIds.MINECRAFT_ID, "mc");
	}

	private <T> void registerFluidIngredients(IModIngredientRegistration registration, IPlatformFluidHelperInternal<T> platformFluidHelper) {
		ISubtypeManager subtypeManager = registration.getSubtypeManager();
		IColorHelper colorHelper = registration.getColorHelper();

		Registry<Fluid> registry = RegistryUtil.getRegistry(Registries.FLUID);
		List<T> fluidIngredients = FluidStackListFactory.create(registry, platformFluidHelper);
		FluidIngredientHelper<T> fluidIngredientHelper = new FluidIngredientHelper<>(subtypeManager, colorHelper, platformFluidHelper);
		FluidTankRenderer<T> fluidTankRenderer = new FluidTankRenderer<>(platformFluidHelper);
		IIngredientType<T> fluidIngredientType = platformFluidHelper.getFluidIngredientType();
		Codec<T> codec = platformFluidHelper.getCodec();
		registration.register(fluidIngredientType, fluidIngredients, fluidIngredientHelper, fluidTankRenderer, codec);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		Textures textures = Internal.getTextures();
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registration.addRecipeCategories(
			craftingCategory = new CraftingRecipeCategory(guiHelper),
			stonecuttingCategory = new StoneCuttingRecipeCategory(guiHelper),
			furnaceCategory = new FurnaceSmeltingCategory(guiHelper),
			smokingCategory = new SmokingCategory(guiHelper),
			blastingCategory = new BlastingCategory(guiHelper),
			campfireCategory = new CampfireCookingCategory(guiHelper),
			smithingCategory = new SmithingRecipeCategory(guiHelper),
			new CompostableRecipeCategory(guiHelper),
			new FurnaceFuelCategory(textures),
			new BrewingRecipeCategory(guiHelper),
			new AnvilRecipeCategory(guiHelper)
		);
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		IExtendableCraftingRecipeCategory craftingCategory = registration.getCraftingCategory();
		craftingCategory.addExtension(CraftingRecipe.class, new CraftingCategoryExtension());

		IExtendableSmithingRecipeCategory smithingCategory = registration.getSmithingCategory();
		IPlatformRecipeHelper recipeHelper = Services.PLATFORM.getRecipeHelper();
		smithingCategory.addExtension(SmithingTransformRecipe.class, new SmithingTransformCategoryExtension(recipeHelper));
		smithingCategory.addExtension(SmithingTrimRecipe.class, new SmithingTrimCategoryExtension(recipeHelper));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		ErrorUtil.checkNotNull(craftingCategory, "craftingCategory");
		ErrorUtil.checkNotNull(stonecuttingCategory, "stonecuttingCategory");
		ErrorUtil.checkNotNull(furnaceCategory, "furnaceCategory");
		ErrorUtil.checkNotNull(smokingCategory, "smokingCategory");
		ErrorUtil.checkNotNull(blastingCategory, "blastingCategory");
		ErrorUtil.checkNotNull(campfireCategory, "campfireCategory");
		ErrorUtil.checkNotNull(smithingCategory, "smithingCategory");

		IIngredientManager ingredientManager = registration.getIngredientManager();
		IVanillaRecipeFactory vanillaRecipeFactory = registration.getVanillaRecipeFactory();
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		VanillaRecipes vanillaRecipes = new VanillaRecipes(ingredientManager);

		var craftingRecipes = vanillaRecipes.getCraftingRecipes(craftingCategory);
		var handledCraftingRecipes = craftingRecipes.get(true);
		var unhandledCraftingRecipes = craftingRecipes.get(false);
		var specialCraftingRecipes = replaceSpecialCraftingRecipes(unhandledCraftingRecipes, jeiHelpers);

		registration.addRecipes(RecipeTypes.CRAFTING, handledCraftingRecipes);
		registration.addRecipes(RecipeTypes.CRAFTING, specialCraftingRecipes);

		registration.addRecipes(RecipeTypes.STONECUTTING, vanillaRecipes.getStonecuttingRecipes(stonecuttingCategory));
		registration.addRecipes(RecipeTypes.SMELTING, vanillaRecipes.getFurnaceRecipes(furnaceCategory));
		registration.addRecipes(RecipeTypes.SMOKING, vanillaRecipes.getSmokingRecipes(smokingCategory));
		registration.addRecipes(RecipeTypes.BLASTING, vanillaRecipes.getBlastingRecipes(blastingCategory));
		registration.addRecipes(RecipeTypes.CAMPFIRE_COOKING, vanillaRecipes.getCampfireCookingRecipes(campfireCategory));
		registration.addRecipes(RecipeTypes.FUELING, FuelRecipeMaker.getFuelRecipes(ingredientManager));
		registration.addRecipes(RecipeTypes.ANVIL, AnvilRecipeMaker.getAnvilRecipes(vanillaRecipeFactory, ingredientManager));
		registration.addRecipes(RecipeTypes.SMITHING, vanillaRecipes.getSmithingRecipes(smithingCategory));
		registration.addRecipes(RecipeTypes.COMPOSTING, CompostingRecipeMaker.getRecipes(ingredientManager));

		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		ErrorUtil.checkNotNull(level, "minecraft.level");
		PotionBrewing potionBrewing = level.potionBrewing();
		IPlatformRecipeHelper recipeHelper = Services.PLATFORM.getRecipeHelper();
		List<IJeiBrewingRecipe> brewingRecipes = recipeHelper.getBrewingRecipes(ingredientManager, vanillaRecipeFactory, potionBrewing);
		brewingRecipes.sort(Comparator.comparingInt(IJeiBrewingRecipe::getBrewingSteps));
		registration.addRecipes(RecipeTypes.BREWING, brewingRecipes);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(CraftingScreen.class, 88, 32, 28, 23, RecipeTypes.CRAFTING);
		registration.addRecipeClickArea(CrafterScreen.class, 88, 32, 28, 23, RecipeTypes.CRAFTING);
		registration.addRecipeClickArea(InventoryScreen.class, 137, 29, 10, 13, RecipeTypes.CRAFTING);
		registration.addRecipeClickArea(BrewingStandScreen.class, 97, 16, 14, 30, RecipeTypes.BREWING);
		registration.addRecipeClickArea(FurnaceScreen.class, 78, 32, 28, 23, RecipeTypes.SMELTING, RecipeTypes.FUELING);
		registration.addRecipeClickArea(SmokerScreen.class, 78, 32, 28, 23, RecipeTypes.SMOKING, RecipeTypes.FUELING);
		registration.addRecipeClickArea(BlastFurnaceScreen.class, 78, 32, 28, 23, RecipeTypes.BLASTING, RecipeTypes.FUELING);
		registration.addRecipeClickArea(AnvilScreen.class, 102, 48, 22, 15, RecipeTypes.ANVIL);
		registration.addRecipeClickArea(SmithingScreen.class, 68, 49, 22, 15, RecipeTypes.SMITHING);

		registration.addGenericGuiContainerHandler(EffectRenderingInventoryScreen.class, new InventoryEffectRendererGuiHandler<>());
		registration.addGuiContainerHandler(CraftingScreen.class, new RecipeBookGuiHandler<>());
		registration.addGuiContainerHandler(InventoryScreen.class, new RecipeBookGuiHandler<>());
		registration.addGuiContainerHandler(AbstractFurnaceScreen.class, new RecipeBookGuiHandler<>());
		registration.addGlobalGuiHandler(new ToastGuiHandler());
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(CraftingMenu.class, MenuType.CRAFTING, RecipeTypes.CRAFTING, 1, 9, 10, 36);
		registration.addRecipeTransferHandler(CrafterMenu.class, MenuType.CRAFTER_3x3, RecipeTypes.CRAFTING, 0, 9, 9, 36);
		registration.addRecipeTransferHandler(FurnaceMenu.class, MenuType.FURNACE, RecipeTypes.SMELTING, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(FurnaceMenu.class, MenuType.FURNACE, RecipeTypes.FUELING, 1, 1, 3, 36);
		registration.addRecipeTransferHandler(SmokerMenu.class, MenuType.SMOKER, RecipeTypes.SMOKING, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(SmokerMenu.class, MenuType.SMOKER, RecipeTypes.FUELING, 1, 1, 3, 36);
		registration.addRecipeTransferHandler(BlastFurnaceMenu.class, MenuType.BLAST_FURNACE, RecipeTypes.BLASTING, 0, 1, 3, 36);
		registration.addRecipeTransferHandler(BlastFurnaceMenu.class, MenuType.BLAST_FURNACE, RecipeTypes.FUELING, 1, 1, 3, 36);
		registration.addRecipeTransferHandler(BrewingStandMenu.class, MenuType.BREWING_STAND, RecipeTypes.BREWING, 0, 4, 5, 36);
		registration.addRecipeTransferHandler(AnvilMenu.class, MenuType.ANVIL, RecipeTypes.ANVIL, 0, 2, 3, 36);
		registration.addRecipeTransferHandler(SmithingMenu.class, MenuType.SMITHING, RecipeTypes.SMITHING, 0, 3, 3, 36);

		IRecipeTransferHandlerHelper transferHelper = registration.getTransferHelper();
		PlayerRecipeTransferHandler recipeTransferHandler = new PlayerRecipeTransferHandler(transferHelper);
		registration.addRecipeTransferHandler(recipeTransferHandler, RecipeTypes.CRAFTING);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalysts(RecipeTypes.CRAFTING,
			Blocks.CRAFTING_TABLE,
			Blocks.CRAFTER
		);
		registration.addRecipeCatalysts(RecipeTypes.FUELING,
			Blocks.FURNACE,
			Blocks.SMOKER,
			Blocks.BLAST_FURNACE
		);
		registration.addRecipeCatalysts(RecipeTypes.CAMPFIRE_COOKING,
			Blocks.CAMPFIRE,
			Blocks.SOUL_CAMPFIRE
		);
		registration.addRecipeCatalyst(Blocks.STONECUTTER, RecipeTypes.STONECUTTING);
		registration.addRecipeCatalyst(Blocks.FURNACE, RecipeTypes.SMELTING);
		registration.addRecipeCatalyst(Blocks.SMOKER, RecipeTypes.SMOKING);
		registration.addRecipeCatalyst(Blocks.BLAST_FURNACE, RecipeTypes.BLASTING);
		registration.addRecipeCatalyst(Blocks.BREWING_STAND, RecipeTypes.BREWING);
		registration.addRecipeCatalyst(Blocks.ANVIL, RecipeTypes.ANVIL);
		registration.addRecipeCatalyst(Blocks.SMITHING_TABLE, RecipeTypes.SMITHING);
		registration.addRecipeCatalyst(Blocks.COMPOSTER, RecipeTypes.COMPOSTING);
	}

	public Optional<CraftingRecipeCategory> getCraftingCategory() {
		return Optional.ofNullable(craftingCategory);
	}

	public Optional<SmithingRecipeCategory> getSmithingCategory() {
		return Optional.ofNullable(smithingCategory);
	}

	/**
	 * By default, JEI can't handle special recipes.
	 * This method expands some special unhandled recipes into a list of normal recipes that JEI can understand.
	 * <p>
	 * If a special recipe we know how to replace is not present (because it has been removed),
	 * we do not replace it.
	 */
	private static List<RecipeHolder<CraftingRecipe>> replaceSpecialCraftingRecipes(List<RecipeHolder<CraftingRecipe>> unhandledCraftingRecipes, IJeiHelpers jeiHelpers) {
		Map<Class<? extends CraftingRecipe>, Supplier<List<RecipeHolder<CraftingRecipe>>>> replacers = new IdentityHashMap<>();
		replacers.put(TippedArrowRecipe.class, () -> TippedArrowRecipeMaker.createRecipes(jeiHelpers));
		replacers.put(ShulkerBoxColoring.class, ShulkerBoxColoringRecipeMaker::createRecipes);
		replacers.put(SuspiciousStewRecipe.class, SuspiciousStewRecipeMaker::createRecipes);
		replacers.put(ShieldDecorationRecipe.class, ShieldDecorationRecipeMaker::createRecipes);

		return unhandledCraftingRecipes.stream()
			.map(RecipeHolder::value)
			.map(CraftingRecipe::getClass)
			.distinct()
			.filter(replacers::containsKey)
			// distinct + this limit will ensure we stop iterating early if we find all the recipes we're looking for.
			.limit(replacers.size())
			.flatMap(recipeClass -> {
				var supplier = replacers.get(recipeClass);
				try {
					return supplier.get()
						.stream();
				} catch (RuntimeException e) {
					LOGGER.error("Failed to create JEI recipes for {}", recipeClass, e);
					return Stream.of();
				}
			})
			.toList();
	}
}
