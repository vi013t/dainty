package violet.dainty.features.blocktooltips.test;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;

public enum ExampleComponentProvider3 implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (accessor.getServerData().contains("Fuel")) {
			tooltip.append(Component.translatable("mymod.fuel", accessor.getServerData().getInt("Fuel")));
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity) accessor.getBlockEntity();
		data.putInt("Fuel", furnace.litTime);
	}

	@Override
	public ResourceLocation getUid() {
		return ExamplePlugin.UID_TEST_FUEL;
	}

}
