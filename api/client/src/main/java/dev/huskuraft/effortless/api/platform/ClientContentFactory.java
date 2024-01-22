package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.input.KeyBinding;
import dev.huskuraft.effortless.api.input.KeyCodes;
import dev.huskuraft.effortless.api.input.OptionKeys;
import dev.huskuraft.effortless.api.renderer.RenderStateFactory;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.texture.TextureFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ClientContentFactory extends ContentFactory {

    SearchTree<ItemStack> newItemStackSearchTree(SearchBy searchBy);

    <T> SearchTree<T> newSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor);

    KeyBinding getOptionKeyBinding(OptionKeys key);

    KeyBinding newKeyBinding(String name, String category, KeyCodes key);

    RenderStateFactory getRenderStateFactory();

    TextureFactory getTextureFactory();

    ClientContentFactory INSTANCE = ClientEntrance.getInstance().getContentFactory();

}
