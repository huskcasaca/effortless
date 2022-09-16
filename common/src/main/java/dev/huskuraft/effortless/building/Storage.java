package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Storage {

    static Storage fullStorage() {
        return new Storage() {
            @Override
            public Optional<ItemStack> searchByTag(ItemStack stack) {
                return Optional.of(stack);
            }

            @Override
            public Optional<ItemStack> searchByItem(Item item) {
                return Optional.of(item.getDefaultStack());
            }

            @Override
            public boolean consume(ItemStack stack) {
                return false;
            }

            @Override
            public List<ItemStack> contents() {
                return List.of();
            }

            @Override
            public Storage clone() {
                return this;
            }
        };
    }

    static Storage create(List<ItemStack> itemStacks) {
        return new Storage() {

            private final Map<Item, ItemStack> cache = new HashMap<>();

            @Override
            public Optional<ItemStack> searchByItem(Item item) {
                var last = cache.get(item);
                if (last != null && !last.isEmpty()) {
                    return Optional.of(last);
                }
                for (var itemStack : itemStacks) {
                    if (itemStack.isItem(item) && !itemStack.isEmpty()) {
                        cache.put(item, itemStack);
                        return Optional.of(itemStack);
                    }
                }
                cache.put(item, null);
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> searchByTag(ItemStack itemStack) {
                var result = searchByItem(itemStack.getItem());
                if (result.isPresent() && result.get().tagEquals(itemStack)) {
                    return result;
                } else {
                    return Optional.empty();
                }
            }

            @Override
            public boolean consume(ItemStack itemStack) {
                var found = searchByTag(itemStack);
                if (found.isEmpty()) {
                    return false;
                }
                if (itemStack.getStackSize() > found.get().getStackSize()) {
                    return false;
                }
                found.get().decrease(itemStack.getStackSize());
                return true;
            }

            @Override
            public List<ItemStack> contents() {
                return itemStacks;
            }

            @Override
            public Storage clone() {
                return create(itemStacks.stream().map(ItemStack::copy).toList());
            }
        };
    }

    Optional<ItemStack> searchByTag(ItemStack stack);

    Optional<ItemStack> searchByItem(Item item);

    boolean consume(ItemStack stack);

    List<ItemStack> contents();

    Storage clone();

}
