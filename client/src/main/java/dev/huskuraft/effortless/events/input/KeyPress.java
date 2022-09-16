package dev.huskuraft.effortless.events.input;

@FunctionalInterface
public interface KeyPress {
    void onKeyPress(int key, int scanCode, int action, int modifiers);
}
