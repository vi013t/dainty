package violet.dainty.features.gravestone.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import violet.dainty.Dainty;
import violet.dainty.features.gravestone.GraveUtils;
import violet.dainty.features.gravestone.Gravestone;
import violet.dainty.features.gravestone.GravestoneServerConfig;
import violet.dainty.features.gravestone.blocks.GraveStoneBlock;
import violet.dainty.features.gravestone.corelib.death.Death;
import violet.dainty.features.gravestone.corelib.death.PlayerDeathEvent;
import violet.dainty.features.gravestone.items.ObituaryItem;
import violet.dainty.features.gravestone.tileentity.GraveStoneTileEntity;

public class DeathEvents {

    public DeathEvents() {
        violet.dainty.features.gravestone.corelib.death.DeathEvents.register();
    }

    @SubscribeEvent
    public void playerDeath(PlayerDeathEvent event) {
        event.storeDeath();

        Death death = event.getDeath();
        Player player = event.getPlayer();
        Level world = player.level();

        if (keepInventory(player)) {
            return;
        } 

        BlockPos graveStoneLocation = GraveUtils.getGraveStoneLocation(world, death.getBlockPos());

        if (GravestoneServerConfig.GIVE_OBITUARIES) {  
            player.getInventory().add(Gravestone.OBITUARY.get().toStack(death));
        }

        if (graveStoneLocation == null) {
            Dainty.LOGGER.info("Grave of '{}' can't be placed (No space)", death.getPlayerName());
            Dainty.LOGGER.info("The death ID of '{}' is {}", death.getPlayerName(), death.getId().toString());
            return;
        }

        world.setBlockAndUpdate(graveStoneLocation, Gravestone.GRAVESTONE.get().defaultBlockState().setValue(GraveStoneBlock.FACING, player.getDirection().getOpposite()));

        if (GraveUtils.isReplaceable(world, graveStoneLocation.below())) {
            world.setBlockAndUpdate(graveStoneLocation.below(), Blocks.DIRT.defaultBlockState());
        }

        BlockEntity tileentity = world.getBlockEntity(graveStoneLocation);

        if (!(tileentity instanceof GraveStoneTileEntity gravestone)) {
            Dainty.LOGGER.info("Grave of '{}' can't be filled with loot (No tileentity found)", death.getPlayerName());
            Dainty.LOGGER.info("The death ID of '{}' is {}", death.getPlayerName(), death.getId().toString());
            return;
        }

        gravestone.setDeath(death);
        event.removeDrops();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerCloneLast(PlayerEvent.Clone event) {
        if (!GravestoneServerConfig.GIVE_OBITUARIES) {
            return;
        }

        if (!event.isWasDeath()) {
            return;
        }

        if (keepInventory(event.getEntity())) {
            return;
        }

        for (ItemStack stack : event.getOriginal().getInventory().items) {
            if (stack.getItem() instanceof ObituaryItem) {
                event.getEntity().getInventory().add(stack);
            }
        }
    }

    public static boolean keepInventory(Player player) {
        try {
            return player.getCommandSenderWorld().getLevelData().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
        } catch (Exception e) {
            return false;
        }
    }

}
