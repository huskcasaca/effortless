package dev.huskuraft.effortless.events.api;

public interface Event<T> {

    T invoker();

    void register(T listener);

    void unregister(T listener);

    boolean isRegistered(T listener);

    void clear();

}
