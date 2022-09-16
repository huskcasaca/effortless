package dev.huskuraft.effortless.events.input;

import dev.huskuraft.effortless.input.KeyRegistry;

@FunctionalInterface
public interface RegisterKeys {
    void onRegisterKeys(KeyRegistry keyRegistry);
}
