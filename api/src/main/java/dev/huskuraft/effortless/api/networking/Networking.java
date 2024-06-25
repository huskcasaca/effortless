package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.platform.Entrance;
import io.netty.buffer.ByteBuf;

public interface Networking {

    @Deprecated
    static ResourceLocation getChannelId() {
        return Entrance.getInstance().getChannel().getChannelId();
    }

    @Deprecated
    static int getCompatibilityVersion() {
        return Entrance.getInstance().getChannel().getCompatibilityVersion();
    }

    @Deprecated
    static String getCompatibilityVersionStr() {
        return Entrance.getInstance().getChannel().getCompatibilityVersionStr();
    }

    void sendToClient(ResourceLocation channelId, ByteBuf byteBuf, Player player);

    void sendToServer(ResourceLocation channelId, ByteBuf byteBuf, Player player);

    void registerClientReceiver(ResourceLocation channelId, ByteBufReceiver receiver);

    void registerServerReceiver(ResourceLocation channelId, ByteBufReceiver receiver);

}
