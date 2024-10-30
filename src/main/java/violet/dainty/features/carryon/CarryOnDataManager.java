package violet.dainty.features.carryon;

import net.minecraft.world.entity.player.Player;

public class CarryOnDataManager {

    public static CarryOnData getCarryData(Player player) {
        return ((ICarrying) player).getCarryOnData();
    }

    public static void setCarryData(Player player, CarryOnData data) {
        ((ICarrying) player).setCarryOnData(data);
    }

    public interface ICarrying {
        void setCarryOnData(CarryOnData data);
        CarryOnData getCarryOnData();
    }

}