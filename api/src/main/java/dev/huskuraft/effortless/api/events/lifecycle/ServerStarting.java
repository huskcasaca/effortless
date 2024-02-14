package dev.huskuraft.effortless.api.events.lifecycle;

import dev.huskuraft.effortless.api.platform.Server;

@FunctionalInterface
public interface ServerStarting {
    void onServerStarting(Server server);
}
