package dev.huskuraft.effortless.networking;

import dev.huskuraft.effortless.core.Player;

public interface PacketReceiver extends BufferReceiver {

    void receivePacket(Packet packet, Player player);

    default void receivePacket(Packet packet) {
        receivePacket(packet, null);
    }

}
