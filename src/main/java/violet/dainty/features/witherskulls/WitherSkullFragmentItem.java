package violet.dainty.features.witherskulls;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class WitherSkullFragmentItem extends Item {

	private WitherSkullFragmentItem() {
		super(new Item.Properties());
	}

	public static Supplier<WitherSkullFragmentItem> create() {
		return WitherSkullFragmentItem::new;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		if (Screen.hasShiftDown()) {
			tooltipComponents.add(Component.literal("Crafting component for wither skeleton skulls.").withStyle(ChatFormatting.GRAY));
		} else {
			tooltipComponents.add(Component.literal("Hold SHIFT for more information...").setStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.DARK_GRAY)));
		} 
	}
	
}
