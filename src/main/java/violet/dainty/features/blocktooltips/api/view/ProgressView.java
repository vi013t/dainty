package violet.dainty.features.blocktooltips.api.view;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import violet.dainty.features.blocktooltips.api.ui.ProgressStyle;
import violet.dainty.features.blocktooltips.impl.ui.SlimProgressStyle;

public class ProgressView {

	public ProgressStyle style;
	public float progress;
	@Nullable
	public Component text;

	public ProgressView(ProgressStyle style) {
		this.style = style;
		Objects.requireNonNull(style);
	}

	public static ProgressView read(CompoundTag tag) {
		ProgressView progressView = new ProgressView(new SlimProgressStyle());
		progressView.progress = tag.getFloat("Progress");
		return progressView;
	}

	public static CompoundTag create(float progress) {
		CompoundTag tag = new CompoundTag();
		tag.putFloat("Progress", progress);
		return tag;
	}

}
