package dev.huskuraft.effortless.networking;

import dev.huskuraft.effortless.api.core.Player;

public interface BufferReceiver {

    void receiveBuffer(Buffer buffer, Player player);

}
