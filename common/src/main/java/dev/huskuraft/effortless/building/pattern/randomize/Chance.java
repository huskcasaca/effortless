package dev.huskuraft.effortless.building.pattern.randomize;

import dev.huskuraft.effortless.core.Item;

public interface Chance<T> {

    static Chance<Item> item(Item item, byte chance) {
        return of(item, chance);
    }

    static <T> Chance<T> of(T content) {
        return of(content, (byte) 1);
    }

    static <T> Chance<T> of(T content, byte chance) {
        return new Chance<T>() {
            @Override
            public T content() {
                return content;
            }

            @Override
            public byte chance() {
                return chance;
            }
        };
    }

    T content();

    byte chance();
}
