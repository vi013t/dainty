package violet.dainty.features.stonefurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.registries.DaintyBlockEntities;
import violet.dainty.registries.DaintyRecipes;

public class StoneFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    public StoneFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(DaintyBlockEntities.STONE_FURNACE.get(), pos, blockState, DaintyRecipes.STONE_FURNACE.get());
    }

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.stone_furnace");
	}
	
	@Override
    protected int getBurnDuration(ItemStack fuel) {
        return super.getBurnDuration(fuel) / 2;
    }

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return new StoneFurnaceMenu(id, player, this, this.dataAccess);
	}    
	
}
