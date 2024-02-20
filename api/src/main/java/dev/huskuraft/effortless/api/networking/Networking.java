package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.platform.Entrance;

public interface Networking {

    static ResourceLocation getChannelId() {
        return Entrance.getInstance().getChannel().getChannelId();
    }

    static int getCompatibilityVersion() {
        return Entrance.getInstance().getChannel().getCompatibilityVersion();
    }

    static String getCompatibilityVersionStr() {
        return Entrance.getInstance().getChannel().getCompatibilityVersionStr();
    }

    void sendToClient(Buffer buffer, Player player);

    void sendToServer(Buffer buffer, Player player);

    void registerClientReceiver(BufferReceiver receiver);

    void registerServerReceiver(BufferReceiver receiver);

}
