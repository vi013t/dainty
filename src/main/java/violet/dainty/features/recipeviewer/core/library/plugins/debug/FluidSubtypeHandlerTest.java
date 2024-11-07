package violet.dainty.features.recipeviewer.core.library.plugins.debug;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import violet.dainty.features.recipeviewer.core.common.util.RegistryUtil;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientTypeWithSubtypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.ISubtypeInterpreter;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;

import org.jetbrains.annotations.Nullable;

public class FluidSubtypeHandlerTest<T> implements ISubtypeInterpreter<T> {
	private final IIngredientTypeWithSubtypes<Fluid, T> fluidType;

	public FluidSubtypeHandlerTest(IIngredientTypeWithSubtypes<Fluid, T> fluidType) {
		this.fluidType = fluidType;
	}

	@Override
	public @Nullable Object getSubtypeData(T ingredient, UidContext context) {
		return fluidType.getBase(ingredient);
	}

	@Override
	public String getLegacyStringSubtypeInfo(T fluidStack, UidContext context) {
		Fluid fluid = fluidType.getBase(fluidStack);
		ResourceLocation key = RegistryUtil
			.getRegistry(Registries.FLUID)
			.getKey(fluid);
		if (key == null) {
			throw new IllegalArgumentException("Fluid has no registry key: " + fluid);
		}
		return key.toString();
	}
}
