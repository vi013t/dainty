package violet.dainty.features.carryon.client;

import java.util.Map;

import javax.annotation.Nullable;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.commands.arguments.blocks.BlockStateParser.BlockResult;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.commands.arguments.item.ItemParser.ItemResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import violet.dainty.features.carryon.Matchables.NBTCondition;

public class ModelOverride {

	private BlockResult parsedBlock;
	private Either<ItemStack, BlockState> renderObject;

	private ModelOverride(String raw, BlockResult parsedBlock, Type type, Either<ItemResult, BlockResult> parsedRHS) {
		this.parsedBlock = parsedBlock;

		parsedRHS.ifLeft(res -> {
			ItemStack stack = new ItemStack(res.item());
			if(res.components() != null)
				stack.applyComponents(res.components());
			this.renderObject = Either.left(stack);
		});

		parsedRHS.ifRight(res -> {
			BlockState state = res.blockState();
			this.renderObject = Either.right(state);
		});
	}

	public static DataResult<ModelOverride> of(String str, HolderLookup.Provider provider) {
		if (!str.contains("->")) return DataResult.error(() -> str + " must contain -> Arrow!");
		String[] split = str.split("->");
		String from = split[0];
		String to = split[1];

		BlockResult res;

		try {
			res = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), from, true);
		} catch (Exception e) {
			return DataResult.error(() -> "Error while parsing " + from + ":" + e.getMessage());
		}

		Type type = Type.ITEM;

		if (to.contains("(") && to.contains(")")) {
			String t = to.substring(to.indexOf("(") + 1, to.indexOf(")"));
			if (t.equals("block")) type = Type.BLOCK;
			to = to.substring(to.indexOf(")") + 1);
		}

		Either<ItemResult, BlockResult> either;
		try {
			if (type == Type.ITEM) either = Either.left(new ItemParser(provider).parse(new StringReader(to)));
			else either = Either.right(BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), to, true));
		} catch (CommandSyntaxException e) {
			String finalTo = to;
			return DataResult.error(() -> "Error while parsing " + finalTo + ":" + e.getMessage());
		}

		return DataResult.success(new ModelOverride(str, res, type, either));
	}

	public boolean matches(BlockState state, @Nullable CompoundTag tag) {
		if (state.getBlock() == parsedBlock.blockState().getBlock() && matchesProperties(state, parsedBlock.properties())) {
			if (tag == null || parsedBlock.nbt() == null) return true;
			NBTCondition nbt = new NBTCondition(parsedBlock.nbt());
			return nbt.matches(tag);
		}
		return false;
	}

	public Either<ItemStack, BlockState> getRenderObject() {
		return this.renderObject;
	}

	private boolean matchesProperties(BlockState state, Map<Property<?>, Comparable<?>> props) {
		for (var entry : props.entrySet()) {
			var val = state.getValue(entry.getKey());
			if (val != entry.getValue()) return false;
		}
		return true;
	}

	public enum Type {
		ITEM,
		BLOCK
	}

}