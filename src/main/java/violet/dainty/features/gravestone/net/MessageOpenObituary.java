package violet.dainty.features.gravestone.net;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import violet.dainty.Dainty;
import violet.dainty.features.gravestone.corelib.death.Death;
import violet.dainty.features.gravestone.corelib.net.Message;
import violet.dainty.features.gravestone.gui.ObituaryScreen;

public class MessageOpenObituary implements Message<MessageOpenObituary> { 

    public static final CustomPacketPayload.Type<MessageOpenObituary> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "open_obituary"));

    private Death death;

    public MessageOpenObituary() {

    }

    public MessageOpenObituary(Death death) {
        this.death = death;
    }

    @Override
    public PacketFlow getExecutingSide() {
        return PacketFlow.CLIENTBOUND;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void executeClientSide(IPayloadContext context) {
        Minecraft.getInstance().setScreen(new ObituaryScreen(death));
    }

    @Override
    public MessageOpenObituary fromBytes(RegistryFriendlyByteBuf buf) {
        death = Death.fromNBT(buf.registryAccess(), buf.readNbt());
        return this;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeNbt(death.toNBT(buf.registryAccess()));
    }

    @Override
    public Type<MessageOpenObituary> type() {
        return TYPE;
    }

}
