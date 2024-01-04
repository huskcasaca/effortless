package dev.huskuraft.effortless.api.events.lifecycle;

import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.core.TickPhase;

@FunctionalInterface
public interface ClientTick {
    void onClientTick(Client client, TickPhase phase);
}
