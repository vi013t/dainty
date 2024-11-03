package violet.dainty.features.blocktooltips.impl;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import violet.dainty.features.blocktooltips.api.Accessor;
import violet.dainty.features.blocktooltips.api.AccessorClientHandler;
import violet.dainty.features.blocktooltips.api.BlockAccessor;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.overlay.WailaTickHandler;

public final class ObjectDataCenter {

	public static int rateLimiter = 250;
	public static long timeLastUpdate = System.currentTimeMillis();
	public static boolean serverConnected;
	private static Accessor<?> accessor;
	private static AccessorClientHandler<Accessor<?>> clientHandler;
	private static CompoundTag serverData;
	private static Object lastObject;

	private ObjectDataCenter() {
	}

	public static void set(@Nullable Accessor<?> accessor) {
		ObjectDataCenter.accessor = accessor;
		if (accessor == null) {
			WailaTickHandler.instance().progressTracker.clear();
			lastObject = null;
			clientHandler = null;
			return;
		}

		clientHandler = WailaClientRegistration.instance().getAccessorHandler(accessor.getAccessorType());
		Object object = accessor.getTarget();
		if (object == null && accessor instanceof BlockAccessor blockAccessor) {
			object = blockAccessor.getBlock();
		}

		if (!Objects.equals(object, lastObject)) {
			WailaTickHandler.instance().progressTracker.clear();
			lastObject = object;
			serverData = null;
			requestServerData();
		}
	}

	@Nullable
	public static Accessor<?> get() {
		return accessor;
	}

	public static CompoundTag getServerData() {
		if (accessor == null || clientHandler == null || serverData == null) {
			return null;
		}
		if (accessor.verifyData(serverData)) {
			return serverData;
		}
		requestServerData();
		return null;
	}

	public static void setServerData(CompoundTag tag) {
		serverData = tag;
		if (accessor != null && accessor.verifyData(serverData)) {
			accessor.getServerData().getAllKeys().clear();
			accessor.getServerData().merge(tag);
		}
	}

	public static void requestServerData() {
		timeLastUpdate = System.currentTimeMillis() - rateLimiter;
	}

	public static boolean isTimeElapsed(long time) {
		return System.currentTimeMillis() - timeLastUpdate >= time;
	}

	public static void resetTimer() {
		timeLastUpdate = System.currentTimeMillis();
	}

	public static IElement getIcon() {
		if (accessor == null || clientHandler == null) {
			return null;
		}
		return clientHandler.getIcon(accessor);
	}
}
