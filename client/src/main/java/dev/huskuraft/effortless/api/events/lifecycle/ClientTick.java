package dev.huskuraft.effortless.api.events.lifecycle;

import dev.huskuraft.effortless.api.core.TickPhase;
import dev.huskuraft.effortless.api.platform.Client;

@FunctionalInterface
public interface ClientTick {
    void onClientTick(Client client, TickPhase phase);
}
