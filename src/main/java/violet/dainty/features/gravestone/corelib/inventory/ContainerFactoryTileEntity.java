package violet.dainty.features.gravestone.corelib.inventory;

import javax.annotation.Nonnull;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.IContainerFactory;

public class ContainerFactoryTileEntity<T extends AbstractContainerMenu, U extends BlockEntity> implements IContainerFactory<T> {

    private final ContainerCreator<T, U> containerCreator;

    public ContainerFactoryTileEntity(ContainerCreator<T, U> containerCreator) {
        this.containerCreator = containerCreator;
    }

	@Override
    @SuppressWarnings("unchecked")
    public T create(int windowId, @Nonnull Inventory inv, @Nonnull RegistryFriendlyByteBuf data) {
        BlockEntity te = inv.player.level().getBlockEntity(data.readBlockPos());
        try {
            return containerCreator.create(windowId, inv, (U) te);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public interface ContainerCreator<T extends AbstractContainerMenu, U extends BlockEntity> {
        T create(int windowId, Inventory inv, U tileEntity);
    }
}
