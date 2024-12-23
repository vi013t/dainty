package violet.dainty.features.recipeviewer.core.neoforge.platform;

import java.util.Optional;

import com.mojang.serialization.Codec;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import violet.dainty.features.recipeviewer.core.common.platform.IPlatformFluidHelperInternal;
import violet.dainty.features.recipeviewer.core.common.util.RegistryUtil;
import violet.dainty.features.recipeviewer.core.commonapi.gui.builder.ITooltipBuilder;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientRenderer;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.IIngredientTypeWithSubtypes;
import violet.dainty.features.recipeviewer.core.commonapi.ingredients.ITypedIngredient;
import violet.dainty.features.recipeviewer.core.library.render.FluidTankRenderer;
import violet.dainty.features.recipeviewer.core.neoforgeapi.NeoForgeTypes;

public class FluidHelper implements IPlatformFluidHelperInternal<FluidStack> {
	@Override
	public IIngredientTypeWithSubtypes<Fluid, FluidStack> getFluidIngredientType() {
		return NeoForgeTypes.FLUID_STACK;
	}

	@Override
	public IIngredientRenderer<FluidStack> createRenderer(long capacity, boolean showCapacity, int width, int height) {
		return new FluidTankRenderer<>(this, capacity, showCapacity, width, height);
	}

	@Override
	public int getColorTint(FluidStack ingredient) {
		Fluid fluid = ingredient.getFluid();
		IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
		return renderProperties.getTintColor(ingredient);
	}

	@Override
	public long getAmount(FluidStack ingredient) {
		return ingredient.getAmount();
	}

	@Override
	public FluidStack copyWithAmount(FluidStack ingredient, long amount) {
		FluidStack copy = ingredient.copy();
		int intAmount = Math.toIntExact(amount);
		copy.setAmount(intAmount);
		return copy;
	}

	@Override
	public DataComponentPatch getComponentsPatch(FluidStack ingredient) {
		return ingredient.getComponentsPatch();
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, FluidStack ingredient, TooltipFlag tooltipFlag) {
		Fluid fluid = ingredient.getFluid();
		if (fluid.isSame(Fluids.EMPTY)) {
			return;
		}

		Component displayName = getDisplayName(ingredient);
		tooltip.add(displayName);

		if (tooltipFlag.isAdvanced()) {
			Registry<Fluid> fluidRegistry = RegistryUtil.getRegistry(Registries.FLUID);
			ResourceLocation resourceLocation = fluidRegistry.getKey(fluid);
			if (resourceLocation != null &&  resourceLocation != BuiltInRegistries.FLUID.getDefaultKey()) {
				MutableComponent advancedId = Component.literal(resourceLocation.toString())
					.withStyle(ChatFormatting.DARK_GRAY);
				tooltip.add(advancedId);
			}
		}
	}

	@Override
	public long bucketVolume() {
		return FluidType.BUCKET_VOLUME;
	}

	@Override
	public Optional<TextureAtlasSprite> getStillFluidSprite(FluidStack fluidStack) {
		Fluid fluid = fluidStack.getFluid();
		IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
		ResourceLocation fluidStill = renderProperties.getStillTexture(fluidStack);
		//noinspection OptionalOfNullableMisuse
		return Optional.ofNullable(fluidStill)
			.map(f -> Minecraft.getInstance()
				.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
				.apply(f)
			)
			.filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
	}

	@Override
	public Component getDisplayName(FluidStack ingredient) {
		Component displayName = ingredient.getHoverName();

		Fluid fluid = ingredient.getFluid();
		if (!fluid.isSource(fluid.defaultFluidState())) {
			return Component.translatable("dainty.tooltip.liquid.flowing", displayName);
		}
		return displayName;
	}

	@Override
	public FluidStack create(Holder<Fluid> fluid, long amount, DataComponentPatch components) {
		int intAmount = (int) Math.min(amount, Integer.MAX_VALUE);
		return new FluidStack(fluid, intAmount, components);
	}

	@Override
	public FluidStack create(Holder<Fluid> fluid, long amount) {
		int intAmount = (int) Math.min(amount, Integer.MAX_VALUE);
		return new FluidStack(fluid, intAmount);
	}

	@Override
	public FluidStack copy(FluidStack ingredient) {
		return ingredient.copy();
	}

	@Override
	public FluidStack normalize(FluidStack ingredient) {
		if (ingredient.getAmount() == FluidType.BUCKET_VOLUME) {
			return ingredient;
		}
		return ingredient.copyWithAmount(FluidType.BUCKET_VOLUME);
	}

	@Override
	public Optional<FluidStack> getContainedFluid(ITypedIngredient<?> ingredient) {
		return ingredient.getItemStack()
			.flatMap(i -> Optional.ofNullable(i.getCapability(Capabilities.FluidHandler.ITEM)))
			.map(c -> c.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE));
	}

	@Override
	public Codec<FluidStack> getCodec() {
		return Codec.withAlternative(
			FluidStack.fixedAmountCodec(FluidType.BUCKET_VOLUME),
			FluidStack.CODEC // TODO: remove this fallback codec in the next major version of JEI
		);
	}
}
