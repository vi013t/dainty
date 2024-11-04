package violet.dainty.features.gravestone.corelib.inventory;

import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TileEntityContainerProvider implements MenuProvider {

    private ContainerCreator container;
    private BlockEntity tileEntity;

    public TileEntityContainerProvider(ContainerCreator container, BlockEntity tileEntity) {
        this.container = container;
        this.tileEntity = tileEntity;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(tileEntity.getBlockState().getBlock().getDescriptionId());
    }

    public static void openGui(Player player, BlockEntity tileEntity, ContainerCreator containerCreator) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new TileEntityContainerProvider(containerCreator, tileEntity), packetBuffer -> packetBuffer.writeBlockPos(tileEntity.getBlockPos()));
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int i, @Nonnull Inventory playerInventory, @Nonnull Player playerEntity) {
        return container.create(i, playerInventory, playerEntity);
    }

    public interface ContainerCreator {
        AbstractContainerMenu create(int i, Inventory playerInventory, Player playerEntity);
    }
}
