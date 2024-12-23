package violet.dainty.features.playerspecificloot.neoforge.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import violet.dainty.Dainty;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.common.entity.EntityTicker;
import violet.dainty.features.playerspecificloot.common.entity.LootrChestMinecartEntity;

@EventBusSubscriber(modid = Dainty.MODID)
public class HandleCart {
	@SuppressWarnings("null")
	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent event) {
		if (!(event.getLevel() instanceof ServerLevel level)) {
			return;
		}
		if (LootrAPI.isDimensionBlocked(level.dimension()) || LootrAPI.isDisabled()) {
			return;
		}
		if (event.getEntity().getType() == EntityType.CHEST_MINECART && event.getEntity() instanceof MinecartChest chest) {
			if (!level.isClientSide() && chest.getLootTable() != null && LootrAPI.shouldConvertMineshafts() && !LootrAPI.isLootTableBlacklisted(chest.getLootTable())) {
				LootrChestMinecartEntity lootrCart = new LootrChestMinecartEntity(chest.level(), chest.getX(), chest.getY(), chest.getZ());
				lootrCart.setLootTable(chest.getLootTable(), chest.getLootTableSeed());
				lootrCart.getPersistentData().merge(chest.getPersistentData());
				event.setCanceled(true);
				if (!level.getServer().isSameThread()) {
					Dainty.LOGGER.error("Minecart with Loot table created off main thread. Falling back on EntityTicker.");
					EntityTicker.addEntity(lootrCart);
				} else {
					level.addFreshEntity(lootrCart);
				}
			}
		} 
	}
}
