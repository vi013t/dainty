package violet.dainty.features.structurecompass.client;

public enum OverlaySide {

	LEFT, RIGHT;

	public static OverlaySide fromString(String str) {
		if (str.equals("RIGHT")) {
			return RIGHT;
		}
		return LEFT;
	}

}
