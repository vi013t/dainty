package violet.dainty.features.blocktooltips.addon.vanilla;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.StreamServerDataProvider;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.IDisplayHelper;

public enum JukeboxProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, ItemStack> {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		ItemStack stack = decodeFromData(accessor).orElse(ItemStack.EMPTY);
		if (stack.isEmpty()) {
			tooltip.add(Component.translatable("tooltip.dainty.empty"));
			return;
		}
		Component name;
		JukeboxPlayable playable = stack.get(DataComponents.JUKEBOX_PLAYABLE);
		if (playable != null) {
			name = playable.song()
					.unwrap(accessor.getLevel().registryAccess())
					.map($ -> $.value().description())
					.orElse(stack.getHoverName());
		} else {
			name = stack.getHoverName();
		}
		tooltip.add(Component.translatable("record.nowPlaying", IDisplayHelper.get().stripColor(name)));
	}

	@Override
	public boolean shouldRequestData(BlockAccessor accessor) {
		return accessor.getBlockState().getValue(JukeboxBlock.HAS_RECORD);
	}

	@Override
	public ItemStack streamData(BlockAccessor accessor) {
		return ((JukeboxBlockEntity) accessor.getBlockEntity()).getTheItem();
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, ItemStack> streamCodec() {
		return ItemStack.OPTIONAL_STREAM_CODEC;
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_JUKEBOX;
	}
}
