package violet.dainty.features.blocktooltips.track;

public abstract class TrackInfo {
	protected boolean alive = true;
	protected boolean updatedThisTick;

	public abstract void update(float pTicks);

	public abstract void tick();
}
