package violet.dainty.features.recipeviewer.core.library.plugins.vanilla.ingredients.fluid;

import net.minecraft.core.Registry;
import net.minecraft.world.level.material.Fluid;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IPlatformFluidHelper;

import java.util.List;

public final class FluidStackListFactory {
	private FluidStackListFactory() {

	}

	public static <T> List<T> create(Registry<Fluid> registry, IPlatformFluidHelper<T> helper) {
		return registry.holders()
			.filter(holder -> {
				Fluid fluid = holder.value();
				return fluid.isSource(fluid.defaultFluidState());
			})
			.map(holder -> helper.create(holder, helper.bucketVolume()))
			.toList();
	}
}
