package violet.dainty.features.blocktooltips.impl.ui;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import violet.dainty.features.blocktooltips.api.ITooltip;
import violet.dainty.features.blocktooltips.api.JadeIds;
import violet.dainty.features.blocktooltips.api.fluid.JadeFluidObject;
import violet.dainty.features.blocktooltips.api.ui.BoxStyle;
import violet.dainty.features.blocktooltips.api.ui.IBoxElement;
import violet.dainty.features.blocktooltips.api.ui.IElement;
import violet.dainty.features.blocktooltips.api.ui.IElementHelper;
import violet.dainty.features.blocktooltips.api.ui.ITextElement;
import violet.dainty.features.blocktooltips.api.ui.ProgressStyle;
import violet.dainty.features.blocktooltips.impl.Tooltip;

public class ElementHelper implements IElementHelper {
	public static final ElementHelper INSTANCE = new ElementHelper();
	public static final ResourceLocation DEFAULT_PROGRESS = JadeIds.JADE("progress");
	public static final ResourceLocation DEFAULT_PROGRESS_BASE = JadeIds.JADE("progress_base");
	public static final Vec2 SMALL_ITEM_SIZE = new Vec2(10, 9);
	public static final Vec2 SMALL_ITEM_OFFSET = new Vec2(0, -1); //Vec2.NEG_UNIT_Y nullified by Saturn mod
	private ResourceLocation uid;

	@Override
	public ITextElement text(Component component) {
		return new TextElement(component);
	}

	@Override
	public IElement item(ItemStack stack) {
		return ItemStackElement.of(stack);
	}

	@Override
	public IElement item(ItemStack stack, float scale) {
		return ItemStackElement.of(stack, scale);
	}

	@Override
	public IElement item(ItemStack stack, float scale, String text) {
		return ItemStackElement.of(stack, scale, text);
	}

	@Override
	public IElement smallItem(ItemStack stack) {
		return item(stack, 0.5F, "").size(SMALL_ITEM_SIZE).translate(SMALL_ITEM_OFFSET).message(null);
	}

	@Override
	public IElement fluid(JadeFluidObject fluid) {
		return new FluidStackElement(fluid);
	}

	@Override
	public IElement spacer(int width, int height) {
		return new SpacerElement(new Vec2(width, height));
	}

	@Override
	public IElement progress(float progress, @Nullable Component text, ProgressStyle style, BoxStyle boxStyle, boolean canDecrease) {
		Objects.requireNonNull(style);
		Objects.requireNonNull(boxStyle);
		return new ProgressElement(progress, text, style, boxStyle, canDecrease);
	}

	@Override
	public IElement progress(float progress) {
		return progress(progress, DEFAULT_PROGRESS_BASE, DEFAULT_PROGRESS, 22, 16, false);
	}

	@Override
	public IElement progress(
			float progress,
			ResourceLocation baseSprite,
			ResourceLocation progressSprite,
			int width,
			int height,
			boolean canDecrease) {
		ProgressStyle style = progressStyle().fitContentX(false).overlay(sprite(progressSprite, width, height));
		BoxStyle boxStyle = BoxStyle.getSprite(baseSprite, null);
		return progress(progress, null, style, boxStyle, canDecrease).size(new Vec2(width, height));
	}

	@Override
	public IBoxElement box(ITooltip tooltip, BoxStyle boxStyle) {
		return new BoxElement((Tooltip) tooltip, boxStyle);
	}

	@Override
	public ITooltip tooltip() {
		return new Tooltip();
	}

	@Override
	public ProgressStyle progressStyle() {
		return new SimpleProgressStyle();
	}

	@Override
	public IElement sprite(ResourceLocation sprite, int width, int height) {
		return new SpriteElement(sprite, width, height);
	}

	@Nullable
	public ResourceLocation currentUid() {
		return uid;
	}

	public void setCurrentUid(ResourceLocation uid) {
		this.uid = uid;
	}

	//    public static IElement sub(String text) {
	//        CompoundTag tag = new CompoundTag();
	//        tag.putString("text", text);
	//        return new RenderableTextComponent(SUB, tag);
	//    }

}
