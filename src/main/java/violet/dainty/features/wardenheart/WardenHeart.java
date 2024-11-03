package violet.dainty.features.wardenheart;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

public class WardenHeart extends Item {

	public WardenHeart() {
		super(new Item.Properties().rarity(Rarity.RARE).fireResistant());
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		if (Screen.hasShiftDown()) {
			tooltipComponents.add(Component.literal("Crafting component for late-game items.").withStyle(ChatFormatting.GRAY));
			tooltipComponents.add(Component.literal("Dropped by Wardens on death.").withStyle(ChatFormatting.GRAY));
		} else {
			tooltipComponents.add(Component.literal("Hold SHIFT for more information...").setStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.DARK_GRAY)));
		} 
	}
	
}
