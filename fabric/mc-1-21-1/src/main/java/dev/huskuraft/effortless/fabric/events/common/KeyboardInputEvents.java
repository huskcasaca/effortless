package dev.huskuraft.effortless.fabric.events.common;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class KeyboardInputEvents {

    public static final Event<KeyInput> KEY_INPUT = EventFactory.createArrayBacked(KeyInput.class, callbacks -> (key, scanCode, action, modifiers) -> {
        for (KeyInput callback : callbacks) {
            callback.onKeyInput(key, scanCode, action, modifiers);
        }
    });

    @FunctionalInterface
    public interface KeyInput {
        void onKeyInput(int key, int scanCode, int action, int modifiers);
    }

}
