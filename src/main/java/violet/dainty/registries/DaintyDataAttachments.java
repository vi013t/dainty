package violet.dainty.registries;

import com.mojang.serialization.Codec;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import violet.dainty.Dainty;
import violet.dainty.features.inventorysorting.SortBy;
import violet.dainty.features.inventorysorting.SortPosition;
import violet.dainty.features.veinmine.VeinMineSettings;
import violet.dainty.features.zoom.ZoomData;

/**
 * Class for creating and registering custom data attachments added by the Dainty mod. Data attachments are a system added by Neoforge
 * for attaching data to blocks, entities, or chunks. Data attachments allow for creating a class, attaching an instance of that class to a
 * block, entity, or chunk, and then later reading that data from the thing it's attached to. For example, Dainty has a feature that linearizes
 * experience. To keep track of fractional experience, the {@link #LINEAR_EXPERIENCE_ATTACHMENT_TYPE} is stored on the player entity.
 * 
 * <br/><br/>
 * 
 * Data components are not to be confused with {@link violet.dainty.registries.DataComponents data components}, which are similar, but
 * store data on item stacks, instead of blocks, entities, and chunks.
 * 
 * <br/><br/>
 * 
 * For more information, see <a href="https://docs.neoforged.net/docs/datastorage/attachments/">the relevant part of the Neoforge documentation</a>.
 */
public class DaintyDataAttachments {

	/**
	 * The registry for custom data attachments. All custom data attachments are registered here, and this registry
	 * itself is registered with Neoforge when {@link #register(IEventBus)} is called during mod initialization.
	 */
	private static final DeferredRegister<AttachmentType<?>> DATA_ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Dainty.MODID);	

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> LINEAR_EXPERIENCE_ATTACHMENT_TYPE = DATA_ATTACHMENTS.register(
		"linear_experience", () -> AttachmentType.builder(() -> 0f).serialize(Codec.FLOAT).build()
	);

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<VeinMineSettings>> VEIN_MINE_SETTINGS_ATTACHMENT_TYPE = DATA_ATTACHMENTS.register(
		"vein_mine_settings", () -> AttachmentType.builder(() -> VeinMineSettings.DEFAULT).serialize(VeinMineSettings.CODEC).build()
	);

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<SortPosition>> SORT_INVENTORY_ATTACHMENT_TYPE = DATA_ATTACHMENTS.register(
		"sort_inventory", () -> AttachmentType.builder(SortPosition::error).serialize(SortPosition.CODEC).build()
	);

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<SortBy>> SORT_ORDER = DATA_ATTACHMENTS.register(
		"sort_order", () -> AttachmentType.builder(SortBy::defaultSorting).serialize(SortBy.CODEC).build()
	);

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<ZoomData>> ZOOM = DATA_ATTACHMENTS.register(
		"zoom", () -> AttachmentType.builder(ZoomData::none).serialize(ZoomData.CODEC).build()
	);

	/**
	 * Registers the mod's data attachments (see {@link #DATA_ATTACHMENTS}) on the Neoforge event bus. This should be called exactly
	 * once during {@link violet.dainty.Dainty#Dainty(IEventBus, net.neoforged.fml.ModContainer) the main class's constructor} at
	 * the time of mod initialization.
	 * 
	 * @param eventBus The event bus supplied by Neoforge.
	 */
	public static void register(IEventBus eventBus) {
		DATA_ATTACHMENTS.register(eventBus);
	}
}
