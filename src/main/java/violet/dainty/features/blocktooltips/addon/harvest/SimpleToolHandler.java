package violet.dainty.features.blocktooltips.addon.harvest;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleToolHandler implements ToolHandler {

	protected final List<ItemStack> tools = Lists.newArrayList();
	protected final List<Block> extraBlocks = Lists.newArrayListWithExpectedSize(0);
	private final ResourceLocation uid;
	private final boolean skipInstaBreakingBlock;

	protected SimpleToolHandler(ResourceLocation uid, List<ItemStack> tools, boolean skipInstaBreakingBlock) {
		this.uid = uid;
		Preconditions.checkArgument(!tools.isEmpty(), "tools cannot be empty");
		this.tools.addAll(tools);
		this.skipInstaBreakingBlock = skipInstaBreakingBlock;
	}

	public static SimpleToolHandler create(ResourceLocation uid, List<Item> tools) {
		return create(uid, tools, true);
	}

	public static SimpleToolHandler create(ResourceLocation uid, List<Item> tools, boolean skipInstaBreakingBlock) {
		return new SimpleToolHandler(uid, Lists.transform(tools, Item::getDefaultInstance), skipInstaBreakingBlock);
	}

	@Override
	public ItemStack test(BlockState state, Level world, BlockPos pos) {
		if (extraBlocks.contains(state.getBlock())) {
			return tools.getFirst();
		}
		if (skipInstaBreakingBlock && !state.requiresCorrectToolForDrops() && state.getDestroySpeed(world, pos) == 0) {
			return ItemStack.EMPTY;
		}
		return test(state);
	}

	protected ItemStack test(BlockState state) {
		tools:
		for (ItemStack toolItem : tools) {
			Tool tool = toolItem.get(DataComponents.TOOL);
			if (tool != null) {
				for (Tool.Rule rule : tool.rules()) {
					if (rule.correctForDrops().isPresent() && state.is(rule.blocks())) {
						if (rule.correctForDrops().get()) {
							return toolItem;
						}
						continue tools;
					}
				}
				if (tool.getMiningSpeed(state) > tool.defaultMiningSpeed()) {
					return toolItem;
				}
			}
			if (toolItem.isCorrectToolForDrops(state)) {
				return toolItem;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public List<ItemStack> getTools() {
		return tools;
	}

	@Override
	public ResourceLocation getUid() {
		return uid;
	}

	public SimpleToolHandler addExtraBlock(Block block) {
		extraBlocks.add(block);
		return this;
	}
}
