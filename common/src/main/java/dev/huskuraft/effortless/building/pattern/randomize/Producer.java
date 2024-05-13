package dev.huskuraft.effortless.building.pattern.randomize;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public interface Producer<T> {

    static <T> Producer<T> createUnordered(Randomizer<T> randomizer, long seed, boolean consume) {
        if (consume) {
            return new ReduceUnordered<>(mapRandomizer(randomizer), seed);
        }
        return new Unordered<>(mapRandomizer(randomizer), seed);
    }

    static <T> Producer<T> createSequence(Randomizer<T> randomizer, long seed, boolean consume) {
        if (consume) {
            return new ReduceSequence<>(mapRandomizer(randomizer), seed);
        }
        return new Sequence<>(mapRandomizer(randomizer));
    }

    static <T> Producer<T> createSingle(T item) {
        return new Single<>(item);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] mapRandomizer(Randomizer<T> randomizer) {
        return (T[]) randomizer.getChances().stream().flatMap(holder -> IntStream.range(0, holder.chance()).mapToObj(i -> holder.content())).toArray(Object[]::new);
    }

    T next();

    // FIXME: 24/10/23
    class Unordered<T> extends Random implements Producer<T> {

        private final T[] items;

//        public Unordered(Randomizer<T> randomizer) {
//            this(randomizer, newSeed() ^ System.nanoTime());
//        }

        public Unordered(T[] randomizer, long seed) {
            super(seed);
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

    class Sequence<T> implements Producer<T> {

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

    class ReduceUnordered<T> extends Random implements Producer<T> {

        private final List<T> items;

        public ReduceUnordered(T[] items, long seed) {
            super(seed);
            this.items = new ArrayList<>(List.of(items));
        }

        @Override
        public T next() {
            if (items.isEmpty()) {
                return null;
            }
            int randomIndex = nextInt(items.size()); // Generate a random index
            return items.remove(randomIndex);
        }

    }

    class ReduceSequence<T> extends Random implements Producer<T> {

        private final List<T> items;

        public ReduceSequence(T[] items, long seed) {
            super(seed);
            this.items = new ArrayList<>(List.of(items));
        }

        @Override
        public T next() {
            if (items.isEmpty()) {
                return null;
            }
            return items.remove(0);
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
