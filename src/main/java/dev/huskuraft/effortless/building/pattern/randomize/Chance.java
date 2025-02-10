package dev.huskuraft.effortless.building.pattern.randomize;

import java.util.Objects;

import dev.huskuraft.universal.api.core.Item;

public interface Chance<T> {

    int MIN_ITEM_COUNT = 0;
    int MAX_ITEM_COUNT = Byte.MAX_VALUE;

    static Chance<Item> item(Item item, int chance) {
        return of(item, chance);
    }

    static <T> Chance<T> of(T content) {
        return of(content, 1);
    }

    static <T> Chance<T> of(T content, int chance) {
        return new Chance<T>() {
            @Override
            public T content() {
                return content;
            }

            @Override
            public int chance() {
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
                result = 31 * result + chance;
                return result;
            }

            @Override
            public String toString() {
                return "Chance[" + "content=" + content + ", chance=" + chance + ']';
            }
        };
    }

    T content();

    int chance();
}
