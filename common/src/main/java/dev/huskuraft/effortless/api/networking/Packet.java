package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.Player;

public interface Packet<T extends PacketListener> {

    void handle(T packetListener, Player sender);

}
