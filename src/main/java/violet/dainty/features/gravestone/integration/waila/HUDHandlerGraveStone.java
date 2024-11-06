package violet.dainty.features.gravestone.integration.waila;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.Dainty;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.IBlockComponentProvider;
import violet.dainty.features.blocktooltips.api.IServerDataProvider;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.config.IPluginConfig;
import violet.dainty.features.gravestone.GraveUtils;
import violet.dainty.features.gravestone.tileentity.GraveStoneTileEntity;

public class HUDHandlerGraveStone implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final HUDHandlerGraveStone INSTANCE = new HUDHandlerGraveStone();

    private static final ResourceLocation OBJECT_NAME_TAG = ResourceLocation.fromNamespaceAndPath("jade", "object_name");

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "grave");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof GraveStoneTileEntity grave) {
            iTooltip.remove(OBJECT_NAME_TAG);
            iTooltip.add(grave.getName().copy().withStyle(ChatFormatting.WHITE));
            Component time = GraveUtils.getDate(grave.getDeath().getTimestamp());
            if (time != null) {
                iTooltip.add(Component.translatable("message.dainty.date_of_death", time));
            }

            CompoundTag data = blockAccessor.getServerData();
            if (data.contains("ItemCount")) {
                iTooltip.add(Component.translatable("message.dainty.item_count", data.getInt("ItemCount")));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof GraveStoneTileEntity grave) {
            compoundTag.putInt("ItemCount", (int) grave.getDeath().getAllItems().stream().filter(itemStack -> !itemStack.isEmpty()).count());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}