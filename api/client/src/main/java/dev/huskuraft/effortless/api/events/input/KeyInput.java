package dev.huskuraft.effortless.api.events.input;

import dev.huskuraft.effortless.api.input.InputKey;

@FunctionalInterface
public interface KeyInput {
    void onKeyInput(InputKey key);
}
