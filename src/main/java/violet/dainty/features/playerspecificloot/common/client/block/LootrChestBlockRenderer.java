package violet.dainty.features.playerspecificloot.common.client.block;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.ChestType;
import violet.dainty.features.playerspecificloot.api.LootrAPI;
import violet.dainty.features.playerspecificloot.api.data.blockentity.ILootrBlockEntity;
import violet.dainty.features.playerspecificloot.api.registry.LootrRegistry;
import violet.dainty.features.playerspecificloot.common.block.entity.LootrChestBlockEntity;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class LootrChestBlockRenderer<T extends LootrChestBlockEntity & ILootrBlockEntity> extends ChestRenderer<T> {
  public static final Material MATERIAL = new Material(Sheets.CHEST_SHEET, LootrAPI.rl("chest"));
  public static final Material MATERIAL2 = new Material(Sheets.CHEST_SHEET, LootrAPI.rl("chest_opened"));
  public static final Material MATERIAL3 = new Material(Sheets.CHEST_SHEET, LootrAPI.rl("chest_trapped"));
  public static final Material MATERIAL4 = new Material(Sheets.CHEST_SHEET, LootrAPI.rl("chest_trapped_opened"));
  public static final Material OLD_MATERIAL = new Material(Sheets.CHEST_SHEET, LootrAPI.rl("old_chest"));
  public static final Material OLD_MATERIAL2 = new Material(Sheets.CHEST_SHEET, LootrAPI.rl("old_chest_opened"));

  public LootrChestBlockRenderer(BlockEntityRendererProvider.Context p_173607_) {
    super(p_173607_);
  }

  // TODO: NeoForge injection
  @Override
  protected Material getMaterial(T blockEntity, ChestType type) {
    if (LootrAPI.isVanillaTextures()) {
      return Sheets.chooseMaterial(blockEntity, type, false);
    }
    // TODO: ???
    boolean trapped = blockEntity.getType().equals(LootrRegistry.getTrappedChestBlockEntity());
    if (blockEntity.hasClientOpened()) {
      if (LootrAPI.isOldTextures()) {
        return OLD_MATERIAL2;
      }
      return trapped ? MATERIAL4 : MATERIAL2;
    } else {
      if (LootrAPI.isOldTextures()) {
        return OLD_MATERIAL;
      }
      return trapped ? MATERIAL3 : MATERIAL;
    }
  }
}
