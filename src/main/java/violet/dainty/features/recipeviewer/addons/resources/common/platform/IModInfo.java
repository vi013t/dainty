package violet.dainty.features.recipeviewer.addons.resources.common.platform;

import net.minecraft.server.packs.PackResources;

import java.util.List;

public interface IModInfo {
    String getName();

    List<? extends PackResources> getPackResources();
}
