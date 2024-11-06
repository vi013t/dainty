package violet.dainty.features.wardenheart;

import com.mojang.datafixers.util.Either;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import violet.dainty.Dainty;
import violet.dainty.config.DaintyConfig;
import violet.dainty.registries.DaintyDataComponents;
import violet.dainty.registries.DaintyItems;

@EventBusSubscriber(modid = Dainty.MODID)
public class WardenHeartEventListener {

	@SubscribeEvent
	public static void dropWardenHeart(LivingDropsEvent event) {
		if (event.getEntity() instanceof Warden && Math.random() < DaintyConfig.WARDEN_HEART_DROP_CHANCE.get()) {
			event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().position().x, event.getEntity().position().y, event.getEntity().position().z, new ItemStack(DaintyItems.WARDEN_HEART.get())));
		}
	}

	@SubscribeEvent
	@SuppressWarnings({ "null", "resource" })
	public static void placeSculkShrieker(BlockEvent.EntityPlaceEvent event) {
		if (event.getEntity() instanceof Player player) {
			ItemStack blockStack = player.getMainHandItem();
			if (blockStack.getOrDefault(DaintyDataComponents.CAN_SUMMON_WARDEN_DATA_COMPONENT, new CanSummonWardenDataComponent(false)).canSummonWarden()) {
				event.getPlacedBlock().setValue(BlockStateProperties.CAN_SUMMON, true);
				Minecraft.getInstance().level.getBlockState(event.getPos()).setValue(BlockStateProperties.CAN_SUMMON, true);
			}
		}
	}

	@SubscribeEvent
	public static void addSculkShriekerTooltip(RenderTooltipEvent.GatherComponents event) {
		if (event.getItemStack().getOrDefault(DaintyDataComponents.CAN_SUMMON_WARDEN_DATA_COMPONENT, new CanSummonWardenDataComponent(false)).canSummonWarden()) {
			event.getTooltipElements().add(Either.left(FormattedText.of("Can summon wardens", Style.EMPTY.withColor(ChatFormatting.DARK_AQUA))));
		}
	}	

	@SubscribeEvent
	public static void craftSculkShrieker(PlayerEvent.ItemCraftedEvent event) {
		if (event.getCrafting().getItem() == Items.SCULK_SHRIEKER) {
			event.getCrafting().set(DaintyDataComponents.CAN_SUMMON_WARDEN_DATA_COMPONENT, new CanSummonWardenDataComponent(true));
		}
	}
}
