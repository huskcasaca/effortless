package dev.huskuraft.effortless.api.events.lifecycle;

import dev.huskuraft.effortless.api.platform.Client;

@FunctionalInterface
public interface ClientStart {
    void onClientStart(Client client);
}
