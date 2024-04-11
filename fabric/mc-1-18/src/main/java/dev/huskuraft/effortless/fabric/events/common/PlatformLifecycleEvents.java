package dev.huskuraft.effortless.fabric.events.common;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class PlatformLifecycleEvents {

    public static final Event<Launch> COMMON_START = EventFactory.createArrayBacked(Launch.class, callbacks -> () -> {
        for (var callback : callbacks) {
            callback.onLaunch();
        }
    });

    public static final Event<Launch> CLIENT_START = EventFactory.createArrayBacked(Launch.class, callbacks -> () -> {
        for (var callback : callbacks) {
            callback.onLaunch();
        }
    });

    @FunctionalInterface
    public interface Launch {
        void onLaunch();
    }

}

