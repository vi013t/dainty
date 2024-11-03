package violet.dainty.features.gravestone.integration.waila;

import violet.dainty.features.blocktooltips.api.IWailaClientRegistration;
import violet.dainty.features.blocktooltips.api.IWailaCommonRegistration;
import violet.dainty.features.blocktooltips.api.IWailaPlugin;
import violet.dainty.features.blocktooltips.api.WailaPlugin;
import violet.dainty.features.gravestone.blocks.GraveStoneBlock;
import violet.dainty.features.gravestone.tileentity.GraveStoneTileEntity;

@WailaPlugin
public class PluginGraveStone implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(HUDHandlerGraveStone.INSTANCE, GraveStoneBlock.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(HUDHandlerGraveStone.INSTANCE, GraveStoneTileEntity.class);
    }
}