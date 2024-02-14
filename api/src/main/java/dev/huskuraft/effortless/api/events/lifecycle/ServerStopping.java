package dev.huskuraft.effortless.api.events.lifecycle;

import dev.huskuraft.effortless.api.platform.Server;

@FunctionalInterface
public interface ServerStopping {
    void onServerStopping(Server server);
}
