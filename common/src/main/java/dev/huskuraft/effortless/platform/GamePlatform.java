package dev.huskuraft.effortless.platform;

import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.core.Items;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.tag.TagIoReader;
import dev.huskuraft.effortless.tag.TagIoWriter;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class GamePlatform {

    public abstract Buffer newBuffer();

    public abstract TagRecord newTagRecord();

    public abstract Optional<Item> newOptionalItem(Resource resource);

    public final Item newItem(Resource resource) {
        return newOptionalItem(resource).orElseThrow();
    }

    public abstract ItemStack newItemStack();

    public abstract ItemStack newItemStack(Item item, int count);

    public abstract ItemStack newItemStack(Item item, int count, TagRecord tag);

    public abstract Text newText();

    public abstract Text newText(String text);

    public abstract Text newText(String text, Text... args);

    public abstract Text newTranslatableText(String text);

    public abstract Text newTranslatableText(String text, Text... args);

    public abstract SearchTree<ItemStack> newItemStackSearchTree(SearchBy searchBy);

    public abstract <T> SearchTree<T> newSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor);

    public abstract TagIoReader getTagIoReader();

    public abstract TagIoWriter getTagIoWriter();

    public final Optional<Item> getOptionalItem(Items items) {
        return newOptionalItem(Resource.of("minecraft", items.name().toLowerCase(Locale.ROOT)));
    }

    public final Item getItem(Items items) {
        return getOptionalItem(items).orElseThrow();
    }

}
