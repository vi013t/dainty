package violet.dainty.features.recipeviewer.addons.resources.neoforge;

import java.util.List;

import net.minecraft.server.packs.PackResources;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import net.neoforged.neoforgespi.language.IModFileInfo;
import violet.dainty.features.recipeviewer.addons.resources.common.platform.IModInfo;

public class ModInfo implements IModInfo {

    private IModFileInfo modFile;

    public ModInfo(IModFileInfo modFile) {
        this.modFile = modFile;
    }

    @Override
    public String getName() {
        return modFile.moduleName();
    }

    @Override
    public List<? extends PackResources> getPackResources() {
        // return List.of(ResourcePackLoader.createPackForMod(modFile).openPrimary(modFile.moduleName()));
        return List.of(ResourcePackLoader.createPackForMod(modFile).openPrimary(null)); // TODO Fix if needed
    }
}
