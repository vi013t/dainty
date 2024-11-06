package violet.dainty.features.playerspecificloot.neoforge.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import violet.dainty.features.playerspecificloot.api.network.ILootrPacket;

/* Shamelessly cribbed from Mekanism
Original source: https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/network/IMekanismPacket.java
 */
public interface ILootrNeoForgePacket extends ILootrPacket  {
  void handle(IPayloadContext context);
}
