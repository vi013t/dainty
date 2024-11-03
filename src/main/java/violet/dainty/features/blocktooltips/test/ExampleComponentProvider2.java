package violet.dainty.features.blocktooltips.test;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;

public enum ExampleComponentProvider2 implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		tooltip.append(Component.translatable("mymod.fuel"));
	}

	@Override
	public ResourceLocation getUid() {
		return ExamplePlugin.UID_TEST_FUEL;
	}

}
