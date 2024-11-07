package violet.dainty.features.recipeviewer.core.common.gui;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import violet.dainty.features.recipeviewer.core.common.Internal;
import violet.dainty.features.recipeviewer.core.common.config.DebugConfig;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformRenderHelper;
import violet.dainty.features.recipeviewer.core.common.platform.Services;
import violet.dainty.features.recipeviewer.core.common.util.ErrorUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IJeiHelpers;
import violet.dainty.features.recipeviewer.core.commonapi.helpers.IModIdHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientHelper;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientType;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.subtypes.UidContext;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IIngredientManager;
import violet.dainty.features.recipeviewer.core.commonapi.runtime.IJeiKeyMapping;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JeiTooltip implements ITooltipBuilder {
	private final List<Either<FormattedText, TooltipComponent>> lines = new ArrayList<>();
	private @Nullable ITypedIngredient<?> typedIngredient;

	@Override
	public void add(@Nullable FormattedText formattedText) {
		if (formattedText == null) {
			if (Services.PLATFORM.getModHelper().isInDev()) {
				throw new NullPointerException("Tried to add null tooltip text");
			}
			return;
		}
		lines.add(Either.left(formattedText));
	}

	@Override
	public void add(@Nullable TooltipComponent component) {
		if (component == null) {
			if (Services.PLATFORM.getModHelper().isInDev()) {
				throw new NullPointerException("Tried to add null tooltip component");
			}
			return;
		}
		lines.add(Either.right(component));
	}

	@Override
	public void setIngredient(ITypedIngredient<?> typedIngredient) {
		this.typedIngredient = typedIngredient;
	}

	public void addKeyUsageComponent(String translationKey, IJeiKeyMapping keyMapping) {
		MutableComponent translatedKeyMessage = keyMapping.getTranslatedKeyMessage().copy();
		addKeyUsageComponent(translationKey, translatedKeyMessage);
	}

	public void addKeyUsageComponent(String translationKey, MutableComponent keyMapping) {
		Component boldKeyMapping = keyMapping.withStyle(ChatFormatting.BOLD);
		MutableComponent component = Component.translatable(translationKey, boldKeyMapping)
			.withStyle(ChatFormatting.ITALIC)
			.withStyle(ChatFormatting.GRAY);

		add(component);
	}

	@Override
	public void addAll(Collection<? extends FormattedText> components) {
		for (FormattedText component : components) {
			add(component);
		}
	}

	@Override
	public void clear() {
		this.lines.clear();
		this.typedIngredient = null;
	}

	public void addAll(JeiTooltip tooltip) {
		lines.addAll(tooltip.lines);
	}

	public boolean isEmpty() {
		return lines.isEmpty() && typedIngredient == null;
	}

	@SuppressWarnings("removal")
	@Override
	public List<Component> toLegacyToComponents() {
		return lines.stream()
			.<Component>mapMulti((e, consumer) -> {
				e.left().ifPresent(f -> {
					if (f instanceof Component c) {
						consumer.accept(c);
					}
				});
			})
			.collect(Collectors.toCollection(ArrayList::new));
	}

	@SuppressWarnings("removal")
	@Override
	public void removeAll(List<Component> components) {
		for (Component component : components) {
			lines.remove(Either.left(component));
		}
	}

	@Override
	public String toString() {
		return lines.stream()
			.map(e -> e.map(
				FormattedText::getString,
				Object::toString
			))
			.collect(Collectors.joining("\n", "[\n", "\n]"));
	}

	public void draw(GuiGraphics guiGraphics, int x, int y) {
		if (typedIngredient != null) {
			draw(guiGraphics, x, y, typedIngredient);
			return;
		}
		if (isEmpty()) {
			return;
		}
		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
		try {
			renderHelper.renderTooltip(guiGraphics, lines, x, y, font, ItemStack.EMPTY);
		} catch (RuntimeException e) {
			throw new RuntimeException("Crashed when rendering tooltip:\n" + this);
		}
	}

	private <T> void draw(GuiGraphics guiGraphics, int x, int y, ITypedIngredient<T> typedIngredient) {
		IIngredientType<T> ingredientType = typedIngredient.getType();
		IIngredientManager ingredientManager = Internal.getJeiRuntime().getIngredientManager();
		IIngredientRenderer<T> ingredientRenderer = ingredientManager.getIngredientRenderer(ingredientType);
		draw(guiGraphics, x, y, typedIngredient, ingredientRenderer, ingredientManager);
	}

	public <T> void draw(
		GuiGraphics guiGraphics,
		int x,
		int y,
		ITypedIngredient<T> typedIngredient,
		IIngredientRenderer<T> ingredientRenderer,
		IIngredientManager ingredientManager
	) {
		Minecraft minecraft = Minecraft.getInstance();
		T ingredient = typedIngredient.getIngredient();
		Font font = ingredientRenderer.getFontRenderer(minecraft, ingredient);
		ItemStack itemStack = typedIngredient.getItemStack().orElse(ItemStack.EMPTY);

		itemStack.getTooltipImage()
			.ifPresent((c) -> {
				lines.add(1, Either.right(c));
			});

		addDebugInfo(ingredientManager, typedIngredient);

		IJeiHelpers jeiHelpers = Internal.getJeiRuntime().getJeiHelpers();
		IModIdHelper modIdHelper = jeiHelpers.getModIdHelper();
		modIdHelper.getModNameForTooltip(typedIngredient)
			.ifPresent(this::add);

		if (isEmpty()) {
			return;
		}
		try {
			IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
			renderHelper.renderTooltip(guiGraphics, lines, x, y, font, itemStack);
		} catch (RuntimeException e) {
			CrashReport crashReport = ErrorUtil.createIngredientCrashReport(e, "Rendering ingredient tooltip", ingredientManager, typedIngredient);
			crashReport.addCategory("tooltip")
				.setDetail("value", this);
			throw new ReportedException(crashReport);
		}
	}

	private <T> void addDebugInfo(IIngredientManager ingredientManager,  ITypedIngredient<T> typedIngredient) {
		if (!DebugConfig.isDebugInfoTooltipsEnabled() || !Minecraft.getInstance().options.advancedItemTooltips) {
			return;
		}
		T ingredient = typedIngredient.getIngredient();
		IIngredientType<T> type = typedIngredient.getType();
		IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(type);
		Codec<T> ingredientCodec = ingredientManager.getIngredientCodec(type);

		add(Component.empty());
		add(
			Component.literal("JEI Debug:")
				.withStyle(ChatFormatting.DARK_GRAY)
		);
		add(
			Component.literal("• type: " + ingredientHelper.getIngredientType().getUid())
				.withStyle(ChatFormatting.DARK_GRAY)
		);
		add(
			Component.literal("• has subtypes: " + (ingredientHelper.hasSubtypes(ingredient) ? "true" : "false"))
				.withStyle(ChatFormatting.DARK_GRAY)
		);
		add(
			Component.literal("• uid: " + ingredientHelper.getUid(ingredient, UidContext.Ingredient))
				.withStyle(ChatFormatting.DARK_GRAY)
		);
		try {
			Minecraft minecraft = Minecraft.getInstance();
			ClientLevel level = minecraft.level;
			assert level != null;
			RegistryAccess registryAccess = level.registryAccess();
			RegistryOps<JsonElement> registryOps = registryAccess.createSerializationContext(JsonOps.INSTANCE);
			String jsonResult = ingredientCodec.encodeStart(registryOps, ingredient)
					.mapOrElse(
						JsonElement::toString,
						DataResult.Error::message
					);
			add(
				Component.literal("• json: " + jsonResult)
					.withStyle(ChatFormatting.DARK_GRAY)
			);
		} catch (RuntimeException e) {
			add(
				Component.literal("• json crashed: " + e.getMessage())
					.withStyle(ChatFormatting.DARK_RED)
			);
		}
		add(
			Component.literal("• extra info: " + ingredientHelper.getErrorInfo(ingredient))
				.withStyle(ChatFormatting.DARK_GRAY)
		);
		add(Component.empty());
	}
}
