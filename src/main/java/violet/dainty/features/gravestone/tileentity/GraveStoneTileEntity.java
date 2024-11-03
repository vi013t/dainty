package violet.dainty.features.gravestone.tileentity;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import violet.dainty.features.gravestone.GraveUtils;
import violet.dainty.features.gravestone.Gravestone;
import violet.dainty.features.gravestone.corelib.death.Death;

public class GraveStoneTileEntity extends BlockEntity implements Nameable {

    protected Death death;

    protected Component customName;

    public GraveStoneTileEntity(BlockPos pos, BlockState state) {
        super(Gravestone.GRAVESTONE_TILEENTITY.get(), pos, state); 
        death = new Death.Builder(GraveUtils.EMPTY_UUID, GraveUtils.EMPTY_UUID).build();
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.put("Death", death.toNBT(provider));
        if (customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(customName, provider));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);

        if (compound.contains("Death")) {
            death = Death.fromNBT(provider, compound.getCompound("Death"));
        } else { // Compatibility
            UUID playerUUID = GraveUtils.EMPTY_UUID;
            try {
                playerUUID = UUID.fromString(compound.getString("PlayerUUID"));
            } catch (Exception e) {
            }

            Death.Builder builder = new Death.Builder(playerUUID, UUID.randomUUID());

            NonNullList<ItemStack> items = NonNullList.create();
            ListTag list = compound.getList("ItemStacks", 10);
            for (int i = 0; i < list.size(); i++) {
                Optional<ItemStack> parse = ItemStack.parse(provider, list.getCompound(i));
                parse.ifPresent(items::add);
            }

            builder.additionalItems(items);
            builder.playerName(compound.getString("PlayerName"));
            builder.timestamp(compound.getLong("DeathTime"));
            death = builder.build();
        }

        if (compound.contains("CustomName")) {
            customName = Component.Serializer.fromJson(compound.getString("CustomName"), provider);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag compound = new CompoundTag();
        saveAdditional(compound, provider);
        return compound;
    }

    public Death getDeath() {
        return death;
    }

    public void setDeath(Death death) {
        this.death = death;
        setChanged();
    }

    public void setCustomName(Component name) {
        this.customName = name;
        setChanged();
    }

    @Override
    public Component getName() {
        return customName != null ? customName : getDefaultName();
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return customName;
    }

    protected Component getDefaultName() {
        String name = death.getPlayerName();
        if (name == null || name.isEmpty()) {
            return Component.translatable(Gravestone.GRAVESTONE.get().getDescriptionId());
        }
        return Component.translatable("message.gravestone.grave_of", name);
    }

    @Nullable
    public Component getGraveName() {
        if (!death.getPlayerName().isEmpty()) {
            return Component.literal(death.getPlayerName());
        } else if (customName != null) {
            return customName;
        } else {
            return null;
        }
    }

}
