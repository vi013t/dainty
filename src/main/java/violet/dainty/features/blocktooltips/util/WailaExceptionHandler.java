package violet.dainty.features.blocktooltips.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import violet.dainty.Dainty;
import violet.dainty.features.blocktooltips.api.IJadeProvider;
import violet.dainty.features.blocktooltips.api.TraceableException;

public class WailaExceptionHandler {

	private static final Set<IJadeProvider> ERRORS = Sets.newConcurrentHashSet();
	private static boolean NULL_ERROR = false;
	private static final File ERROR_OUTPUT = new File("logs", "JadeErrorOutput.txt");

	public static void handleErr(Throwable e, @Nullable IJadeProvider provider, @Nullable Consumer<Component> tooltip) {
		if (CommonProxy.isDevEnv()) {
			ExceptionUtils.wrapAndThrow(e);
			return;
		}
		if (provider == null) {
			if (!NULL_ERROR) {
				NULL_ERROR = true;
				writeLog(e, null);
			}
		} else if (!ERRORS.contains(provider)) {
			ERRORS.add(provider);
			writeLog(e, provider);
		}
		if (tooltip != null) {
			String modid = null;
			if (e instanceof TraceableException traceableException) {
				modid = traceableException.getNamespace();
			} else if (provider != null) {
				modid = provider.getUid().getNamespace();
			}
			if (modid == null || ResourceLocation.DEFAULT_NAMESPACE.equals(modid)) {
				modid = Dainty.MODID;
			}
			tooltip.accept(Component.translatable("dainty.error", ModIdentification.getModName(modid).orElse(modid))
					.withStyle(ChatFormatting.DARK_RED));
		}
	}

	private static void writeLog(Throwable e, IJadeProvider provider) {
		Dainty.LOGGER.error("Caught unhandled exception : [{}] {}", provider, e);
		Dainty.LOGGER.error("See JadeErrorOutput.txt for more information");
		Dainty.LOGGER.error("", e);
		try {
			FileUtils.writeStringToFile(
					ERROR_OUTPUT,
					DateFormat.getDateTimeInstance().format(new Date()) + "\n" + provider + "\n" + ExceptionUtils.getStackTrace(e) + "\n",
					StandardCharsets.UTF_8,
					true);
		} catch (Exception ignored) {
		}
	}
}
