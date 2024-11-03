package violet.dainty.features.blocktooltips.util;

import violet.dainty.features.blocktooltips.api.ui.IDisplayHelper;

public class FluidTextHelper {

	public static String getUnicodeMillibuckets(long amount, boolean simplify) {
		return IDisplayHelper.get().humanReadableNumber(amount, "B", true);
	}

}
