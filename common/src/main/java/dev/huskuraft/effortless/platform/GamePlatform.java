package dev.huskuraft.effortless.platform;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class GamePlatform {

    public abstract Buffer newBuffer();

    public abstract TagRecord newTagRecord();

    public abstract Item newItem(Resource resource);

    public abstract ItemStack newItemStack();

    public abstract ItemStack newItemStack(Item item, int count);

    public abstract ItemStack newItemStack(Item item, int count, TagRecord tag);

    public abstract Text newText();

    public abstract Text newText(String text);

    public abstract Text newText(String text, Text... args);

    public abstract Text newTranslatableText(String text);

    public abstract Text newTranslatableText(String text, Text... args);

    public abstract List<ItemRandomizer> getDefaultRandomizers();

    public abstract List<Transformer> getDefaultTransformers();

    public abstract List<Pattern> getDefaultPatterns();

    public abstract SearchTree<ItemStack> newItemStackSearchTree(SearchBy searchBy);

    public abstract <T> SearchTree<T> newSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor);

}
