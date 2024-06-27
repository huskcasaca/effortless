package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.events.impl.EventRegistry;
import dev.huskuraft.effortless.api.networking.NetworkChannel;

public interface Entrance {

    static Entrance getInstance() {
        return PlatformLoader.getSingleton();
    }

    String getId();

    EventRegistry getEventRegistry();

    NetworkChannel<?> getChannel();

    ServerManager getServerManager();

    default Server getServer() {
        return getServerManager().getRunningServer();
    }

}

