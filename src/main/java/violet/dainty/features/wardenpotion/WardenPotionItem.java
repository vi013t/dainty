package violet.dainty.features.wardenpotion;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import violet.dainty.registries.DaintyPotions;

public class WardenPotionItem extends PotionItem {

	private final boolean isLong;

	public WardenPotionItem(boolean isLong) {
		super(new Item.Properties().rarity(Rarity.RARE));
		this.isLong = isLong;
	}
	
    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemStack = super.getDefaultInstance();
        itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(this.isLong ? DaintyPotions.LONG_WARDEN_POTION : DaintyPotions.WARDEN_POTION));
        return itemStack;
    }

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
		if (Screen.hasShiftDown()) {
			tooltipComponents.add(Component.literal("Immune to darkness effects and walk silently.").withStyle(ChatFormatting.GRAY));
		} else {
			tooltipComponents.add(Component.literal("Hold SHIFT for more information...").setStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.DARK_GRAY)));
		}
	}
}
