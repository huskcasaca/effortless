package dev.huskuraft.effortless.api.events.input;

import dev.huskuraft.effortless.api.input.KeyBindingOwner;

public interface KeyRegistry {

    void register(KeyBindingOwner key);
}
