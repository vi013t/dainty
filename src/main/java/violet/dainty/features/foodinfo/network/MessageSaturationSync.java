package violet.dainty.features.foodinfo.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import violet.dainty.Dainty;

public record MessageSaturationSync(float saturationLevel) implements CustomPacketPayload
{
	public static final Type<MessageSaturationSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "saturation"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MessageSaturationSync> CODEC = StreamCodec.composite(
		ByteBufCodecs.FLOAT,
		MessageSaturationSync::saturationLevel,
		MessageSaturationSync::new
	);


	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	public static void handle(final MessageSaturationSync message, final IPayloadContext ctx)
	{
		ctx.enqueueWork(() -> {
			ctx.player().getFoodData().setSaturation(message.saturationLevel());
		});
	}
}