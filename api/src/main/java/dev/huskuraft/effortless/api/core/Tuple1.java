package dev.huskuraft.effortless.api.core;

import java.util.List;

public record Tuple1<T1>(T1 value1) implements Tuple {

    public Tuple1<T1> withValue1(T1 value1) {
        return new Tuple1<>(value1);
    }

    @Override
    public List<Object> asList() {
        return List.of(value1);
    }

}
