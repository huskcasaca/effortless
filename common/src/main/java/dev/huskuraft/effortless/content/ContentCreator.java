package dev.huskuraft.effortless.content;

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

public abstract class ContentCreator {

    public abstract Buffer buffer();

    public abstract TagRecord tagRecord();

    public abstract Item item(Resource resource);

    public abstract ItemStack itemStack();

    public abstract ItemStack itemStack(Item item, int count);

    public abstract ItemStack itemStack(Item item, int count, TagRecord tag);

    public abstract Text text();

    public abstract Text text(String text);

    public abstract Text text(String text, Text... args);

    public abstract Text translate(String text);

    public abstract Text translate(String text, Text... args);

    public abstract List<ItemRandomizer> getDefaultRandomizers();

    public abstract List<Transformer> getDefaultTransformers();

    public abstract List<Pattern> getDefaultPatterns();

    public abstract SearchTree<ItemStack> itemStackSearchTree(SearchBy searchBy);

    public abstract <T> SearchTree<T> searchTree(List<T> list, Function<T, Stream<Text>> keyExtractor);

}
