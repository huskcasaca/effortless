package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.Player;

public interface NetByteBufSender {

    void sendBuffer(NetByteBuf byteBuf, Player player);

}
