package dev.huskuraft.effortless.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class FabricLoaderEvents {

    public static final Event<InitializeMod> INIT_MOD = EventFactory.createArrayBacked(InitializeMod.class, callbacks -> () -> {
        for (var callback : callbacks) {
            callback.onInitializeMod();
        }
    });

    @FunctionalInterface
    public interface InitializeMod {
        void onInitializeMod();

    }

}
