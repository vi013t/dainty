package violet.dainty.features.playerspecificloot.common.command;  

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.data.blockentity.ILootrBlockEntity;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;
import violet.dainty.features.playerspecificloot.common.block.LootrBarrelBlock;
import violet.dainty.features.playerspecificloot.common.block.LootrChestBlock;
import violet.dainty.features.playerspecificloot.common.block.LootrShulkerBlock;
import violet.dainty.features.playerspecificloot.common.block.entity.LootrInventoryBlockEntity;
import violet.dainty.features.playerspecificloot.common.entity.LootrChestMinecartEntity;
import violet.dainty.mixins.playerspecificloot.MixinBaseContainerBlockEntity;

public class CommandLootr {
  private static List<ResourceKey<LootTable>> tables = null;
  private static List<String> tableNames = null;
  private final CommandDispatcher<CommandSourceStack> dispatcher;

  public CommandLootr(CommandDispatcher<CommandSourceStack> dispatcher) {
    this.dispatcher = dispatcher;
  }

  private static List<ResourceKey<LootTable>> getTables() {
    if (tables == null) {
      tables = new ArrayList<>(BuiltInLootTables.all());
      tableNames = tables.stream().map(o -> o.location().toString()).toList();
    }
    return tables;
  }

  @SuppressWarnings("null")
private static List<String> getProfiles() {
    return Lists.newArrayList(LootrAPI.getServer().getProfileCache().profilesByName.keySet());
  }

  private static List<String> getTableNames() {
    getTables();
    return tableNames;
  }

