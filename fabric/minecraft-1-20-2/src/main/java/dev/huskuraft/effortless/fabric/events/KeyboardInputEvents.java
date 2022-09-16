package dev.huskuraft.effortless.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class KeyboardInputEvents {

    public static final Event<KeyPress> KEY_PRESS = EventFactory.createArrayBacked(KeyPress.class, callbacks -> (key, scanCode, action, modifiers) -> {
        for (KeyPress callback : callbacks) {
            callback.onKeyPress(key, scanCode, action, modifiers);
        }
    });

    @FunctionalInterface
    public interface KeyPress {
        void onKeyPress(int key, int scanCode, int action, int modifiers);
    }

}