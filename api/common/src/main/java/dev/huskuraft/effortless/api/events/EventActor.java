package dev.huskuraft.effortless.api.events;

@FunctionalInterface
public interface EventActor<T> {

    EventResult get(T t);

}
