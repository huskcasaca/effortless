package dev.huskuraft.effortless.vanilla.platform;

import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.platform.Platform;
import dev.huskuraft.effortless.tag.TagIoReader;
import dev.huskuraft.effortless.tag.TagIoWriter;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.vanilla.core.*;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Optional;

public class MinecraftCommonPlatform implements Platform {

    @Override
    public Resource newResource(String namespace, String path) {
        return MinecraftResource.fromMinecraftResource(new ResourceLocation(namespace, path));
    }

    @Override
    public Buffer newBuffer() {
        return MinecraftBuffer.fromMinecraftBuffer(new FriendlyByteBuf(Unpooled.buffer()));
    }

    @Override
    public TagRecord newTagRecord() {
        return MinecraftTagRecord.fromMinecraft(new CompoundTag());
    }

    @Override
    public Optional<Item> newOptionalItem(Resource resource) {
        return BuiltInRegistries.ITEM.getOptional(MinecraftResource.toMinecraftResource(resource)).map(MinecraftItem::fromMinecraft);
    }

    @Override
    public ItemStack newItemStack() {
        return MinecraftItemStack.fromMinecraft(net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public ItemStack newItemStack(Item item, int count) {
        return MinecraftItemStack.fromMinecraft(MinecraftItem.toMinecraft(item), count);
    }

    @Override
    public ItemStack newItemStack(Item item, int count, TagRecord tag) {
        return MinecraftItemStack.fromMinecraft(MinecraftItem.toMinecraft(item), MinecraftTagRecord.toMinecraft(tag), count);
    }

    @Override
    public Text newText() {
        return MinecraftText.fromMinecraftText(Component.empty());
    }

    @Override
    public Text newText(String text) {
        return MinecraftText.fromMinecraftText(Component.literal(text));
    }

    @Override
    public Text newText(String text, Text... args) {
        return MinecraftText.fromMinecraftText(Component.translatable(text, Arrays.stream(args).map(MinecraftText::toMinecraftText).toArray(Object[]::new)));
    }

    @Override
    public Text newTranslatableText(String text) {
        return MinecraftText.fromMinecraftText(Component.translatable(text));
    }

    @Override
    public Text newTranslatableText(String text, Text... args) {
        return MinecraftText.fromMinecraftText(Component.translatable(text, Arrays.stream(args).map(MinecraftText::toMinecraftText).toArray(Object[]::new)));
    }

    @Override
    public TagIoReader getTagIoReader() {
        return input -> MinecraftTagRecord.fromMinecraft(NbtIo.readCompressed(input, NbtAccounter.unlimitedHeap()));
    }

    @Override
    public TagIoWriter getTagIoWriter() {
        return (output, config) -> NbtIo.writeCompressed(MinecraftTagRecord.toMinecraft(config), output);
    }

}
