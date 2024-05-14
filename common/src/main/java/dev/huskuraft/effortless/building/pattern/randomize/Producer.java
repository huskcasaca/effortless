package dev.huskuraft.effortless.building.pattern.randomize;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public interface Producer<T> {

    static <T> Producer<T> createUnordered(Randomizer<T> randomizer, long seed, boolean consume) {
        return new Unordered<>(mapRandomizer(randomizer), seed, consume);
    }

    static <T> Producer<T> createSequence(Randomizer<T> randomizer, long seed, boolean consume) {
        return new Sequence<>(mapRandomizer(randomizer), consume);
    }

    static <T> Producer<T> createSingle(T item) {
        return new Single<>(item);
    }

    private static <T> List<T> mapRandomizer(Randomizer<T> randomizer) {
        return randomizer.getChances().stream().flatMap(holder -> IntStream.range(0, holder.chance()).mapToObj(i -> holder.content())).toList();
    }

    T next();

    class Unordered<T> extends Random implements Producer<T> {

        private final List<T> items;
        private final boolean consume;

        public Unordered(List<T> items, long seed, boolean consume) {
            super(seed);
            this.items = consume ? new ArrayList<>(items) : items;
            this.consume = consume;
        }

        @Override
        public T next() {
            if (items.isEmpty()) {
                return null;
            }
            if (consume) {
                return items.remove(nextInt(items.size()));
            }
            return items.get(nextInt(items.size()));
        }
    }

    class Sequence<T> implements Producer<T> {

        private final List<T> items;
        private final boolean consume;
        private int index = 0;

        public Sequence(List<T> items, boolean consume) {
            this.items = consume ? new ArrayList<>(items) : items;
            this.consume = consume;
        }

        @Override
        public T next() {
            if (items.isEmpty()) {
                return null;
            }
            if (consume) {
                return items.remove(0);
            }
            return items.get(index++ % items.size());
        }

    }

    class Single<T> implements Producer<T> {

        private final T item;

        public Single(T item) {
            this.item = item;
        }

        @Override
        public T next() {
            return item;
        }

    }
}
