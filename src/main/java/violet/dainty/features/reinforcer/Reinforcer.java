package violet.dainty.features.reinforcer;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

public class Reinforcer extends Item {

	public Reinforcer() {
		super(new Item.Properties().fireResistant().rarity(Rarity.EPIC));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		if (Screen.hasShiftDown()) {
			tooltipComponents.add(Component.literal("Combine with any item in an anvil to").withStyle(ChatFormatting.GRAY));
			tooltipComponents.add(Component.literal("make it unbreakable and unburnable.").withStyle(ChatFormatting.GRAY));
		} else {
			tooltipComponents.add(Component.literal("Hold SHIFT for more information...").setStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.DARK_GRAY)));
		}
	}
}
