package dev.huskuraft.effortless.platform;

import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.input.KeyBinding;
import dev.huskuraft.effortless.input.KeyBindings;
import dev.huskuraft.effortless.text.Text;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ClientPlatform extends Platform {

    SearchTree<ItemStack> newItemStackSearchTree(SearchBy searchBy);

    <T> SearchTree<T> newSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor);

    KeyBinding getKeyBinding(KeyBindings keyBindings);


}
