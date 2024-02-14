package dev.huskuraft.effortless.api.events.lifecycle;

import dev.huskuraft.effortless.api.platform.Server;

@FunctionalInterface
public interface ServerTick {
    void onClientTick(Server server, Phase phase);

    enum Phase {
        START,
        END
    }
}
