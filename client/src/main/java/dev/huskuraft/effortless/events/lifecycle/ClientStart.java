package dev.huskuraft.effortless.events.lifecycle;

import dev.huskuraft.effortless.platform.Client;

@FunctionalInterface
public interface ClientStart {
    void onClientStart(Client client);
}
