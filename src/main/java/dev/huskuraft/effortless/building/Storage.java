package dev.huskuraft.effortless.building;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.huskuraft.universal.api.core.Item;
import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.universal.api.core.Player;

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
        public int consume(Item item, int count) {
            return count;
        }

        @Override
        public int getCount(Item item) {
            return Integer.MAX_VALUE;
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
        public int consume(Item item, int count) {
            return 0;
        }

        @Override
        public int getCount(Item item) {
            return 0;
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
            public int consume(Item item, int count) {
                return getStorage().consume(item, count);
            }

            @Override
            public int getCount(Item item) {
                return getStorage().getCount(item);
            }

            @Override
            public List<ItemStack> contents() {
                return getStorage().contents();
            }

        };
    }


    static Storage merge(Storage... storages) {
        return new Storage() {
            @Override
            public Optional<ItemStack> searchTag(ItemStack stack) {
                for (var storage : storages) {
                    var found = storage.searchTag(stack);
                    if (found.isPresent()) {
                        return found;
                    }
                }
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> search(Item item) {
                for (var storage : storages) {
                    var found = storage.search(item);
                    if (found.isPresent()) {
                        return found;
                    }
                }
                return Optional.empty();
            }

            @Override
            public boolean consume(ItemStack stack) {
                for (var storage : storages) {
                    if (storage.consume(stack)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public int consume(Item item, int count) {
                var consumed = 0;
                for (var storage : storages) {
                    consumed += storage.consume(item, count - consumed);
                    if (consumed >= count) {
                        return consumed;
                    }
                }
                return consumed;
            }

            @Override
            public int getCount(Item item) {
                var result = 0L;
                for (var storage : storages) {
                    result += storage.getCount(item);
                }
                return (int) Math.min(result, Integer.MAX_VALUE);
            }

            @Override
            public List<ItemStack> contents() {
                return Arrays.stream(storages).map(Storage::contents).flatMap(List::stream).toList();
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
                return Optional.empty();
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
            public int consume(Item item, int count) {
                if (infinite) {
                    return count;
                }
                var consumed = 0;
                for (var content : contents()) {
                    if (content.getItem().equals(item)) {
                        var available = Math.min(content.getCount(), count - consumed);
                        content.decrease(available);
                        consumed += available;
                    }
                    if (consumed >= count) {
                        return consumed;
                    }
                }
                return consumed;
            }

            @Override
            public int getCount(Item item) {
                if (infinite) {
                    return Integer.MAX_VALUE;
                }
                var result = 0;
                for (var content : contents()) {
                    if (content.getItem().equals(item)) {
                        result += content.getCount();
                    }
                }
                return result;
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

    int consume(Item item, int count);

    int getCount(Item item);

//    default boolean contains(Item item) {
//        var result = search(item);
//        return result.isPresent() && !result.get().isEmpty();
//    }
//

    List<ItemStack> contents();

}
