package violet.dainty.features.gravestone.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import violet.dainty.features.gravestone.DeathInfo;
import violet.dainty.features.gravestone.Gravestone;
import violet.dainty.features.gravestone.corelib.death.Death;
import violet.dainty.features.gravestone.corelib.death.DeathManager;
import violet.dainty.features.gravestone.net.MessageOpenObituary;

public class ObituaryItem extends Item {

    public ObituaryItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player p, @Nonnull InteractionHand hand) {
        if (!(p instanceof ServerPlayer player)) {
            return InteractionResultHolder.success(p.getItemInHand(hand));
        }

        ItemStack itemInHand = player.getItemInHand(hand);
        convert(itemInHand);
        Death death = fromStack(player, itemInHand);

        if (death == null) {
            player.displayClientMessage(Component.translatable("message.dainty.death_not_found"), true);
        } else if (player.isShiftKeyDown()) {
            if (player.hasPermissions(player.server.getOperatorUserPermissionLevel())) {
                Component replace = ComponentUtils.wrapInSquareBrackets(Component.translatable("message.dainty.restore.replace"))
                        .withStyle((style) -> style
                                .applyFormat(ChatFormatting.GREEN)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/restore @s " + death.getId().toString() + " replace"))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("message.dainty.restore.replace.description")))
                        );
                Component add = ComponentUtils.wrapInSquareBrackets(Component.translatable("message.dainty.restore.add"))
                        .withStyle((style) -> style
                                .applyFormat(ChatFormatting.GREEN)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/restore @s " + death.getId().toString() + " add"))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("message.dainty.restore.add.description")))
                        );
                player.sendSystemMessage(Component.translatable("message.dainty.restore").append(" ").append(replace).append(" ").append(add));
            }
        } else {
            PacketDistributor.sendToPlayer(player, new MessageOpenObituary(death));
        }
        return InteractionResultHolder.success(itemInHand);
    }

    @Nullable
    public Death fromStack(ServerPlayer player, ItemStack stack) {
        DeathInfo deathInfo = stack.get(Gravestone.DEATH_DATA_COMPONENT);
        if (deathInfo == null) {
            return null;
        }
        return DeathManager.getDeath(player.serverLevel(), deathInfo.getPlayerId(), deathInfo.getDeathId());
    }

    public ItemStack toStack(Death death) {
        ItemStack stack = new ItemStack(this);
        stack.set(Gravestone.DEATH_DATA_COMPONENT, new DeathInfo(death.getPlayerUUID(), death.getId())); 
        return stack;
    }

    public static void convert(ItemStack stack) {
        if (!(stack.getItem() instanceof ObituaryItem)) {
            return;
        }
        if (stack.has(Gravestone.DEATH_DATA_COMPONENT)) {
            return;
        }
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return;
        }
        CompoundTag compoundTag = customData.copyTag();
        if (!compoundTag.contains("Death", Tag.TAG_COMPOUND)) {
            return;
        }
        CompoundTag death = compoundTag.getCompound("Death");
        DeathInfo info = new DeathInfo(death.getUUID("PlayerUUID"), death.getUUID("DeathID"));
        compoundTag.remove("Death");
        if (compoundTag.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
        }
        stack.set(Gravestone.DEATH_DATA_COMPONENT, info);
    }
}
