package violet.dainty.features.blocktooltips.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.math.IntMath;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import violet.dainty.features.blocktooltips.addon.universal.ItemIterator;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.fluid.JadeFluidObject;
import violet.dainty.features.blocktooltips.api.view.FluidView;
import violet.dainty.features.blocktooltips.api.view.ViewGroup;

public class JadeForgeUtils {

	private JadeForgeUtils() {
	}

	public static ItemIterator<? extends IItemHandler> fromItemHandler(IItemHandler storage, int fromIndex) {
		return fromItemHandler(storage, fromIndex, CommonProxy::findItemHandler);
	}

	public static ItemIterator<? extends IItemHandler> fromItemHandler(
			IItemHandler storage,
			int fromIndex,
			Function<Accessor<?>, @Nullable IItemHandler> containerFinder) {
		return new ItemIterator.SlottedItemIterator<>(containerFinder, fromIndex) {
			@Override
			protected int getSlotCount(IItemHandler container) {
				return container.getSlots();
			}

			@Override
			protected ItemStack getItemInSlot(IItemHandler container, int slot) {
				return container.getStackInSlot(slot);
			}
		};
	}

	public static JadeFluidObject fromFluidStack(FluidStack fluidStack) {
		return JadeFluidObject.of(fluidStack.getFluid(), fluidStack.getAmount(), fluidStack.getComponentsPatch());
	}

	public static List<ViewGroup<CompoundTag>> fromFluidHandler(IFluidHandler fluidHandler) {
		List<CompoundTag> list = new ArrayList<>(fluidHandler.getTanks());
		int emptyCapacity = 0;
		for (int i = 0; i < fluidHandler.getTanks(); i++) {
			int capacity = fluidHandler.getTankCapacity(i);
			if (capacity <= 0) {
				continue;
			}
			FluidStack fluidStack = fluidHandler.getFluidInTank(i);
			if (fluidStack.isEmpty()) {
				emptyCapacity = IntMath.saturatedAdd(emptyCapacity, capacity);
				continue;
			}
			list.add(FluidView.writeDefault(fromFluidStack(fluidStack), capacity));
		}
		if (list.isEmpty() && emptyCapacity > 0) {
			list.add(FluidView.writeDefault(JadeFluidObject.empty(), emptyCapacity));
		}
		if (!list.isEmpty()) {
			return List.of(new ViewGroup<>(list));
		}
		return List.of();
	}
}
