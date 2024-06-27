package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.Player;
import io.netty.buffer.ByteBuf;

public interface ByteBufReceiver {

    void receiveBuffer(ByteBuf byteBuf, Player player);

}
