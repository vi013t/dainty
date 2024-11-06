package violet.dainty.features.stonefurnace;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import violet.dainty.registries.DaintyMenus;
import violet.dainty.registries.DaintyRecipes;

public class StoneFurnaceMenu extends AbstractFurnaceMenu {

	public StoneFurnaceMenu(int containerId, Inventory playerInventory) {
        super(DaintyMenus.STONE_FURNACE.get(), DaintyRecipes.STONE_FURNACE.get(), RecipeBookType.BLAST_FURNACE, containerId, playerInventory);
    }

    public StoneFurnaceMenu(int containerId, Inventory playerInventory, Container blastFurnaceContainer, ContainerData blastFurnaceData) {
        super(DaintyMenus.STONE_FURNACE.get(), DaintyRecipes.STONE_FURNACE.get(), RecipeBookType.BLAST_FURNACE, containerId, playerInventory, blastFurnaceContainer, blastFurnaceData);
    }
}
