package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.events.impl.EventRegistry;
import dev.huskuraft.effortless.api.networking.NetworkChannel;

import java.util.Optional;

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

    default <T extends Plugin> Optional<T> findPlugin(Class<T> pluginClass) {
        try {
            return Optional.of(PlatformLoader.getSingleton(pluginClass));
        } catch (LinkageError e) {
            return Optional.empty();
        }
    }

}

