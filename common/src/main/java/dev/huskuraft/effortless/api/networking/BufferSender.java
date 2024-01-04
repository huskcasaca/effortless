package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.Player;

public interface BufferSender {

    void sendBuffer(Buffer buffer, Player player);

}
