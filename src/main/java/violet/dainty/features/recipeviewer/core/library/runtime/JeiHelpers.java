package violet.dainty.features.recipeviewer.core.library.runtime;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.ICodecHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IColorHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IGuiHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IModIdHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IPlatformFluidHelper;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IStackHelper;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.IFocusFactory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.RecipeType;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.category.IRecipeCategory;
import violet.dainty.features.recipeviewer.core.commonapi.recipe.vanilla.IVanillaRecipeFactory;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientVisibility;
import violet.dainty.features.recipeviewer.core.library.gui.helpers.GuiHelper;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

public class JeiHelpers implements IJeiHelpers {
	private final GuiHelper guiHelper;
	private final IStackHelper stackHelper;
	private final IModIdHelper modIdHelper;
	private final IFocusFactory focusFactory;
	private final IColorHelper colorHelper;
	private final IIngredientManager ingredientManager;
	private final IVanillaRecipeFactory vanillaRecipeFactory;
	private final IIngredientVisibility ingredientVisibility;
	private final IPlatformFluidHelper<?> platformFluidHelper;
	private final ICodecHelper codecHelper;
	private @Nullable Collection<IRecipeCategory<?>> recipeCategories;

	public JeiHelpers(
		GuiHelper guiHelper,
		IStackHelper stackHelper,
		IModIdHelper modIdHelper,
		IFocusFactory focusFactory,
		IColorHelper colorHelper,
		IIngredientManager ingredientManager,
		IVanillaRecipeFactory vanillaRecipeFactory,
		ICodecHelper codecHelper,
		IIngredientVisibility ingredientVisibility
	) {
		this.guiHelper = guiHelper;
		this.stackHelper = stackHelper;
		this.modIdHelper = modIdHelper;
		this.focusFactory = focusFactory;
		this.colorHelper = colorHelper;
		this.ingredientManager = ingredientManager;
		this.vanillaRecipeFactory = vanillaRecipeFactory;
		this.ingredientVisibility = ingredientVisibility;
		this.platformFluidHelper = Services.PLATFORM.getFluidHelper();
		this.codecHelper = codecHelper;
	}

	public void setRecipeCategories(Collection<IRecipeCategory<?>> recipeCategories) {
		this.recipeCategories = Collections.unmodifiableCollection(recipeCategories);
	}

	@Override
	public IGuiHelper getGuiHelper() {
		return guiHelper;
	}

	@Override
	public IStackHelper getStackHelper() {
		return stackHelper;
	}

	@Override
	public IModIdHelper getModIdHelper() {
		return modIdHelper;
	}

	@Override
	public IFocusFactory getFocusFactory() {
		return focusFactory;
	}

	@Override
	public IColorHelper getColorHelper() {
		return colorHelper;
	}

	@Override
	public IPlatformFluidHelper<?> getPlatformFluidHelper() {
		return platformFluidHelper;
	}

	@Override
	public <T> Optional<RecipeType<T>> getRecipeType(ResourceLocation uid, Class<? extends T> recipeClass) {
		return Optional.ofNullable(this.recipeCategories)
			.flatMap(r -> r.stream()
				.map(IRecipeCategory::getRecipeType)
				.filter(t -> t.getUid().equals(uid) && t.getRecipeClass().equals(recipeClass))
				.map(t -> {
					@SuppressWarnings("unchecked")
					RecipeType<T> cast = (RecipeType<T>) t;
					return cast;
				})
				.findFirst()
			);
	}

	@Override
	public Optional<RecipeType<?>> getRecipeType(ResourceLocation uid) {
		return Optional.ofNullable(this.recipeCategories)
			.flatMap(r -> r.stream()
				.map(IRecipeCategory::getRecipeType)
				.filter(t -> t.getUid().equals(uid))
				.findFirst()
			);
	}

	@Override
	public Stream<RecipeType<?>> getAllRecipeTypes() {
		if (this.recipeCategories == null) {
			return Stream.of();
		}
		return this.recipeCategories.stream()
			.map(IRecipeCategory::getRecipeType);
	}

	@Override
	public IIngredientManager getIngredientManager() {
		return ingredientManager;
	}

	@Override
	public ICodecHelper getCodecHelper() {
		return codecHelper;
	}

	@Override
	public IVanillaRecipeFactory getVanillaRecipeFactory() {
		return vanillaRecipeFactory;
	}

	@Override
	public IIngredientVisibility getIngredientVisibility() {
		return ingredientVisibility;
	}
}
