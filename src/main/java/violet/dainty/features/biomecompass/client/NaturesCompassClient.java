package violet.dainty.features.biomecompass.client;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import violet.dainty.Dainty;
import violet.dainty.features.biomecompass.BiomeCompass;
import violet.dainty.features.biomecompass.items.NaturesCompassItem;
import violet.dainty.features.biomecompass.util.CompassState;

@EventBusSubscriber(modid = Dainty.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NaturesCompassClient {
	
	@SubscribeEvent
	public static void clientInit(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(BiomeCompass.naturesCompass, ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "angle"), new ClampedItemPropertyFunction() {
				@OnlyIn(Dist.CLIENT)
				private double rotation;
				@OnlyIn(Dist.CLIENT)
				private double rota;
				@OnlyIn(Dist.CLIENT)
				private long lastUpdateTick;

				@SuppressWarnings("null")
				@OnlyIn(Dist.CLIENT)
				@Override
				public float unclampedCall(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entityLiving, int seed) {
					if (entityLiving == null && !stack.isFramed()) {
						return 0.0F;
					} else {
						final boolean entityExists = entityLiving != null;
						final Entity entity = (Entity) (entityExists ? entityLiving : stack.getFrame());
						if (world == null && entity.level() instanceof ClientLevel) {
							world = (ClientLevel) entity.level();
						}

						double rotation = entityExists ? (double) entity.getYRot() : getFrameRotation((ItemFrame) entity);
						rotation = rotation % 360.0D;
						double adjusted = Math.PI - ((rotation - 90.0D) * 0.01745329238474369D - getAngle(world, entity, stack));

						if (entityExists) {
							adjusted = wobble(world, adjusted);
						}

						final float f = (float) (adjusted / (Math.PI * 2D));
						return Mth.positiveModulo(f, 1.0F);
					}
				}

				@OnlyIn(Dist.CLIENT)
				private double wobble(ClientLevel world, double amount) {
					if (world.getGameTime() != lastUpdateTick) {
						lastUpdateTick = world.getGameTime();
						double d0 = amount - rotation;
						d0 = Mth.positiveModulo(d0 + Math.PI, Math.PI * 2D) - Math.PI;
						d0 = Mth.clamp(d0, -1.0D, 1.0D);
						rota += d0 * 0.1D;
						rota *= 0.8D;
						rotation += rota;
					}

					return rotation;
				}

				@OnlyIn(Dist.CLIENT)
				private double getFrameRotation(ItemFrame itemFrame) {
					Direction direction = itemFrame.getDirection();
					int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
					return (double) Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + itemFrame.getRotation() * 45 + i);
				}

				@OnlyIn(Dist.CLIENT)
				private double getAngle(ClientLevel world, Entity entity, ItemStack stack) {
					if (stack.getItem() == BiomeCompass.naturesCompass) {
						NaturesCompassItem compassItem = (NaturesCompassItem) stack.getItem();
						BlockPos pos;
						if (compassItem.getState(stack) == CompassState.FOUND) {
							pos = new BlockPos(compassItem.getFoundBiomeX(stack), 0, compassItem.getFoundBiomeZ(stack));
						} else {
							pos = world.getSharedSpawnPos();
						}
						return Math.atan2((double) pos.getZ() - entity.position().z(), (double) pos.getX() - entity.position().x());
					}
					return 0.0D;
				}
			});
		});
	}
	
	@SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.BOSS_OVERLAY, ResourceLocation.fromNamespaceAndPath(Dainty.MODID, "natures_compass"), new NaturesCompassOverlay());
    }

}