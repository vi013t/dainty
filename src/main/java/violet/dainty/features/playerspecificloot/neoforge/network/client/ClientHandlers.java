package violet.dainty.features.playerspecificloot.neoforge.network.client; 

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import violet.dainty.Dainty;
import violet.dainty.features.playerspecificloot.api.data.blockentity.ILootrBlockEntity;
import violet.dainty.features.playerspecificloot.api.data.entity.ILootrCart;


public class ClientHandlers {
  public static void handleCloseCart(int entityId) {
    Level level = Minecraft.getInstance().level;
    if (level == null) {
      Dainty.LOGGER.info("Unable to mark entity with id '" + entityId + "' as closed as level is null.");
      return;
    }
    Entity cart = level.getEntity(entityId);
    if (cart == null) {
      Dainty.LOGGER.info("Unable to mark entity with id '" + entityId + "' as closed as entity is null.");
      return;
    }

    if (!(cart instanceof ILootrCart lootrCart)) {
      Dainty.LOGGER.info("Unable to mark entity with id '" + entityId + "' as closed as entity is not a Lootr minecart.");
      return;
    }

    lootrCart.setClientOpened(false);
  }

  public static void handleOpenCart(int entityId) {
    Level level = Minecraft.getInstance().level;
    if (level == null) {
      Dainty.LOGGER.info("Unable to mark entity with id '" + entityId + "' as opened as level is null.");
      return;
    }
    Entity cart = level.getEntity(entityId);
    if (cart == null) {
      Dainty.LOGGER.info("Unable to mark entity with id '" + entityId + "' as opened as entity is null.");
      return;
    }

    if (!(cart instanceof ILootrCart lootrCart)) {
      Dainty.LOGGER.info("Unable to mark entity with id '" + entityId + "' as opened as entity is not a Lootr minecart.");
      return;
    }

    lootrCart.setClientOpened(true);
  }

  public static void handleOpenContainer(BlockPos pos) {
    if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof ILootrBlockEntity provider) {
      provider.setClientOpened(true);
    }
    refreshModel(pos);
  }

  public static void handleCloseContainer(BlockPos pos) {
    if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof ILootrBlockEntity provider) {
      provider.setClientOpened(false);
    }
    refreshModel(pos);
  }

  public static void refreshModel(BlockPos pos) {
    SectionPos sPos = SectionPos.of(pos);
    Minecraft.getInstance().levelRenderer.setSectionDirty(sPos.x(), sPos.y(), sPos.z());
  }

  @Nullable
  public static Player getPlayer() {
    return Minecraft.getInstance().player;
  }
}
