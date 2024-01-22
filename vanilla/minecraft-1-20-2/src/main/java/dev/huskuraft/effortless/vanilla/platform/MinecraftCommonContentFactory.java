package dev.huskuraft.effortless.vanilla.platform;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.Platform;
import dev.huskuraft.effortless.api.tag.TagIoReader;
import dev.huskuraft.effortless.api.tag.TagIoWriter;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.vanilla.core.*;
import dev.huskuraft.effortless.vanilla.tag.MinecraftTagRecord;
import io.netty.buffer.Unpooled;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Optional;

public class MinecraftCommonContentFactory implements ContentFactory {

    public static final MinecraftCommonContentFactory INSTANCE = new MinecraftCommonContentFactory();

    @Override
    public ResourceLocation newResource(String namespace, String path) {
        return new MinecraftResourceLocation(new net.minecraft.resources.ResourceLocation(namespace, path));
    }

    @Override
    public Buffer newBuffer() {
        return new MinecraftBuffer(new FriendlyByteBuf(Unpooled.buffer()));
    }

    @Override
    public TagRecord newTagRecord() {
        return new MinecraftTagRecord(new CompoundTag());
    }

    @Override
    public Optional<Item> newOptionalItem(ResourceLocation location) {
        return BuiltInRegistries.ITEM.getOptional(location.<net.minecraft.resources.ResourceLocation>reference()).map(item -> new MinecraftItem(item));
    }

    @Override
    public ItemStack newItemStack() {
        return new MinecraftItemStack(net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public ItemStack newItemStack(Item item, int count) {
        net.minecraft.world.item.Item item1 = item.reference();
        return new MinecraftItemStack(item1, count);
    }

    @Override
    public ItemStack newItemStack(Item item, int count, TagRecord tag) {
        net.minecraft.world.item.Item item1 = item.reference();
        CompoundTag tag1 = tag.reference();
        return new MinecraftItemStack(item1, tag1, count);
    }

    @Override
    public Text newText() {
        return new MinecraftText((Component) Component.empty());
    }

    @Override
    public Text newText(String text) {
        return new MinecraftText((Component) Component.literal(text));
    }

    @Override
    public Text newText(String text, Text... args) {
        return new MinecraftText((Component) Component.translatable(text, Arrays.stream(args).map(text1 -> text1.reference()).toArray(Object[]::new)));
    }

    @Override
    public Text newTranslatableText(String text) {
        return new MinecraftText((Component) Component.translatable(text));
    }

    @Override
    public Text newTranslatableText(String text, Text... args) {
        return new MinecraftText((Component) Component.translatable(text, Arrays.stream(args).map(text1 -> text1.reference()).toArray(Object[]::new)));
    }

    @Override
    public TagIoReader getTagIoReader() {
        return input -> new MinecraftTagRecord(NbtIo.readCompressed(input));
    }

    @Override
    public TagIoWriter getTagIoWriter() {
        return (output, config) -> NbtIo.writeCompressed(config.reference(), output);
    }

    @Override
    public Platform.OperatingSystem getOperatingSystem() {
        return switch (Util.getPlatform()) {
            case LINUX -> Platform.OperatingSystem.LINUX;
            case SOLARIS -> Platform.OperatingSystem.SOLARIS;
            case WINDOWS -> Platform.OperatingSystem.WINDOWS;
            case OSX -> Platform.OperatingSystem.MACOS;
            case UNKNOWN -> Platform.OperatingSystem.UNKNOWN;
        };
    }

}
