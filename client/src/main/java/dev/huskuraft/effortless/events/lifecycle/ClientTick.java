package dev.huskuraft.effortless.events.lifecycle;

import dev.huskuraft.effortless.core.TickPhase;
import dev.huskuraft.effortless.platform.Client;

@FunctionalInterface
public interface ClientTick {
    void onClientTick(Client client, TickPhase phase);
}
