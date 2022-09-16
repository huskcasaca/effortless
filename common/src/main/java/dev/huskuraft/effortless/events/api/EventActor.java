package dev.huskuraft.effortless.events.api;

@FunctionalInterface
public interface EventActor<T> {

    EventResult get(T t);

}
