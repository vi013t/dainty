package violet.dainty.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import violet.dainty.Dainty;
import violet.dainty.features.bag.BagDataComponent;

public class DaintyDataComponents {

	private static final DeferredRegister.DataComponents DAINTY_DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Dainty.MODID);

	private static final Codec<BagDataComponent> BAG_DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("capacity").forGetter(BagDataComponent::capacity),
		Codec.list(ItemStack.CODEC).fieldOf("items").forGetter(BagDataComponent::itemList)
	).apply(instance, BagDataComponent::fromItemList));

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<BagDataComponent>> BAG_DATA_COMPONENT = DAINTY_DATA_COMPONENTS.registerComponentType(
		"basic",
		builder -> builder.persistent(BAG_DATA_CODEC)
	);	

	public static void register(IEventBus eventBus) {
		DAINTY_DATA_COMPONENTS.register(eventBus);
	}
}
