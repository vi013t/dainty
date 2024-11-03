package violet.dainty.features.blocktooltips.api.callback;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.phys.HitResult;
import violet.dainty.features.blocktooltips.api.Accessor;

@FunctionalInterface
public interface JadeRayTraceCallback {

	@Nullable
	Accessor<?> onRayTrace(HitResult hitResult, @Nullable Accessor<?> accessor, @Nullable Accessor<?> originalAccessor);

}
