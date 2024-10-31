package violet.dainty.features.bag;

import java.util.List;

import com.google.common.base.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import violet.dainty.registries.DaintyDataComponents;

public class Bag extends Item {

	private final Tier tier;

	private Bag(Bag.Tier tier) {
		super(new Item.Properties().component(DaintyDataComponents.BAG_DATA_COMPONENT, BagDataComponent.empty(tier.capacity())));
		this.tier = tier;
	}

	public static Supplier<Bag> create(Tier tier) {
		return () -> new Bag(tier);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		if (Screen.hasShiftDown()) {
			tooltipComponents.add(
				Component.literal("Holds up to ")
				.append(Component.literal(Integer.toString(this.tier.capacity() / 64)).withColor(this.tier.color()))
				.append(Component.literal(" stacks of "))
				.append(Component.literal(Integer.toString(this.tier.maxItemVariants())).withColor(this.tier.color()))
				.append(Component.literal(" types of items."))
			);

			BagDataComponent bagData = stack.get(DaintyDataComponents.BAG_DATA_COMPONENT);

			if (bagData.items().length == 0) {

			} else {
				tooltipComponents.add(Component.literal(""));
			}

			for (ItemStack storedItem : bagData.items()) {
				tooltipComponents.add(Component.literal(storedItem.getCount() + "x ").append(((MutableComponent) storedItem.getDisplayName()).withStyle(ChatFormatting.AQUA)));
			}

			tooltipComponents.add(Component.literal(""));
			tooltipComponents.add(
				Component.literal(Integer.toString(bagData.capacityRemaining())).withColor(this.tier.color())
				.append(Component.literal(" items remaining"))
			);

		} else {
			tooltipComponents.add(Component.literal("Hold SHIFT for more information...").setStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.DARK_GRAY)));
		}
	}

	public static enum Tier {
		BASIC,
		IRON,
		GOLD,
		DIAMOND,
		NETHERITE;

		public int capacity() {
			return 64 * (int) Math.pow(2, this.ordinal() + 3);
		}

		public int maxItemVariants() {
			return this == BASIC ? 1 : this.ordinal();
		}

		public int color() {
			return switch(this) {
				case BASIC -> 0xBB8877;
				case IRON -> 0xDDDDDD;
				case GOLD -> 0xFFFF99;
				case DIAMOND -> 0x99FFFF;
				case NETHERITE -> 0x333344;
			};
		}
	}
}
