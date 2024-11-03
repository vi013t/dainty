package violet.dainty.features.blocktooltips.api.view;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluids;
import violet.dainty.features.blocktooltips.api.fluid.JadeFluidObject;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;
import violet.dainty.features.blocktooltips.util.CommonProxy;
import violet.dainty.features.blocktooltips.util.FluidTextHelper;

public class FluidView {

	public static final Component EMPTY_FLUID = Component.translatable("dainty.fluid.empty");

	public IElement overlay;
	public String current;
	public String max;
	public float ratio;
	@Nullable
	public Component fluidName;
	@Nullable
	public Component overrideText;

	public FluidView(IElement overlay) {
		this.overlay = overlay;
		Objects.requireNonNull(overlay);
	}

	@Nullable
	public static FluidView readDefault(CompoundTag tag) {
		long capacity = tag.getLong("capacity");
		if (capacity <= 0) {
			return null;
		}
		JadeFluidObject fluidObject = JadeFluidObject.CODEC.parse(NbtOps.INSTANCE, tag.get("fluid")).result().orElse(null);
		if (fluidObject == null) {
			return null;
		}
		long amount = fluidObject.getAmount();
		FluidView fluidView = new FluidView(IElementHelper.get().fluid(fluidObject));
		fluidView.fluidName = CommonProxy.getFluidName(fluidObject);
		fluidView.current = FluidTextHelper.getUnicodeMillibuckets(amount, true);
		fluidView.max = FluidTextHelper.getUnicodeMillibuckets(capacity, true);
		fluidView.ratio = (float) ((double) amount / capacity);
		if (fluidObject.getType().isSame(Fluids.EMPTY)) {
			fluidView.overrideText = Component.translatable(
					"dainty.fluid",
					EMPTY_FLUID,
					Component.literal(fluidView.max).withStyle(ChatFormatting.GRAY));
		}
		return fluidView;
	}

	public static CompoundTag writeDefault(JadeFluidObject fluidObject, long capacity) {
		CompoundTag tag = new CompoundTag();
		if (capacity <= 0) {
			return tag;
		}
		tag.put("fluid", JadeFluidObject.CODEC.encodeStart(NbtOps.INSTANCE, fluidObject).result().orElseThrow());
		tag.putLong("capacity", capacity);
		return tag;
	}

}
