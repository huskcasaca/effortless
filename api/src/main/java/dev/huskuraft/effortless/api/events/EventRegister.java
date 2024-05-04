package dev.huskuraft.effortless.api.events;

import java.util.HashMap;
import java.util.Map;

import dev.huskuraft.effortless.api.platform.PlatformLoader;

public class EventRegister {

    private final Map<Class<?>, Event<?>> events = new HashMap<>();

    private static Class<?> getReturnType(Class<?> functionalInterface) {
        var methods = functionalInterface.getDeclaredMethods();
        if (methods.length != 1) {
            throw new IllegalArgumentException("Functional interface must have exactly one method.");
        }
        var method = methods[0];
        return method.getReturnType();
    }

    public static EventRegister getClient() {
        return PlatformLoader.getSingleton(ClientEventRegistry.class);
    }

    public static EventRegister getCommon() {
        return PlatformLoader.getSingleton(CommonEventRegistry.class);
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

    public <T> void clear(Class<T> clazz) {
        events.remove(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> void clear(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        clear((Class<T>) typeGetter.getClass().getComponentType());
    }

}
