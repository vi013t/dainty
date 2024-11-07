package violet.dainty.features.recipeviewer.core.neoforge.config;

import violet.dainty.features.recipeviewer.core.common.config.IServerConfig;

public final class ServerConfig implements IServerConfig {

	private final boolean enableCheatModeForOp = true;
	private final boolean enableCheatModeForCreative = true;
	private final boolean enableCheatModeForGive = true;

	@Override
	public boolean isCheatModeEnabledForOp() {
		return enableCheatModeForOp;
	}

	@Override
	public boolean isCheatModeEnabledForGive() {
		return enableCheatModeForGive;
	}

	@Override
	public boolean isCheatModeEnabledForCreative() {
		return enableCheatModeForCreative;
	}

}
