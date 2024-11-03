package violet.dainty.features.blocktooltips.impl.template;

import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.TooltipPosition;

public final class TemplateBlockComponentProvider extends TemplateComponentProvider<BlockAccessor> implements IBlockComponentProvider {
	public TemplateBlockComponentProvider(ResourceLocation uid) {
		this(uid, false, true, TooltipPosition.BODY);
	}

	public TemplateBlockComponentProvider(ResourceLocation uid, boolean required, boolean enabledByDefault, int defaultPriority) {
		super(uid, required, enabledByDefault, defaultPriority);
	}
}
