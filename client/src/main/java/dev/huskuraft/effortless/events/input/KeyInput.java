package dev.huskuraft.effortless.events.input;

import dev.huskuraft.effortless.input.InputKey;

@FunctionalInterface
public interface KeyInput {
    void onKeyInput(InputKey key);
}
