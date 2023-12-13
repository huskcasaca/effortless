package dev.huskuraft.effortless.building.pattern.randomize;

import dev.huskuraft.effortless.core.Item;

import java.util.Objects;

public interface Chance<T> {

    int MIN_ITEM_COUNT = 0;
    int MAX_ITEM_COUNT = Byte.MAX_VALUE;

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

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Chance<?> o1)) return false;

                if (chance != o1.chance()) return false;
                return Objects.equals(content, o1.content());
            }

            @Override
            public int hashCode() {
                int result = content != null ? content.hashCode() : 0;
                result = 31 * result + (int) chance;
                return result;
            }

            @Override
            public String toString() {
                return "Chance[" + "content=" + content + ", chance=" + chance + ']';
            }
        };
    }

    T content();

    byte chance();
}
