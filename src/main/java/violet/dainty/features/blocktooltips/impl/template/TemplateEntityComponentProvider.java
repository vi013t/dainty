package violet.dainty.features.blocktooltips.impl.template;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.TooltipPosition;

public final class TemplateEntityComponentProvider extends TemplateComponentProvider<EntityAccessor> implements IEntityComponentProvider {
	public TemplateEntityComponentProvider(ResourceLocation uid) {
		this(uid, false, true, TooltipPosition.BODY);
	}

	public TemplateEntityComponentProvider(ResourceLocation uid, boolean required, boolean enabledByDefault, int defaultPriority) {
		super(uid, required, enabledByDefault, defaultPriority);
	}
}
