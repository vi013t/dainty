package violet.dainty.features.recipeviewer.core.common.platform;

public interface IPlatformModHelper {
	String getModNameForModId(String modId);

	boolean isInDev();
}