  public static void createBlock(CommandSourceStack c, @Nullable Block block, @Nullable ResourceKey<LootTable> incomingTable) {
    Level world = c.getLevel();
    Vec3 incomingPos = c.getPosition();
    BlockPos pos = new BlockPos((int) incomingPos.x, (int) incomingPos.y, (int) incomingPos.z);
    ResourceKey<LootTable> table;
    if (incomingTable == null) {
      table = getTables().get(world.getRandom().nextInt(getTables().size()));
    } else {
      table = incomingTable;
    }
    if (block == null) {
      LootrChestMinecartEntity cart = new LootrChestMinecartEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
      Entity e = c.getEntity();
      if (e != null) {
        cart.setYRot(e.getYRot());
      }
      cart.setLootTable(table, world.getRandom().nextLong());
      world.addFreshEntity(cart);
      c.sendSuccess(() -> Component.translatable("dainty.commands.summon", ComponentUtils.wrapInSquareBrackets(Component.translatable("dainty.commands.blockpos", pos.getX(), pos.getY(), pos.getZ()).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN)).withBold(true))), table.toString()), false);
    } else {
      BlockState placementState = block.defaultBlockState();
      Entity e = c.getEntity();
      if (e != null) {
        EnumProperty<Direction> prop = null;
        Direction dir = Direction.orderedByNearest(e)[0].getOpposite();
        if (placementState.hasProperty(LootrBarrelBlock.FACING)) {
          prop = LootrBarrelBlock.FACING;
        } else if (placementState.hasProperty(LootrChestBlock.FACING)) {
          prop = LootrChestBlock.FACING;
          dir = e.getDirection().getOpposite();
        } else if (placementState.hasProperty(LootrShulkerBlock.FACING)) {
          prop = LootrShulkerBlock.FACING;
        }
        if (prop != null) {
          placementState = placementState.setValue(prop, dir);
        }
      }
      world.setBlock(pos, placementState, 2);
      if (world.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity randomizableBe) {
        randomizableBe.setLootTable(table, world.getRandom().nextLong());
      }
      c.sendSuccess(() -> Component.translatable("dainty.commands.create", Component.translatable(block.getDescriptionId()), ComponentUtils.wrapInSquareBrackets(Component.translatable("dainty.commands.blockpos", pos.getX(), pos.getY(), pos.getZ()).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN)).withBold(true))), table.toString()), false);
    }
  }

  private static NonNullList<ItemStack> copyItemList(NonNullList<ItemStack> reference) {
    NonNullList<ItemStack> contents = NonNullList.withSize(reference.size(), ItemStack.EMPTY);
    for (int i = 0; i < reference.size(); i++) {
      contents.set(i, reference.get(i).copy());
    }
    return contents;
  }

  public CommandLootr register() {
    this.dispatcher.register(builder(Commands.literal("dainty").requires(p -> p.hasPermission(2))));
    return this;
  }

  private RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> suggestTables() {
    return Commands.argument("table", ResourceLocationArgument.id())
        .suggests((c, build) -> SharedSuggestionProvider.suggest(getTableNames(), build));
  }

  private RequiredArgumentBuilder<CommandSourceStack, String> suggestProfiles() {
    return Commands.argument("profile", StringArgumentType.string()).suggests((c, build) -> SharedSuggestionProvider.suggest(getProfiles(), build));
  }

  public LiteralArgumentBuilder<CommandSourceStack> builder(LiteralArgumentBuilder<CommandSourceStack> builder) {
    builder.executes(c -> {
      c.getSource().sendSuccess(() -> Component.translatable("dainty.commands.usage"), false);
      return 1;
    });
    builder.then(Commands.literal("barrel").executes(c -> {
      createBlock(c.getSource(), LootrRegistry.getBarrelBlock(), null);
      return 1;
    }).then(suggestTables().executes(c -> {
      createBlock(c.getSource(), LootrRegistry.getBarrelBlock(), ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationArgument.getId(c, "table")));
      return 1;
    })));
    builder.then(Commands.literal("trapped_chest").executes(c -> {
      createBlock(c.getSource(), LootrRegistry.getTrappedChestBlock(), null);
      return 1;
    }).then(suggestTables().executes(c -> {
      createBlock(c.getSource(), LootrRegistry.getTrappedChestBlock(), ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationArgument.getId(c, "table")));
      return 1;
    })));
    builder.then(Commands.literal("chest").executes(c -> {
      createBlock(c.getSource(), LootrRegistry.getChestBlock(), null);
      return 1;
    }).then(suggestTables().executes(c -> {
      createBlock(c.getSource(), LootrRegistry.getChestBlock(), ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationArgument.getId(c, "table")));
      return 1;
    })));
    builder.then(Commands.literal("shulker").executes(c -> {
      createBlock(c.getSource(), LootrRegistry.getShulkerBlock(), null);
      return 1;
    }).then(suggestTables().executes(c -> {
      createBlock(c.getSource(), LootrRegistry.getShulkerBlock(), ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationArgument.getId(c, "table")));
      return 1;
    })));
    builder.then(Commands.literal("clear").executes(c -> {
      c.getSource().sendSuccess(() -> Component.literal("Must provide player name."), true);
      return 1;
    }).then(suggestProfiles().executes(c -> {
      String playerName = StringArgumentType.getString(c, "profile");
      @SuppressWarnings("null")
	Optional<GameProfile> opt_profile = c.getSource().getServer().getProfileCache().get(playerName);
      if (!opt_profile.isPresent()) {
        c.getSource().sendFailure(Component.literal("Invalid player name: " + playerName + ", profile not found in the cache."));
        return 0;
      }
      GameProfile profile = opt_profile.get();
      c.getSource().sendSuccess(() -> Component.literal(LootrAPI.clearPlayerLoot(profile.getId()) ? "Cleared stored inventories for " + playerName : "No stored inventories for " + playerName + " to clear"), true);
      return 1;
    })));
    builder.then(Commands.literal("cart").executes(c -> {
      createBlock(c.getSource(), null, null);
      return 1;
    }).then(suggestTables().executes(c -> {
      createBlock(c.getSource(), null, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocationArgument.getId(c, "table")));
      return 1;
    })));
    builder.then(Commands.literal("custom").executes(c -> {
      BlockPos pos = BlockPos.containing(c.getSource().getPosition());
      Level world = c.getSource().getLevel();
      BlockState state = world.getBlockState(pos);
      if (!state.is(Blocks.CHEST) && !state.is(Blocks.BARREL)) {
        pos = pos.below();
        state = world.getBlockState(pos);
      }
      if (!state.is(Blocks.CHEST) && !state.is(Blocks.BARREL)) {
        c.getSource().sendSuccess(() -> Component.literal("Please stand on the chest or barrel you wish to convert."), false);
      } else {
        NonNullList<ItemStack> reference;
        BlockState newState;
        if (state.is(Blocks.CHEST)) {
          reference = ((MixinBaseContainerBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).invokeGetItems();
          newState = LootrRegistry.getInventoryBlock().defaultBlockState().setValue(ChestBlock.FACING, state.getValue(ChestBlock.FACING)).setValue(ChestBlock.WATERLOGGED, state.getValue(ChestBlock.WATERLOGGED));
        } else {
          Direction facing = state.getValue(BarrelBlock.FACING);
          if (facing == Direction.UP || facing == Direction.DOWN) {
            facing = Direction.NORTH;
          }
          reference = ((MixinBaseContainerBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).invokeGetItems();
          newState = LootrRegistry.getInventoryBlock().defaultBlockState().setValue(ChestBlock.FACING, facing);
        }
        NonNullList<ItemStack> custom = copyItemList(reference);
        world.removeBlockEntity(pos);
        world.setBlockAndUpdate(pos, newState);
        BlockEntity te = world.getBlockEntity(pos);
        if (!(te instanceof LootrInventoryBlockEntity inventory)) {
          c.getSource().sendSuccess(() -> Component.literal("Unable to convert chest, BlockState is not a Lootr Inventory block."), false);
        } else {
          inventory.setCustomInventory(custom);
          inventory.setChanged();
        }
      }
      return 1;
    }));
    builder.then(Commands.literal("id").executes(c -> {
      BlockPos pos = BlockPos.containing(c.getSource().getPosition());
      Level world = c.getSource().getLevel();
      BlockEntity te = world.getBlockEntity(pos);
      if (!(te instanceof ILootrBlockEntity)) {
        pos = pos.below();
        te = world.getBlockEntity(pos);
      }
      if (!(te instanceof ILootrBlockEntity ibe)) {
        c.getSource().sendSuccess(() -> Component.literal("Please stand on a valid Lootr container."), false);
      } else {
        c.getSource().sendSuccess(() -> Component.literal("The ID of this inventory is: " + (ibe).getInfoUUID().toString()), false);
      }
      return 1;
    }));
    builder.then(Commands.literal("refresh").executes(c -> {
      BlockPos pos = BlockPos.containing(c.getSource().getPosition());
      Level level = c.getSource().getLevel();
      BlockEntity be = level.getBlockEntity(pos);
      if (!(be instanceof ILootrBlockEntity)) {
        pos = pos.below();
        be = level.getBlockEntity(pos);
      }
      if (be instanceof ILootrBlockEntity ibe) {
        LootrAPI.setRefreshing(ibe);
        c.getSource().sendSuccess(() -> Component.literal("Container with ID " + (ibe).getInfoUUID() + " has been set to refresh with a delay of " + LootrAPI.getRefreshValue()), false);
      } else {
        c.getSource().sendSuccess(() -> Component.literal("Please stand on a valid Lootr container."), false);
      }
      return 1;
    }));
    builder.then(Commands.literal("decay").executes(c -> {
      BlockPos pos = BlockPos.containing(c.getSource().getPosition());
      Level level = c.getSource().getLevel();
      BlockEntity be = level.getBlockEntity(pos);
      if (!(be instanceof ILootrBlockEntity)) {
        pos = pos.below();
        be = level.getBlockEntity(pos);
      }
      if (be instanceof ILootrBlockEntity ibe) {
        LootrAPI.setDecaying(ibe);
        c.getSource().sendSuccess(() -> Component.literal("Container with ID " + (ibe).getInfoUUID() + " has been set to decay with a delay of " + LootrAPI.getDecayValue()), false);
      } else {
        c.getSource().sendSuccess(() -> Component.literal("Please stand on a valid Lootr container."), false);
      }
      return 1;
    }));
    builder.then(Commands.literal("openers").then(Commands.argument("location", Vec3Argument.vec3()).executes(c -> {
      BlockPos position = Vec3Argument.getCoordinates(c, "location").getBlockPos(c.getSource());
      Level world = c.getSource().getLevel();
      BlockEntity blockEntity = world.getBlockEntity(position);
      if (blockEntity instanceof ILootrBlockEntity ibe) {
        Set<UUID> openers = ibe.getActualOpeners();
        if (openers != null) {
          c.getSource().sendSuccess(() -> Component.literal("BlockEntity at location " + position + " has " + openers.size() + " openers. UUIDs as follows:"), true);
          for (UUID uuid : openers) {
            @SuppressWarnings("null")
			Optional<GameProfile> prof = c.getSource().getServer().getProfileCache().get(uuid);
            c.getSource().sendSuccess(() -> Component.literal("UUID: " + uuid + ", user profile: " + (prof.isPresent() ? prof.get().getName() : "null")), true);
          }
        }
      } else {
        c.getSource().sendSuccess(() -> Component.literal("No Lootr block entity exists at location: " + position), false);
      }
      return 1;
    })));
    return builder;
  }
}
