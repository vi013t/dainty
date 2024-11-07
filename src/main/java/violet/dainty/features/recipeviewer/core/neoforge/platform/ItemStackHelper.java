package violet.dainty.features.recipeviewer.core.neoforge.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformItemStackHelper;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;

public class ItemStackHelper implements IPlatformItemStackHelper {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public int getBurnTime(ItemStack itemStack) {
		try {
			return itemStack.getBurnTime(null);
		} catch (RuntimeException | LinkageError e) {
			String itemStackInfo = ErrorUtil.getItemStackInfo(itemStack);
			LOGGER.error("Failed to check if item is fuel {}.", itemStackInfo, e);
			return 0;
		}
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		Item item = stack.getItem();
		return item.isBookEnchantable(stack, book);
	}

	@Override
	public Optional<String> getCreatorModId(ItemStack stack) {
		Item item = stack.getItem();
		String creatorModId = item.getCreatorModId(stack);
		return Optional.ofNullable(creatorModId);
	}

	@Override
	public List<Component> getTestTooltip(@Nullable Player player, ItemStack itemStack) {
		try {
			List<Component> tooltip = new ArrayList<>();
			tooltip.add(Component.literal("JEI Tooltip Testing for mod name formatting"));
			ItemTooltipEvent tooltipEvent = EventHooks.onItemTooltip(itemStack, player, tooltip, TooltipFlag.Default.NORMAL, Item.TooltipContext.EMPTY);
			return tooltipEvent.getToolTip();
		} catch (LinkageError | RuntimeException e) {
			LOGGER.error("Error while Testing for mod name formatting", e);
		}
		return List.of();
	}
}
