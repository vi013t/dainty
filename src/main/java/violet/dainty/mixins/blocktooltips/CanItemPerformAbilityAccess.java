package violet.dainty.mixins.blocktooltips;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.loot.CanItemPerformAbility;

@Mixin(CanItemPerformAbility.class)
public interface CanItemPerformAbilityAccess {
	@Accessor
	ItemAbility getAbility();
}
