package dev.huskuraft.effortless.building;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;

public interface Storage {

    static Storage create(Player player, boolean copy) {
        return new Storage() {
            private final Storage survivalStorage;

            {
                if (copy) {
                    survivalStorage = Storage.create(player.getItemStacks().stream().map(ItemStack::copy).toList());
                } else {
                    survivalStorage = Storage.create(player.getItemStacks());
                }
            }

            private Storage getStorage() {
                return switch (player.getGameType()) {
                    case SURVIVAL, ADVENTURE -> survivalStorage;
                    case CREATIVE -> full();
                    case SPECTATOR -> empty();
                };
            }

            @Override
            public Optional<ItemStack> searchByTag(ItemStack stack) {
                return getStorage().searchByTag(stack);
            }

            @Override
            public Optional<ItemStack> searchByItem(Item item) {
                return getStorage().searchByItem(item);
            }

            @Override
            public boolean consume(ItemStack stack) {
                return getStorage().consume(stack);
            }

            @Override
            public List<ItemStack> contents() {
                return getStorage().contents();
            }

        };
    }

    Storage FULL = new Storage() {
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
            return true;
        }

        @Override
        public List<ItemStack> contents() {
            return List.of();
        }

    };

    Storage EMPTY = new Storage() {
        @Override
        public Optional<ItemStack> searchByTag(ItemStack stack) {
            return Optional.empty();
        }

        @Override
        public Optional<ItemStack> searchByItem(Item item) {
            return Optional.empty();
        }

        @Override
        public boolean consume(ItemStack stack) {
            return false;
        }

        @Override
        public List<ItemStack> contents() {
            return List.of();
        }

    };

    static Storage full() {
        return FULL;
    }

    static Storage empty() {
        return EMPTY;
    }

    static Storage create(List<ItemStack> itemStacks) {
        return new Storage() {

            private final Map<Item, ItemStack> cache = new HashMap<>();

            // FIXME: 24/12/23
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
                cache.put(item, ItemStack.empty());
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

        };
    }

    Optional<ItemStack> searchByTag(ItemStack stack);

    Optional<ItemStack> searchByItem(Item item);

    boolean consume(ItemStack stack);

    List<ItemStack> contents();

}
