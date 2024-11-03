package violet.dainty.features.gravestone;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.world.level.block.Block;
import violet.dainty.features.gravestone.corelib.tag.Tag;
import violet.dainty.features.gravestone.corelib.tag.TagUtils;

public class GravestoneServerConfig {
	public static final boolean GIVE_OBITUARIES = true;
	public static final List<Tag<Block>> REPLACEABLE_BLOCKS_SPEC =  Collections.singletonList("#dainty:grave_replaceable").stream().map(TagUtils::getBlock).filter(Objects::nonNull).collect(Collectors.toList());
	public static final boolean REMOVE_OBITUARY = false;
	public static final boolean ONLY_OWNERS_CAN_BREAK = false;
	public static final boolean SPAWN_GHOST = false;
	public static final boolean FRIENDLY_GHOST = true;
	public static final boolean SNEAK_PICKUP = false;
	public static final boolean BREAK_PICKUP = true;
}
