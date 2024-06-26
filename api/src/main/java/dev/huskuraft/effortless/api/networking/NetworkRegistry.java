package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public interface NetworkRegistry {

    ByteBufSender register(ResourceLocation channelId, Side side, ByteBufReceiver receiver);

}
