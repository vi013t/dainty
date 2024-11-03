package violet.dainty.features.veinmine;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent.MouseScrollingEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.Dainty;
import violet.dainty.registries.DaintyDataAttachments;
import violet.dainty.registries.DaintyKeyBindings;

@EventBusSubscriber(modid = Dainty.MODID)
public class VeinMineEventHandler {

	@SubscribeEvent
	public static void veinMine(BlockEvent.BreakEvent event) {
		if (!DaintyKeyBindings.VEINMINE.get().isDown()) return;

		// Get vein-mine settings
		VeinMineSettings settings = event.getPlayer().getData(DaintyDataAttachments.VEIN_MINE_SETTINGS_ATTACHMENT_TYPE);
		ServerLevel level = (ServerLevel) event.getLevel();

		// Carve out the necessary blocks
		BlockPos[] blocksToCarve = settings.carve(level, event.getPos(), event.getPlayer().getDirection());
		List<ItemStack> drops = new ArrayList<>();
		for (BlockPos position : blocksToCarve) {
			BlockState blockToBreak = level.getBlockState(position);
			drops.addAll(blockToBreak.getDrops(new LootParams.Builder(level)
				.withParameter(LootContextParams.TOOL, event.getPlayer().getMainHandItem())
				.withParameter(LootContextParams.ORIGIN, position.getCenter())
			));
			event.getLevel().removeBlock(position, false);
		}

		// Drop items
		if (!event.getPlayer().isCreative()) {
			for (ItemStack drop : drops) {
				level.addFreshEntity(new ItemEntity(level, event.getPlayer().getX(), event.getPlayer().getY(), event.getPlayer().getZ(), drop));
			}
		}

		// Take food points
		event.getPlayer().causeFoodExhaustion(blocksToCarve.length * 20 * 0.005f);
	}

	@SubscribeEvent
	@SuppressWarnings({ "null", "resource" })
	public static void changeSettings(MouseScrollingEvent event) {
		if (Minecraft.getInstance().player == null || !DaintyKeyBindings.VEINMINE.get().isDown()) return;

 		VeinMineSettings settings = Minecraft.getInstance().player.getData(DaintyDataAttachments.VEIN_MINE_SETTINGS_ATTACHMENT_TYPE);
		VeinMineSettings newSettings = settings.changeShape((int) event.getScrollDeltaY());
		PacketDistributor.sendToServer(newSettings);

		VeinMineSettingsOverlay.shift((int) event.getScrollDeltaY());

		event.setCanceled(true);
	}
}
