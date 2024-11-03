package violet.dainty.features.blocktooltips;

import violet.dainty.Dainty;
import violet.dainty.features.blocktooltips.api.IWailaPlugin;
import violet.dainty.features.blocktooltips.api.config.IWailaConfig;
import violet.dainty.features.blocktooltips.impl.WailaClientRegistration;
import violet.dainty.features.blocktooltips.impl.WailaCommonRegistration;
import violet.dainty.features.blocktooltips.impl.config.PluginConfig;
import violet.dainty.features.blocktooltips.impl.config.WailaConfig;
import violet.dainty.features.blocktooltips.impl.config.WailaConfig.ConfigGeneral;
import violet.dainty.features.blocktooltips.test.ExamplePlugin;
import violet.dainty.features.blocktooltips.util.CommonProxy;
import violet.dainty.features.blocktooltips.util.JsonConfig;

public class Jade {
	/**
	 * addons: Use {@link IWailaConfig#get()}
	 */
	public static final JsonConfig<WailaConfig> CONFIG = new JsonConfig<>(Dainty.MODID + "/" + Dainty.MODID, WailaConfig.CODEC, null);
	public static boolean FROZEN;

	public static void loadComplete() {
		if (FROZEN) {
			return;
		}
		FROZEN = true;
		if (CommonProxy.isDevEnv()) {
			try {
				IWailaPlugin plugin = new ExamplePlugin();
				plugin.register(WailaCommonRegistration.instance());
				if (CommonProxy.isPhysicallyClient()) {
					plugin.registerClient(WailaClientRegistration.instance());
				}
			} catch (Throwable ignored) {
			}
		}

		WailaCommonRegistration.instance().priorities.sort(PluginConfig.INSTANCE.getKeys());
		WailaCommonRegistration.instance().loadComplete();
		if (CommonProxy.isPhysicallyClient()) {
			WailaClientRegistration.instance().loadComplete();
			ConfigGeneral.init();
		}
		PluginConfig.INSTANCE.reload();
	}
}
