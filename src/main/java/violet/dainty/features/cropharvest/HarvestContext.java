package violet.dainty.features.cropharvest;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

public record HarvestContext(Player player, Block block) {}