package dev.huskuraft.effortless.api.events.lifecycle;

import dev.huskuraft.effortless.api.platform.Client;

@FunctionalInterface
public interface ClientTick {
    void onClientTick(Client client, Phase phase);

    enum Phase {
        START,
        END
    }
}
