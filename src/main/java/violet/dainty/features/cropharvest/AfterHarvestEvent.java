package violet.dainty.features.cropharvest;

import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

public class AfterHarvestEvent extends Event {

	private final HarvestContext context;

	private AfterHarvestEvent(HarvestContext context) {
		this.context = context;
	}

	protected static void post(HarvestContext context) {
		AfterHarvestEvent event = new AfterHarvestEvent(context);
		NeoForge.EVENT_BUS.post(event);
	}

	public HarvestContext getContext() {
		return this.context;
	}
}
