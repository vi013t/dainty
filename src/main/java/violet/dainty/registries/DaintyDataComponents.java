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
import violet.dainty.features.wardenheart.CanSummonWardenDataComponent;

/**
 * Class for creating and registering custom data components added by the Dainty mod. Data components are a vanilla system
 * for attaching data to item stacks. Data components allow for creating a class, attaching an instance of that class to an
 * item stack, and then later reading that data from an item stack. For example, {@link violet.dainty.registries.DaintyItems#BAG bags}
 * need to keep track of their items, and they do so by having a {@link #BAG_DATA_COMPONENT} attached to the item stack.
 * 
 * <br/><br/>
 * 
 * Data components are not to be confused with {@link violet.dainty.registries.DaintyDataAttachments data attachments}, which are similar, but
 * store data on blocks, entities, and chunks, instead of item stacks.
 * 
 * <br/><br/>
 * 
 * For more information, see <a href="https://docs.neoforged.net/docs/items/datacomponents/">the relevant part of the Neoforge documentation</a>.
 */
public class DaintyDataComponents {

	/**
	 * The registry of custom data components added by the Dainty mod. All custom data components are registered here,
	 * and this registry itself is registered with the game when {@link #register(IEventBus)} is called during mod initialization.
	 */
	private static final DeferredRegister.DataComponents DAINTY_DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Dainty.MODID);

	private static final Codec<BagDataComponent> BAG_DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("capacity").forGetter(BagDataComponent::capacity),
		Codec.list(ItemStack.CODEC).fieldOf("items").forGetter(BagDataComponent::itemList)
	).apply(instance, BagDataComponent::fromItemList));

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<BagDataComponent>> BAG_DATA_COMPONENT = DAINTY_DATA_COMPONENTS.registerComponentType(
		"bag",
		builder -> builder.persistent(BAG_DATA_CODEC)
	);	

	/**
	 * The codec for serializing and synchronizing the {@link #CAN_SUMMON_WARDEN_DATA_COMPONENT}.
	 */
	private static final Codec<CanSummonWardenDataComponent> CAN_SUMMON_WARDEN_DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.BOOL.fieldOf("canSummonWarden").forGetter(CanSummonWardenDataComponent::canSummonWarden)
	).apply(instance, CanSummonWardenDataComponent::new));

	/**
	 * The "can summon warden" data component. This data component is attached to item stacks of 
	 * {@link net.minecraft.world.level.block.Blocks#SCULK_SHRIEKER sculk shriekers} that have been crafted to indicate 
	 * that they can spawn wardens (sculk shriekers mined with silk touch cannot spawn wardens). When the block is placed,
	 * the item stack is checked for this data component, and if it's present and allows warden spawning, the placed block
	 * gets the {@link net.minecraft.world.level.block.state.properties.BlockStateProperties#CAN_SUMMON "can summon"} block
	 * property applied, which allows it to spawn wardens. This logic is handled in 
	 * {@link violet.dainty.features.wardenheart.WardenHeartEventListener#placeSculkShrieker(net.neoforged.neoforge.event.level.BlockEvent.EntityPlaceEvent)
	 * the corresponding part of the warden heart event handler}.
	 */
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CanSummonWardenDataComponent>> CAN_SUMMON_WARDEN_DATA_COMPONENT = DAINTY_DATA_COMPONENTS.registerComponentType(
		"can_summon_warden",
		builder -> builder.persistent(CAN_SUMMON_WARDEN_DATA_CODEC)
	);	

	/**
	 * Registers the mod's data components (see {@link #DAINTY_DATA_COMPONENTS}) on the Neoforge event bus. This should be called exactly
	 * once during {@link violet.dainty.Dainty#Dainty(IEventBus, net.neoforged.fml.ModContainer) the main class's constructor} at
	 * the time of mod initialization.
	 * 
	 * @param eventBus The event bus supplied by Neoforge.
	 */
	public static void register(IEventBus eventBus) {
		DAINTY_DATA_COMPONENTS.register(eventBus);
	}
}
