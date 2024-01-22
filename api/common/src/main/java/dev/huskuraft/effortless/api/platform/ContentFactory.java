package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.tag.TagIoReader;
import dev.huskuraft.effortless.api.tag.TagIoWriter;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.text.Text;

import java.util.Locale;
import java.util.Optional;

public interface ContentFactory {

    ResourceLocation newResource(String namespace, String path);

    Buffer newBuffer();

    TagRecord newTagRecord();

    Optional<Item> newOptionalItem(ResourceLocation location);

    default Item newItem(ResourceLocation location) {
        return newOptionalItem(location).orElseThrow();
    }

    ItemStack newItemStack();

    ItemStack newItemStack(Item item, int count);

    ItemStack newItemStack(Item item, int count, TagRecord tag);

    Text newText();

    Text newText(String text);

    Text newText(String text, Text... args);

    Text newTranslatableText(String text);

    Text newTranslatableText(String text, Text... args);

    TagIoReader getTagIoReader();

    TagIoWriter getTagIoWriter();

    Platform.OperatingSystem getOperatingSystem();

    default Optional<Item> getOptionalItem(Items items) {
        return newOptionalItem(ResourceLocation.of("minecraft", items.name().toLowerCase(Locale.ROOT)));
    }

    default Item getItem(Items items) {
        return getOptionalItem(items).orElseThrow();
    }

}
