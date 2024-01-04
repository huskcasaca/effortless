package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.Player;

public interface BufferReceiver {

    void receiveBuffer(Buffer buffer, Player player);

}
