package violet.dainty.features.gravestone.corelib.inventory;

import javax.annotation.Nonnull;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LockedSlot extends Slot {

    protected boolean inputLocked;
    protected boolean outputLocked;

    public LockedSlot(Container inventoryIn, int index, int xPosition, int yPosition, boolean inputLocked, boolean outputLocked) {
        super(inventoryIn, index, xPosition, yPosition);
        this.inputLocked = inputLocked;
        this.outputLocked = outputLocked;
    }

    public LockedSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        this(inventoryIn, index, xPosition, yPosition, false, true);
    }

    @Override
    public boolean mayPickup(@Nonnull Player playerIn) {
        if (outputLocked) {
            return false;
        } else {
            return super.mayPickup(playerIn);
        }
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        if (inputLocked) {
            return false;
        } else {
            return super.mayPlace(stack);
        }
    }

}