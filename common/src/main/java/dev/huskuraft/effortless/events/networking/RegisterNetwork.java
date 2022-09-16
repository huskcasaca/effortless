package dev.huskuraft.effortless.events.networking;

import dev.huskuraft.effortless.networking.NetworkRegistry;

@FunctionalInterface
public interface RegisterNetwork {
    void onRegisterNetwork(NetworkRegistry registry);
}
