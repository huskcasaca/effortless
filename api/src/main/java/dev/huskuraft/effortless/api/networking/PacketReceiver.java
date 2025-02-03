package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.Player;

public interface PacketReceiver extends ByteBufReceiver {

    void receivePacket(Packet packet, Player player);

}
