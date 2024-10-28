package violet.dainty;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Dainty.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<Float> ENDERMAN_PHANTOM_MEMBRANE_DROP_CHANCE = BUILDER
		.comment("Drop chance of phantom membranes from enderman")
		.define("endermanPhantomMembraneDropChance", 0.5f);

    private static final ModConfigSpec.ConfigValue<Boolean> DISABLE_PHANTOMS = BUILDER
		.comment("Disable phantom spawns")
		.define("disablePhantoms", true);

    private static final ModConfigSpec.ConfigValue<Boolean> DISABLE_FARMLAND_TRAMPLING = BUILDER
		.comment("Disable farmland trampling")
		.define("disableFarmlandTrampling", true);

    private static final ModConfigSpec.ConfigValue<Boolean> ALLOW_JUMPING_ON_FENCES = BUILDER
		.comment("Allow jumping on fences")
		.define("allowJumpingOnFences", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static float endermanPhantomMembraneDropChance;
	private static boolean disablePhantoms;
	private static boolean disableFarmlandTrampling;
	private static boolean allowJumpingOnFences;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        endermanPhantomMembraneDropChance = ENDERMAN_PHANTOM_MEMBRANE_DROP_CHANCE.get();
		disablePhantoms = DISABLE_PHANTOMS.get();
		disableFarmlandTrampling = DISABLE_FARMLAND_TRAMPLING.get();
		allowJumpingOnFences = ALLOW_JUMPING_ON_FENCES.get();
    }

	public static float endermanPhantomMembraneDropChance() {
		return endermanPhantomMembraneDropChance;
	}

	public static boolean disablePhantoms() {
		return disablePhantoms;
	}

	public static boolean disableFarmlandTrampling() {
		return disableFarmlandTrampling;
	}
	
	public static boolean allowJumpingOnFences() {
		return allowJumpingOnFences;
	}
}
