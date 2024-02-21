package dev.huskuraft.effortless.api.events;

import java.util.HashMap;
import java.util.Map;

import dev.huskuraft.effortless.api.events.lifecycle.ServerStarted;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStarting;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStopped;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStopping;
import dev.huskuraft.effortless.api.events.networking.RegisterNetwork;
import dev.huskuraft.effortless.api.events.player.PlayerChangeWorld;
import dev.huskuraft.effortless.api.events.player.PlayerLoggedIn;
import dev.huskuraft.effortless.api.events.player.PlayerLoggedOut;
import dev.huskuraft.effortless.api.events.player.PlayerRespawn;

public class EventRegistry {

    private final Map<Class<?>, Event<?>> events = new HashMap<>();

    private static Class<?> getReturnType(Class<?> functionalInterface) {
        var methods = functionalInterface.getDeclaredMethods();
        if (methods.length != 1) {
            throw new IllegalArgumentException("Functional interface must have exactly one method.");
        }
        var method = methods[0];
        return method.getReturnType();
    }

    @SuppressWarnings("unchecked")
    public <T> Event<T> get(Class<T> clazz) {
        return (Event<T>) events.computeIfAbsent(clazz, clazz1 -> {
            if (getReturnType(clazz1) == Void.TYPE) {
                return EventFactory.createLoop(clazz1);
            } else {
                return EventFactory.createEventResult(clazz1);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> Event<T> get(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return get((Class<T>) typeGetter.getClass().getComponentType());
    }

    public Event<RegisterNetwork> getRegisterNetworkEvent() {
        return get();
    }

    public Event<PlayerChangeWorld> getPlayerChangeWorldEvent() {
        return get();
    }

    public Event<PlayerRespawn> getPlayerRespawnEvent() {
        return get();
    }

    public Event<PlayerLoggedIn> getPlayerLoggedInEvent() {
        return get();
    }

    public Event<PlayerLoggedOut> getPlayerLoggedOutEvent() {
        return get();
    }

    public Event<ServerStarting> getServerStartingEvent() {
        return get();
    }

    public Event<ServerStarted> getServerStartedEvent() {
        return get();
    }

    public Event<ServerStopping> getServerStoppingEvent() {
        return get();
    }

    public Event<ServerStopped> getServerStoppedEvent() {
        return get();
    }

}
