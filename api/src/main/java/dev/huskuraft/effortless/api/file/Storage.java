package dev.huskuraft.effortless.api.file;

import java.util.function.Consumer;

public interface Storage<T> {

    default void use(Consumer<T> consumer) {
        var config = get();
        consumer.accept(config);
        set(config);
    }

    T get();

    void set(T config);

}
