package violet.dainty.features.wardenpotion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class WardenPotionEffect extends MobEffect {

	public WardenPotionEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x336666);
	}

	@Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		entity.removeEffect(MobEffects.DARKNESS);
        return true;
    }
    
    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return true;
    }
    
    @Override
    public void onEffectAdded(LivingEntity entity, int amplifier) {
        super.onEffectAdded(entity, amplifier);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
		super.onEffectStarted(entity, amplifier);
	}

}
