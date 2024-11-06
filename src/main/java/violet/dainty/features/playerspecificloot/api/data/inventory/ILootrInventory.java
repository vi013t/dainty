package violet.dainty.features.playerspecificloot.api.data.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.playerspecificloot.api.MenuBuilder;
import violet.dainty.features.playerspecificloot.api.data.ILootrInfo;
import violet.dainty.features.playerspecificloot.api.data.ILootrSavedData;

public interface ILootrInventory extends Container, MenuProvider {
  ILootrInfo getInfo();

  void setInfo(ILootrSavedData info);

  @Override
  default Component getDisplayName() {
    return getInfo().getInfoDisplayName();
  }

  NonNullList<ItemStack> getInventoryContents();

  void setMenuBuilder(MenuBuilder builder);

  CompoundTag saveToTag(HolderLookup.Provider provider);
}
