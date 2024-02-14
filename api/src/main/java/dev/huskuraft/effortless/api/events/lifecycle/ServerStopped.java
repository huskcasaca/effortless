package dev.huskuraft.effortless.api.events.lifecycle;

import dev.huskuraft.effortless.api.platform.Server;

@FunctionalInterface
public interface ServerStopped {
    void onServerStopped(Server server);
}
