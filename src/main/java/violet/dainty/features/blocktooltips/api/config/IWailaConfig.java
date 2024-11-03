package violet.dainty.features.blocktooltips.api.config;

import org.jetbrains.annotations.ApiStatus.NonExtendable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import violet.dainty.features.blocktooltips.JadeInternals;
import violet.dainty.features.blocktooltips.api.SimpleStringRepresentable;
import violet.dainty.features.blocktooltips.api.theme.Theme;

//TODO(1.21.2): split accessibility options into a new class
@NonExtendable
public interface IWailaConfig {

	static IWailaConfig get() {
		return JadeInternals.getWailaConfig();
	}

	IConfigGeneral getGeneral();

	IConfigOverlay getOverlay();

	IConfigFormatting getFormatting();

	IPluginConfig getPlugin();

	enum IconMode implements SimpleStringRepresentable {
		TOP, CENTERED, INLINE, HIDE
	}

	enum TTSMode implements SimpleStringRepresentable {
		TOGGLE, PRESS
	}

	enum DisplayMode implements SimpleStringRepresentable {
		HOLD_KEY, TOGGLE, LITE
	}

	enum FluidMode implements SimpleStringRepresentable {
		NONE(ClipContext.Fluid.NONE),
		ANY(ClipContext.Fluid.ANY),
		SOURCE_ONLY(ClipContext.Fluid.SOURCE_ONLY),
		FALLBACK(ClipContext.Fluid.NONE);

		public final ClipContext.Fluid ctx;

		FluidMode(ClipContext.Fluid ctx) {
			this.ctx = ctx;
		}
	}

	enum BossBarOverlapMode implements SimpleStringRepresentable {
		NO_OPERATION, HIDE_BOSS_BAR, HIDE_TOOLTIP, PUSH_DOWN
	}

	enum PerspectiveMode implements SimpleStringRepresentable {
		CAMERA, EYE
	}

	@NonExtendable
	interface IConfigGeneral {

		void setDisplayTooltip(boolean displayTooltip);

		boolean getDisplayEntities();

		void setDisplayEntities(boolean displayEntities);

		boolean getDisplayBosses();

		void setDisplayBosses(boolean displayBosses);

		boolean getDisplayBlocks();

		void setDisplayBlocks(boolean displayBlocks);

		void setHideFromTabList(boolean hideFromTabList);

		void setHideFromGUIs(boolean hideFromGUIs);

		void toggleTTS();

		void setItemModNameTooltip(boolean itemModNameTooltip);

		boolean shouldDisplayTooltip();

		DisplayMode getDisplayMode();

		void setDisplayMode(DisplayMode displayMode);

		boolean shouldHideFromTabList();

		boolean shouldHideFromGUIs();

		boolean shouldEnableTextToSpeech();

		TTSMode getTTSMode();

		void setTTSMode(TTSMode ttsMode);

		boolean shouldDisplayFluids();

		FluidMode getDisplayFluids();

		void setDisplayFluids(boolean displayFluids);

		void setDisplayFluids(FluidMode displayFluids);

		boolean showItemModNameTooltip();

		float getExtendedReach();

		void setExtendedReach(float extendedReach);

		BossBarOverlapMode getBossBarOverlapMode();

		void setBossBarOverlapMode(BossBarOverlapMode mode);

		boolean isDebug();

		void setDebug(boolean debug);

		boolean getBuiltinCamouflage();

		void setBuiltinCamouflage(boolean builtinCamouflage);

		boolean getAccessibilityModMemory();

		void setAccessibilityModMemory(boolean lowVisionMemory);

		boolean getEnableAccessibilityPlugin();

		void setEnableAccessibilityPlugin(boolean showAccessibilityPlugins);

		PerspectiveMode getPerspectiveMode();

		void setPerspectiveMode(PerspectiveMode perspectiveMode);
	}

	@NonExtendable
	interface IConfigOverlay {

		static int applyAlpha(int color, float alpha) {
			if (alpha == 0) {
				return 0;
			}
			int prevAlphaChannel = (color >> 24) & 0xFF;
			if (prevAlphaChannel > 0) {
				alpha *= prevAlphaChannel / 256f;
			}
			int alphaChannel = Mth.clamp((int) (0xFF * alpha), 4, 255);
			return (color & 0xFFFFFF) | alphaChannel << 24;
		}

		float getOverlayPosX();

		void setOverlayPosX(float overlayPosX);

		float getOverlayPosY();

		void setOverlayPosY(float overlayPosY);

		float getOverlayScale();

		void setOverlayScale(float overlayScale);

		float getAnchorX();

		void setAnchorX(float overlayAnchorX);

		float getAnchorY();

		void setAnchorY(float overlayAnchorY);

		boolean getFlipMainHand();

		void setFlipMainHand(boolean overlaySquare);

		float tryFlip(float f);

		boolean getSquare();

		void setSquare(boolean overlaySquare);

		float getAutoScaleThreshold();

		float getAlpha();

		void setAlpha(float alpha);

		Theme getTheme();

		void applyTheme(ResourceLocation id);

		boolean shouldShowIcon();

		IconMode getIconMode();

		void setIconMode(IconMode iconMode);

		boolean getAnimation();

		void setAnimation(boolean animation);

		float getDisappearingDelay();

		void setDisappearingDelay(float delay);
	}

	@NonExtendable
	interface IConfigFormatting {

		Style getItemModNameStyle();

		void setItemModNameStyle(Style itemModNameStyle);

		Component registryName(String name);
	}
}
