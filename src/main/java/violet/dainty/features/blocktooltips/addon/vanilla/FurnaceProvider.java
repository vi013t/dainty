package violet.dainty.features.blocktooltips.addon.vanilla;

import java.util.List;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.phys.Vec2;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.StreamServerDataProvider;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;
import violet.dainty.mixins.blocktooltips.AbstractFurnaceBlockEntityAccess;

public enum FurnaceProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, FurnaceProvider.Data> {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		Data data = decodeFromData(accessor).orElse(null);
		if (data == null) {
			return;
		}
		IElementHelper helper = IElementHelper.get();
		tooltip.add(helper.item(data.inventory.get(0)));
		tooltip.append(helper.item(data.inventory.get(1)));
		tooltip.append(helper.spacer(4, 0));
		tooltip.append(helper.progress((float) data.progress / data.total).translate(new Vec2(-2, 0)));
		tooltip.append(helper.item(data.inventory.get(2)));
	}

	@Override
	public Data streamData(BlockAccessor accessor) {
		AbstractFurnaceBlockEntityAccess access = (AbstractFurnaceBlockEntityAccess) accessor.getBlockEntity();
		AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity) accessor.getBlockEntity();
		return new Data(
				access.getCookingProgress(),
				access.getCookingTotalTime(),
				List.of(furnace.getItem(0), furnace.getItem(1), furnace.getItem(2)));
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
		return Data.STREAM_CODEC;
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_FURNACE;
	}

	public record Data(int progress, int total, List<ItemStack> inventory) {
		public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.VAR_INT,
				Data::progress,
				ByteBufCodecs.VAR_INT,
				Data::total,
				ItemStack.OPTIONAL_LIST_STREAM_CODEC,
				Data::inventory,
				Data::new);
	}

}
