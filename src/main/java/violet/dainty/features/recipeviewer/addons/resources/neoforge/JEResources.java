package violet.dainty.features.recipeviewer.addons.resources.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import violet.dainty.features.recipeviewer.addons.resources.common.compatibility.api.JERAPI;
import violet.dainty.features.recipeviewer.addons.resources.common.profiling.ProfileCommand;
import violet.dainty.features.recipeviewer.addons.resources.common.proxy.ClientProxy;
import violet.dainty.features.recipeviewer.addons.resources.common.proxy.CommonProxy;

public class JEResources {
    public static CommonProxy PROXY;

    public JEResources(ModContainer container, Dist dist) {
        PROXY = dist.isClient() ? new ClientProxy() : new CommonProxy();
        container.getEventBus().addListener(this::commonSetup);
        NeoForge.EVENT_BUS.addListener(this::onCommandsRegister);
	}

    private void commonSetup(FMLCommonSetupEvent event) {
        JERAPI.init();
    }

    private void onCommandsRegister(RegisterCommandsEvent event) {
        ProfileCommand.register(event.getDispatcher());
    }
}
