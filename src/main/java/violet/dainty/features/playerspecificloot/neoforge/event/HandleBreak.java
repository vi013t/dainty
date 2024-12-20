package violet.dainty.features.playerspecificloot.neoforge.event; 

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import violet.dainty.Dainty;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.LootrTags;

@EventBusSubscriber(modid = Dainty.MODID)
public class HandleBreak {

  @SubscribeEvent
  public static void onBlockBreak(BlockEvent.BreakEvent event) {
    Player player = event.getPlayer();

    if (!event.getLevel().isClientSide()) {
      if (event.getState().is(LootrTags.Blocks.CONTAINERS)) {
        if (LootrAPI.canDestroyOrBreak(player)) {
          return;
        }
        if (LootrAPI.isBreakDisabled()) {
          if (player.getAbilities().instabuild) {
            if (!player.isShiftKeyDown()) {
              event.setCanceled(true);
              player.displayClientMessage(Component.translatable("dainty.message.cannot_break_sneak").setStyle(LootrAPI.getChatStyle()), false);
            }
          } else {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("dainty.message.cannot_break").setStyle(LootrAPI.getChatStyle()), false);
          }
        } else {
          if (!event.getPlayer().isShiftKeyDown()) {
            event.setCanceled(true);
            event.getPlayer().displayClientMessage(Component.translatable("dainty.message.should_sneak").setStyle(LootrAPI.getChatStyle()), false);
            event.getPlayer().displayClientMessage(Component.translatable("dainty.message.should_sneak2", Component.translatable("dainty.message.should_sneak3").setStyle(Style.EMPTY.withBold(true))).setStyle(LootrAPI.getChatStyle()), false);
          }
        }
      }
    }
  }
}
