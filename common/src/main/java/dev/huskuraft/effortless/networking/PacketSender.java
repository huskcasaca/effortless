package dev.huskuraft.effortless.networking;

import dev.huskuraft.effortless.api.core.Player;

public interface PacketSender extends BufferSender {

    void sendPacket(Packet packet, Player player);

    default void sendPacket(Packet packet) {
        sendPacket(packet, null);
    }


}
