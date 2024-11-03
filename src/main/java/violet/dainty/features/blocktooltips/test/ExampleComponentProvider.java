package violet.dainty.features.blocktooltips.test;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.phys.Vec2;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;

public enum ExampleComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (accessor.getServerData().contains("Fuel")) {
			IElement icon = IElementHelper.get().item(new ItemStack(Items.CLOCK), 0.5f).size(new Vec2(10, 10)).translate(new Vec2(0, -1));
			icon.message(null);
			tooltip.add(icon);
			tooltip.append(Component.translatable("mymod.fuel", accessor.getServerData().getInt("Fuel")));
		}

		Component test1 = Component.literal("1");
		Component test2 = Component.literal("2");
		Component test3 = Component.literal("3");
		tooltip.add(IElementHelper.get().text(test2).align(IElement.Align.RIGHT));
		tooltip.add(IElementHelper.get().text(test3).align(IElement.Align.CENTER));
		tooltip.add(IElementHelper.get().text(test1).align(IElement.Align.LEFT));
		tooltip.append(IElementHelper.get().text(test1).align(IElement.Align.RIGHT));
		tooltip.append(IElementHelper.get().text(test1).align(IElement.Align.CENTER));
		tooltip.append(IElementHelper.get().text(test2).align(IElement.Align.CENTER));
		tooltip.append(IElementHelper.get().text(test2).align(IElement.Align.RIGHT));
		tooltip.append(IElementHelper.get().text(test3).align(IElement.Align.RIGHT));
		tooltip.append(IElementHelper.get().text(test2).align(IElement.Align.LEFT));
		tooltip.append(IElementHelper.get().text(test3).align(IElement.Align.LEFT));
		tooltip.append(IElementHelper.get().text(test3).align(IElement.Align.CENTER));

		IElement text = IElementHelper.get().text(Component.literal("test"));
		tooltip.replace(JadeIds.CORE_OBJECT_NAME, $ -> List.of(List.of(text), List.of(text), List.of(text)));
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
