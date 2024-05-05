package dev.huskuraft.effortless.api.core;

import java.util.List;

public record Tuple4<T1, T2, T3, T4>(T1 value1, T2 value2, T3 value3, T4 value4) implements Tuple {

    public Tuple4<T1, T2, T3, T4> withValue1(T1 value1) {
        return new Tuple4<>(value1, value2, value3, value4);
    }

    public Tuple4<T1, T2, T3, T4> withValue2(T2 value2) {
        return new Tuple4<>(value1, value2, value3, value4);
    }

    public Tuple4<T1, T2, T3, T4> withValue3(T3 value3) {
        return new Tuple4<>(value1, value2, value3, value4);
    }

    public Tuple4<T1, T2, T3, T4> withValue4(T4 value4) {
        return new Tuple4<>(value1, value2, value3, value4);
    }

    @Override
    public List<Object> asList() {
        return List.of(value1, value2, value3, value4);
    }
}
