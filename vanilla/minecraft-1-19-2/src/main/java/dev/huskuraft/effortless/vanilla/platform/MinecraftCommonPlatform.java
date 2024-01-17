package dev.huskuraft.effortless.vanilla.platform;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Resource;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.platform.Platform;
import dev.huskuraft.effortless.api.tag.TagIoReader;
import dev.huskuraft.effortless.api.tag.TagIoWriter;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.vanilla.adapters.*;
import io.netty.buffer.Unpooled;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Optional;

public class MinecraftCommonPlatform implements Platform {

    @Override
    public Resource newResource(String namespace, String path) {
        return MinecraftConvertor.fromPlatformResource(new ResourceLocation(namespace, path));
    }

    @Override
    public Buffer newBuffer() {
        return MinecraftConvertor.fromPlatformBuffer(new FriendlyByteBuf(Unpooled.buffer()));
    }

    @Override
    public TagRecord newTagRecord() {
        return MinecraftConvertor.fromPlatformTagRecord(new CompoundTag());
    }

    @Override
    public Optional<Item> newOptionalItem(Resource resource) {
        return DefaultedRegistry.ITEM.getOptional(MinecraftConvertor.toPlatformResource(resource)).map(MinecraftConvertor::fromPlatformItem);
    }

    @Override
    public ItemStack newItemStack() {
        return MinecraftConvertor.fromPlatformItemStack(net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public ItemStack newItemStack(Item item, int count) {
        return MinecraftConvertor.fromPlatformItemStack(MinecraftConvertor.toPlatformItem(item), count);
    }

    @Override
    public ItemStack newItemStack(Item item, int count, TagRecord tag) {
        return MinecraftConvertor.fromPlatformItemStack(MinecraftConvertor.toPlatformItem(item), MinecraftConvertor.toPlatformTagRecord(tag), count);
    }

    @Override
    public Text newText() {
        return MinecraftConvertor.fromPlatformText(Component.empty());
    }

    @Override
    public Text newText(String text) {
        return MinecraftConvertor.fromPlatformText(Component.literal(text));
    }

    @Override
    public Text newText(String text, Text... args) {
        return MinecraftConvertor.fromPlatformText(Component.translatable(text, Arrays.stream(args).map(MinecraftConvertor::toPlatformText).toArray(Object[]::new)));
    }

    @Override
    public Text newTranslatableText(String text) {
        return MinecraftConvertor.fromPlatformText(Component.translatable(text));
    }

    @Override
    public Text newTranslatableText(String text, Text... args) {
        return MinecraftConvertor.fromPlatformText(Component.translatable(text, Arrays.stream(args).map(MinecraftConvertor::toPlatformText).toArray(Object[]::new)));
    }

    @Override
    public TagIoReader getTagIoReader() {
        return input -> MinecraftConvertor.fromPlatformTagRecord(NbtIo.readCompressed(input));
    }

    @Override
    public TagIoWriter getTagIoWriter() {
        return (output, config) -> NbtIo.writeCompressed(MinecraftConvertor.toPlatformTagRecord(config), output);
    }

}
