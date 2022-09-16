package dev.huskuraft.effortless.content;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class ContentCreator {

    public abstract Buffer allocateButter();

    public abstract TagRecord emptyTagRecord();

    public abstract ItemStack emptyItemStack();

    public abstract List<ItemRandomizer> getDefaultRandomizers();

    public abstract List<Transformer> getDefaultTransformers();

    public abstract List<Pattern> getDefaultPatterns();

    public abstract SearchTree<ItemStack> getItemsSearchTree(SearchBy searchBy);

    public abstract <T> SearchTree<T> createSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor);

    public abstract Text empty();

    public abstract Text text(String text);

    public abstract Text text(String text, Text... args);

    public abstract Text translate(String text);

    public abstract Text translate(String text, Text... args);

}
