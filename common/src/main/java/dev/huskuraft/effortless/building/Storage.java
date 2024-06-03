package dev.huskuraft.effortless.building;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;

public interface Storage {

    Storage FULL = new Storage() {
        @Override
        public Optional<ItemStack> searchTag(ItemStack stack) {
            return Optional.of(stack.copy());
        }

        @Override
        public Optional<ItemStack> search(Item item) {
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
        public Optional<ItemStack> searchTag(ItemStack stack) {
            return Optional.empty();
        }

        @Override
        public Optional<ItemStack> search(Item item) {
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

    static Storage create(Player player, boolean copy) {
        return new Storage() {
            private final Storage storage;

            {
                storage = switch (player.getGameMode()) {
                    case SURVIVAL, ADVENTURE -> {
                        if (copy) {
                            yield Storage.create(player.getInventory().getItems().stream().map(ItemStack::copy).toList(), false);
                        } else {
                            yield Storage.create(player.getInventory().getItems(), false);
                        }
                    }
                    case CREATIVE -> Storage.merge(
                            Storage.create(player.getInventory().getItems().stream().map(ItemStack::copy).toList(), true),
                            full()
                    );
                    case SPECTATOR -> empty();
                };
            }

            private Storage getStorage() {
                return storage;
            }

            @Override
            public Optional<ItemStack> searchTag(ItemStack stack) {
                return getStorage().searchTag(stack);
            }

            @Override
            public Optional<ItemStack> search(Item item) {
                return getStorage().search(item);
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


    static Storage merge(Storage... storage) {
        return new Storage() {
            @Override
            public Optional<ItemStack> searchTag(ItemStack stack) {
                for (Storage storage1 : storage) {
                    var found = storage1.searchTag(stack);
                    if (found.isPresent()) {
                        return found;
                    }
                }
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> search(Item item) {
                for (Storage storage1 : storage) {
                    var found = storage1.search(item);
                    if (found.isPresent()) {
                        return found;
                    }
                }
                return Optional.empty();
            }

            @Override
            public boolean consume(ItemStack stack) {
                for (Storage storage1 : storage) {
                    if (storage1.consume(stack)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public List<ItemStack> contents() {
                return Arrays.stream(storage).map(Storage::contents).flatMap(List::stream).toList();
            }

        };
    }

    static Storage full() {
        return FULL;
    }

    static Storage empty() {
        return EMPTY;
    }

    static Storage create(List<ItemStack> itemStacks, boolean infinite) {
        return new Storage() {

            private final Map<Item, ItemStack> cache = new HashMap<>();

            // FIXME: 24/12/23
            @Override
            public Optional<ItemStack> search(Item item) {
                var last = cache.get(item);
                if (last != null && !last.isEmpty()) {
                    return Optional.of(last).map(stack -> infinite ? stack.copy() : stack);
                }
                for (var itemStack : itemStacks) {
                    if (itemStack.getItem().equals(item) && !itemStack.isEmpty()) {
                        cache.put(item, itemStack);
                        return Optional.of(itemStack).map(stack -> infinite ? stack.copy() : stack);
                    }
                }
                cache.put(item, ItemStack.empty());
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> searchTag(ItemStack itemStack) {
                var result = search(itemStack.getItem());
                if (result.isPresent() && result.get().getOrCreateTag().equals(itemStack.getOrCreateTag())) {
                    return result.map(stack -> infinite ? stack.copy() : stack);
                } else {
                    return Optional.empty();
                }
            }

            @Override
            public boolean consume(ItemStack itemStack) {
                var found = searchTag(itemStack);
                if (found.isEmpty()) {
                    return false;
                }
                if (infinite) {
                    return true;
                }
                if (itemStack.getCount() > found.get().getCount()) {
                    return false;
                }
                found.get().decrease(itemStack.getCount());
                return true;
            }

            @Override
            public List<ItemStack> contents() {
                return itemStacks;
            }

        };
    }

    Optional<ItemStack> searchTag(ItemStack stack);

    Optional<ItemStack> search(Item item);

    boolean consume(ItemStack stack);

    List<ItemStack> contents();

}
