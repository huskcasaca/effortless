package dev.huskuraft.effortless.api.events;

import java.util.HashMap;
import java.util.Map;

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

}
