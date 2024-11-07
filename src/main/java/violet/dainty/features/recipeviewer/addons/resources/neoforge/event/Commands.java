package violet.dainty.features.recipeviewer.addons.resources.neoforge.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import violet.dainty.features.recipeviewer.addons.resources.common.profiling.ProfileCommand;

public class Commands {
    @SubscribeEvent
    public void registerCommand(RegisterCommandsEvent event) {
        new ProfileCommand().register(event.getDispatcher());
    }
}
