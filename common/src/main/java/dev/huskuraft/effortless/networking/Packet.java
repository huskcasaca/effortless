package dev.huskuraft.effortless.networking;

import dev.huskuraft.effortless.core.Player;

public interface Packet<T extends PacketListener> {

    void handle(T packetListener, Player sender);

}
