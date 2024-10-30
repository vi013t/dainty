package violet.dainty.data;

import com.mojang.serialization.Codec;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import violet.dainty.Dainty;

public class DaintyDataAttachments {

	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Dainty.MODID);	

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> LINEAR_EXPERIENCE_ATTACHMENT_TYPE = ATTACHMENT_TYPES.register(
		"linear_experience", () -> AttachmentType.builder(() -> 0f).serialize(Codec.FLOAT).build()
	);

	public static void register(IEventBus eventBus) {
		ATTACHMENT_TYPES.register(eventBus);
	}
}
