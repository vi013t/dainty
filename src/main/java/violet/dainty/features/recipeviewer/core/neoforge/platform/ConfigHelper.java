package violet.dainty.features.recipeviewer.core.neoforge.platform;

import java.nio.file.Path;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforgespi.language.IModInfo;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformConfigHelper;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.constants.ModIds;

public class ConfigHelper implements IPlatformConfigHelper {

	@Override
	public Path getModConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}

	@Override
	public Optional<Screen> getConfigScreen() {
		Minecraft minecraft = Minecraft.getInstance();
		ErrorUtil.checkNotNull(minecraft.screen, "minecraft.screen");
		return ModList.get()
			.getModContainerById(ModIds.JEI_ID)
			.flatMap(m -> {
				IModInfo modInfo = m.getModInfo();
				return IConfigScreenFactory.getForMod(modInfo)
					.map(f -> f.createScreen(m, minecraft.screen));
			});
	}
}
