package violet.dainty.features.blocktooltips.addon.vanilla;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import violet.dainty.features.blocktooltips.JadeClient;
import violet.dainty.features.blocktooltips.api.EntityAccessor;
import violet.dainty.features.blocktooltips.api.IEntityComponentProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.TraceableException;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.blocktooltips.impl.ui.TextElement;
import violet.dainty.features.blocktooltips.util.ModIdentification;
import violet.dainty.features.blocktooltips.util.WailaExceptionHandler;

public enum ItemTooltipProvider implements IEntityComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		ItemStack stack = ((ItemEntity) accessor.getEntity()).getItem();
		Item.TooltipContext tooltipContext = Item.TooltipContext.of(accessor.getLevel());
		JadeClient.hideModNameIn(tooltipContext);
		List<Either<FormattedText, TooltipComponent>> lines = Lists.newArrayList();
		try {
			stack.getTooltipLines(tooltipContext, null, TooltipFlag.Default.NORMAL)
					.stream()
					.peek(component -> {
						if (component instanceof MutableComponent mutable && mutable.getStyle().getColor() != null) {
							mutable.setStyle(mutable.getStyle().withColor((TextColor) null));
						}
					})
					.map(Either::<FormattedText, TooltipComponent>left)
					.forEach(lines::add);
		} catch (Throwable e) {
			String namespace = BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace();
			WailaExceptionHandler.handleErr(TraceableException.create(e, namespace), this, tooltip::add);
		}
		if (lines.isEmpty()) {
			return;
		}
		List<FormattedText> realLines = lines.stream().map($ -> $.left()).filter(Optional::isPresent).map(Optional::get).skip(1).toList();
		String modName = ModIdentification.getModName(stack);
		@SuppressWarnings("resource")
		Font font = Minecraft.getInstance().font;
		int maxWidth = 250;
		for (FormattedText text : realLines) {
			if (Objects.equals(ChatFormatting.stripFormatting(text.getString()), modName)) {
				continue;
			}
			int width = font.width(text);
			if (width > maxWidth) {
				tooltip.add(Component.literal(font.substrByWidth(text, maxWidth - 5).getString() + ".."));
			} else {
				tooltip.add(new TextElement(text));
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return JadeIds.MC_ITEM_TOOLTIP;
	}

}
