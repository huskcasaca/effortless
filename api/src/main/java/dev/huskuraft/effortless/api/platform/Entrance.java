package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.events.CommonEventRegistry;
import dev.huskuraft.effortless.api.networking.NetworkChannel;

public interface Entrance {

    static Entrance getInstance() {
        return PlatformLoader.getSingleton();
    }

    String getId();

    CommonEventRegistry getEventRegistry();

    NetworkChannel<?> getChannel();

    ServerManager getServerManager();

    default Server getServer() {
        return getServerManager().getRunningServer();
    }

}

