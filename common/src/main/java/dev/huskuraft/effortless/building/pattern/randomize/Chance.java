package dev.huskuraft.effortless.building.pattern.randomize;

import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;

public interface Chance<T> {

    static Chance<ItemStack> itemStack(Item item, byte chance) {
        return itemStack(item.getDefaultStack(), chance);
    }

    static Chance<ItemStack> itemStack(ItemStack itemStack, byte chance) {
        return of(itemStack, chance);
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
