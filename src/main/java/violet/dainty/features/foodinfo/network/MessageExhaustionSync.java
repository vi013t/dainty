package violet.dainty.features.foodinfo.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import violet.dainty.Dainty;

public record MessageExhaustionSync(float exhaustionLevel) implements CustomPacketPayload
{
	public static final Type<MessageExhaustionSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "exhaustion"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MessageExhaustionSync> CODEC = StreamCodec.composite(
		ByteBufCodecs.FLOAT,
		MessageExhaustionSync::exhaustionLevel,
		MessageExhaustionSync::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	public static void handle(final MessageExhaustionSync message, final IPayloadContext ctx)
	{
		ctx.enqueueWork(() -> {
			ctx.player().getFoodData().setExhaustion(message.exhaustionLevel());
		});
	}
}