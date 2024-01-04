package dev.huskuraft.effortless.api.events.api;

@FunctionalInterface
public interface EventActor<T> {

    EventResult get(T t);

}
