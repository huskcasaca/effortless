package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.Player;

public interface NetByteBufReceiver {

    void receiveBuffer(NetByteBuf byteBuf, Player player);

}
