package dev.huskuraft.effortless.api.file;

import java.util.function.UnaryOperator;

public interface Storage<T> {

    default void update(UnaryOperator<T> operator) {
        set(operator.apply(get()));
    }

    T get();

    void set(T config);

}
