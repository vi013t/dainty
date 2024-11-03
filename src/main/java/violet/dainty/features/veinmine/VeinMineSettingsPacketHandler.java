package violet.dainty.features.veinmine;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import violet.dainty.registries.DaintyDataAttachments;

public class VeinMineSettingsPacketHandler implements IPayloadHandler<VeinMineSettings> {

	@Override
	public void handle(VeinMineSettings settings, IPayloadContext context) {
		context.player().setData(DaintyDataAttachments.VEIN_MINE_SETTINGS_ATTACHMENT_TYPE, settings);
	}
	
}
