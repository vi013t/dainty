package violet.dainty.features.gravestone.corelib.inventory;

import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemListInventory implements Container {

    protected NonNullList<ItemStack> items;
    private Runnable onMarkDirty;
    private Function<Player, Boolean> onIsUsableByPlayer;

    public ItemListInventory(NonNullList<ItemStack> items, Runnable onMarkDirty, Function<Player, Boolean> onIsUsableByPlayer) {
        this.items = items;
        this.onMarkDirty = onMarkDirty;
        this.onIsUsableByPlayer = onIsUsableByPlayer;
    }

    public ItemListInventory(NonNullList<ItemStack> items, Runnable onMarkDirty) {
        this(items, onMarkDirty, null);
    }

    public ItemListInventory(NonNullList<ItemStack> items) {
        this(items, null);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack itemStack = ContainerHelper.removeItem(items, index, count);
        if (!itemStack.isEmpty()) {
            setChanged();
        }
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, @Nonnull ItemStack stack) {
        items.set(index, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public void setChanged() {
        if (onMarkDirty != null) {
            onMarkDirty.run();
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        if (onIsUsableByPlayer != null) {
            return onIsUsableByPlayer.apply(player);
        } else {
            return true;
        }
    }

    @Override
    public void clearContent() {
        items.clear();
    }

}
