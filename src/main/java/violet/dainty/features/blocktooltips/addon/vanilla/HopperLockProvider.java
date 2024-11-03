package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import violet.dainty.features.blocktooltips.addon.access.AccessibilityPlugin;
import violet.dainty.features.blocktooltips.addon.core.ObjectNameProvider;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.StreamServerDataProvider;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.config.IWailaConfig;

public enum HopperLockProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, Boolean> {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (decodeFromData(accessor).orElse(false)) {
			if (config.get(JadeIds.MC_REDSTONE)) {
				AccessibilityPlugin.replaceTitle(tooltip, "block.locked");
			} else if (IWailaConfig.get().getGeneral().getEnableAccessibilityPlugin() && config.get(JadeIds.ACCESS_BLOCK_DETAILS)) {
				AccessibilityPlugin.replaceTitle(tooltip, "block.locked");
			}
		}
	}

	@Override
	public Boolean streamData(BlockAccessor accessor) {
		return !accessor.getBlockState().getValue(BlockStateProperties.ENABLED);
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, Boolean> streamCodec() {
		return ByteBufCodecs.BOOL.cast();
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_HOPPER_LOCK;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

	@Override
	public int getDefaultPriority() {
		return ObjectNameProvider.getBlock().getDefaultPriority() + 10;
	}
}
