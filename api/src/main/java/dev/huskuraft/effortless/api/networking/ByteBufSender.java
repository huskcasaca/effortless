package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.Player;
import io.netty.buffer.ByteBuf;

public interface ByteBufSender {

    void sendBuffer(ByteBuf byteBuf, Player player);

}
