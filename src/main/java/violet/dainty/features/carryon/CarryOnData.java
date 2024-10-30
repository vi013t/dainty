package violet.dainty.features.carryon;

import java.util.Optional;

import javax.annotation.Nullable;

import com.mojang.serialization.DataResult;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CarryOnData {

    private CarryType type;
    private CompoundTag nbt;
    private boolean keyPressed = false;
    private CarryOnScript activeScript;
    private int selectedSlot = 0;

    public CarryOnData(CompoundTag data) {
        if (data.contains("type")) this.type = CarryType.valueOf(data.getString("type"));
        else this.type = CarryType.INVALID;
        this.nbt = data;

        if (data.contains("keyPressed")) this.keyPressed = data.getBoolean("keyPressed");

        if (data.contains("activeScript")) {
            DataResult<CarryOnScript> res = CarryOnScript.CODEC.parse(NbtOps.INSTANCE, data.get("activeScript"));
            this.activeScript = res.getOrThrow((s) -> {throw new RuntimeException("Failed to decode activeScript during CarryOnData serialization: " + s);});
        }

        if (data.contains("selected")) this.selectedSlot = data.getInt("selected");
    }

    public CompoundTag getNbt() {
        nbt.putString("type", type.toString());
        nbt.putBoolean("keyPressed", keyPressed);
        if(activeScript != null) {
            DataResult<Tag> res = CarryOnScript.CODEC.encodeStart(NbtOps.INSTANCE, activeScript);
            Tag tag = res.getOrThrow((s) -> {throw new RuntimeException("Failed to encode activeScript during CarryOnData serialization: " + s);});
            nbt.put("activeScript", tag);
        }
        nbt.putInt("selected", this.selectedSlot);
        return nbt;
    }

    public CompoundTag getContentNbt() {
        if (type == CarryType.BLOCK && nbt.contains("block")) return nbt.getCompound("block");
        else if (type == CarryType.ENTITY && nbt.contains("entity")) return nbt.getCompound("entity");
        return null;
    }

    public void setBlock(BlockState state, @Nullable BlockEntity tile) {
        this.type = CarryType.BLOCK;
        if(state.hasProperty(BlockStateProperties.WATERLOGGED)) state = state.setValue(BlockStateProperties.WATERLOGGED, false);
        CompoundTag stateData = NbtUtils.writeBlockState(state);
        nbt.put("block", stateData);

        if(tile != null) {
            @SuppressWarnings("null")
			CompoundTag tileData = tile.saveWithId(tile.getLevel().registryAccess());
            nbt.put("tile", tileData);
        }
    }

    public BlockState getBlock() {
        if (this.type != CarryType.BLOCK) throw new IllegalStateException("Called getBlock on data that contained " + this.type);
        return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), nbt.getCompound("block"));
    }

    @Nullable
    public BlockEntity getBlockEntity(BlockPos pos, HolderLookup.Provider lookup) {
        if (this.type != CarryType.BLOCK) throw new IllegalStateException("Called getBlockEntity on data that contained " + this.type);
        if (!nbt.contains("tile")) return null;
        return BlockEntity.loadStatic(pos, this.getBlock(), nbt.getCompound("tile"), lookup);
    }

    public void setEntity(Entity entity) {
        this.type = CarryType.ENTITY;
        CompoundTag entityData = new CompoundTag();
        entity.save(entityData);
        nbt.put("entity", entityData);
    }

    public Entity getEntity(Level level) {
        if(this.type != CarryType.ENTITY) throw new IllegalStateException("Called getEntity on data that contained " + this.type);

        var optionalEntity = EntityType.create(nbt.getCompound("entity"), level);
        if (optionalEntity.isPresent()) return optionalEntity.get();

       	System.err.println("Called EntityType#create even though no entity data was present. Data: " + nbt.toString());
        this.clear();
        return new AreaEffectCloud(level, 0, 0, 0);
    }

    public Optional<CarryOnScript> getActiveScript() {
        if (activeScript == null) return Optional.empty();
        return Optional.of(activeScript);
    }

    public void setActiveScript(CarryOnScript script) {
        this.activeScript = script;
    }

    public void setCarryingPlayer() {
        this.type = CarryType.PLAYER;
    }

    public boolean isCarrying() {
        return this.type != CarryType.INVALID;
    }

    public boolean isCarrying(CarryType type) {
        return this.type == type;
    }

    public boolean isKeyPressed() {
		return this.keyPressed;
	}

    public void setKeyPressed(boolean val) {
        this.keyPressed = val;
        this.nbt.putBoolean("keyPressed", val);
    }

    public void setSelected(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public int getSelected() {
        return this.selectedSlot;
    }

    public void clear() {
        this.type = CarryType.INVALID;
        this.nbt = new CompoundTag();
        this.activeScript = null;
    }

    public CarryOnData clone() {
        return new CarryOnData(nbt.copy());
    }

    public int getTick() {
        if (!this.nbt.contains("tick")) return -1;
        return this.nbt.getInt("tick");
    }

    public enum CarryType {
        BLOCK,
        ENTITY,
        PLAYER,
        INVALID
    }
}