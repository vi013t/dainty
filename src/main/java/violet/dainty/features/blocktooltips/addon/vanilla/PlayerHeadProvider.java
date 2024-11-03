package violet.dainty.features.blocktooltips.addon.vanilla;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;

public enum PlayerHeadProvider implements IBlockComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (accessor.getBlockEntity() instanceof SkullBlockEntity tile) {
			ResolvableProfile profile = tile.getOwnerProfile();
			if (profile == null || !profile.isResolved()) {
				return;
			}
			String name = profile.name().orElse(null);
			if (StringUtils.isBlank(name)) {
				return;
			}
			if (!name.contains(" ") && !name.contains("§")) {
				name = I18n.get(Items.PLAYER_HEAD.getDescriptionId() + ".named", name);
			}
			tooltip.replace(JadeIds.CORE_OBJECT_NAME, IThemeHelper.get().title(name));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_PLAYER_HEAD;
	}

}
