package violet.dainty.features.blocktooltips.addon.access;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;

public class SignProvider implements IBlockComponentProvider {
	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!(accessor.getBlockEntity() instanceof SignBlockEntity be)) {
			return;
		}
		boolean front = be.isFacingFrontText(accessor.getPlayer());
		tooltip.add(Component.translatable("dainty.access.sign." + (front ? "front" : "back")));
		int i = 0;
		for (Component message : be.getFrontText().getMessages(true)) {
			++i;
			if (accessor.showDetails()) {
				tooltip.add(Component.translatable("dainty.access.sign.line" + i, message));
			} else {
				tooltip.add(message);
			}
			if (i >= 4) {
				break;
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.ACCESS_SIGN;
	}
}
