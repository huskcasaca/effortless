package dev.huskuraft.effortless.api.events.networking;

import dev.huskuraft.effortless.api.networking.NetworkRegistry;

@FunctionalInterface
public interface RegisterNetwork {
    void onRegisterNetwork(NetworkRegistry registry);
}
