package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.input.KeyBinding;
import dev.huskuraft.effortless.api.input.KeyCodes;
import dev.huskuraft.effortless.api.input.Keys;
import dev.huskuraft.effortless.api.renderer.RenderComponentFactory;
import dev.huskuraft.effortless.api.text.Text;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ClientPlatform extends Platform {

    SearchTree<ItemStack> newItemStackSearchTree(SearchBy searchBy);

    <T> SearchTree<T> newSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor);

    KeyBinding getKeyBinding(Keys key);

    KeyBinding newKeyBinding(String name, String category, KeyCodes key);

   RenderComponentFactory getRenderFactory();

    ClientPlatform INSTANCE = ClientEntrance.getInstance().getPlatform();

}
