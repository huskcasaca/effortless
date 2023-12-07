package dev.huskuraft.effortless.building.pattern.randomize;

import dev.huskuraft.effortless.core.Item;

import java.util.Random;
import java.util.stream.IntStream;

public interface Source<T> {

    static <T> Source<T> createUnordered(Randomizer<T> randomizer, long seed) {
        return new Unordered<>(mapRandomizer(randomizer), seed);
    }

    static <T> Source<T> createSequence(Randomizer<T> randomizer) {
        return new Sequence<>(mapRandomizer(randomizer));
    }

    static <T> Source<T> createSingle(T item) {
        return new Single<>(item);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] mapRandomizer(Randomizer<T> randomizer) {
        return (T[]) randomizer.getChances().stream().flatMap(holder -> IntStream.range(0, holder.chance()).mapToObj(i -> holder.content())).toArray(Object[]::new);
    }

    T next();

    // FIXME: 24/10/23
    class Unordered<T> extends Random implements Source<T> {

        private final long seed;
        private final T[] items;

//        public Unordered(Randomizer<T> randomizer) {
//            this(randomizer, newSeed() ^ System.nanoTime());
//        }

        public Unordered(T[] randomizer, long seed) {
            super(seed);
            this.seed = seed;
            this.items = randomizer;
        }

        @Override
        public T next() {
            if (items.length == 0) {
                return null;
            }
            return items[nextInt(items.length)];
        }
    }

    class Sequence<T> implements Source<T> {

        private final T[] items;
        private int index = 0;

        public Sequence(T[] items) {
            this.items = items;
        }

        @Override
        public T next() {
            if (items.length == 0) {
                return null;
            }
            return items[index++ % items.length];
        }

    }

    class Single<T> implements Source<T> {

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
