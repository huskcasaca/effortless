package dev.huskuraft.effortless.api.events.lifecycle;

import dev.huskuraft.effortless.api.platform.Server;

@FunctionalInterface
public interface ServerStarted {
    void onServerStarted(Server server);
}
