package violet.dainty.features.blocktooltips.addon.access;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.IWailaClientRegistration;
import violet.dainty.features.blocktooltips.api.IWailaPlugin;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.WailaPlugin;
import violet.dainty.features.blocktooltips.api.theme.IThemeHelper;

@WailaPlugin
public class AccessibilityPlugin implements IWailaPlugin {
	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerBlockComponent(new SignProvider(), SignBlock.class);
		registration.markAsClientFeature(JadeIds.ACCESS_SIGN);

		registration.registerBlockComponent(new BlockDetailsProvider(), Block.class);
		registration.registerBlockComponent(new BlockDetailsBodyProvider(), Block.class);
		registration.markAsClientFeature(JadeIds.ACCESS_BLOCK_DETAILS);

		registration.registerBlockComponent(new BlockAmountProvider(), Block.class);
		registration.markAsClientFeature(JadeIds.ACCESS_BLOCK_AMOUNT);

		registration.registerEntityComponent(new EntityDetailsProvider(), Entity.class);
		registration.registerEntityComponent(new EntityDetailsBodyProvider(), Entity.class);
		registration.markAsClientFeature(JadeIds.ACCESS_ENTITY_DETAILS);

		registration.registerEntityComponent(new EntityVariantProvider(), LivingEntity.class);
		registration.markAsClientFeature(JadeIds.ACCESS_ENTITY_VARIANT);

		registration.registerEntityComponent(new HeldItemProvider(), LivingEntity.class);
		registration.markAsClientFeature(JadeIds.ACCESS_HELD_ITEM);
	}

	public static void replaceTitle(ITooltip tooltip, String key) {
		String message = tooltip.getMessage(JadeIds.CORE_OBJECT_NAME);
		if (!message.isBlank()) {
			var title = IThemeHelper.get().title(Component.translatable("dainty.access." + key, message));
			tooltip.replace(JadeIds.CORE_OBJECT_NAME, title);
		}
	}
}
