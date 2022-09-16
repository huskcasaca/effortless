package dev.huskcasaca.effortless.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.Screen;

import javax.annotation.Nullable;

public class ClientScreenEvent {

    public static final Event<ScreenOpening> SCREEN_OPENING_EVENT = EventFactory.createArrayBacked(ScreenOpening.class, callbacks -> (screen) -> {
        for (ScreenOpening callback : callbacks) {
            callback.onScreenOpening(screen);
        }
    });

    @FunctionalInterface
    public interface ScreenOpening {
        void onScreenOpening(@Nullable Screen screen);
    }

}