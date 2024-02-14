package dev.huskuraft.effortless.api.platform;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.input.KeyBinding;
import dev.huskuraft.effortless.api.input.OptionKeys;
import dev.huskuraft.effortless.api.text.Text;

public interface ClientContentFactory extends ContentFactory {

    SearchTree<ItemStack> searchItemStack(SearchBy searchBy);

    @Deprecated
	<T> SearchTree<T> search(List<T> list, Function<T, Stream<Text>> keyExtractor);

    KeyBinding getOptionKeyBinding(OptionKeys key);

    KeyBinding newKeyBinding(String name, String category, int code);

    default KeyBinding newKeyBinding(int code) {
        return newKeyBinding("", "", code);
    }

    ClientContentFactory INSTANCE = PlatformServiceLoader.load(ClientContentFactory.class).get();

    static ClientContentFactory getInstance() {
        return INSTANCE;
    }

}
