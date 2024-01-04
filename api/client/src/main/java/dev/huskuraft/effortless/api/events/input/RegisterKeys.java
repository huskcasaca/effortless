package dev.huskuraft.effortless.api.events.input;

@FunctionalInterface
public interface RegisterKeys {
    void onRegisterKeys(KeyRegistry keyRegistry);
}
