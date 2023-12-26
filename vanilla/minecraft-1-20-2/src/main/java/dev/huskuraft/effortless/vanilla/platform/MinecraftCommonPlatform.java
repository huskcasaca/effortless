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
import dev.huskuraft.effortless.vanilla.adapters.*;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Optional;

public class MinecraftCommonPlatform implements Platform {

    @Override
    public Buffer newBuffer() {
        return new MinecraftBuffer(new FriendlyByteBuf(Unpooled.buffer()));
    }

    @Override
    public TagRecord newTagRecord() {
        return MinecraftAdapter.adapt(new CompoundTag());
    }

    @Override
    public Optional<Item> newOptionalItem(Resource resource) {
        return BuiltInRegistries.ITEM.getOptional(MinecraftAdapter.adapt(resource)).map(MinecraftItem::new);
    }

    @Override
    public ItemStack newItemStack() {
        return new MinecraftItemStack(net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public ItemStack newItemStack(Item item, int count) {
        return new MinecraftItemStack(((MinecraftItem) item).getRef(), count);
    }

    @Override
    public ItemStack newItemStack(Item item, int count, TagRecord tag) {
        return new MinecraftItemStack(((MinecraftItem) item).getRef(), ((MinecraftTagRecord) tag).getRef(), count);
    }

    @Override
    public Text newText() {
        return MinecraftAdapter.adapt(Component.empty());
    }

    @Override
    public Text newText(String text) {
        return MinecraftAdapter.adapt(Component.literal(text));
    }

    @Override
    public Text newText(String text, Text... args) {
        return MinecraftAdapter.adapt(Component.translatable(text, Arrays.stream(args).map(MinecraftAdapter::adapt).toArray(Object[]::new)));
    }

    @Override
    public Text newTranslatableText(String text) {
        return MinecraftAdapter.adapt(Component.translatable(text));
    }

    @Override
    public Text newTranslatableText(String text, Text... args) {
        return MinecraftAdapter.adapt(Component.translatable(text, Arrays.stream(args).map(MinecraftAdapter::adapt).toArray(Object[]::new)));
    }

    @Override
    public TagIoReader getTagIoReader() {
        return input -> MinecraftAdapter.adapt(NbtIo.readCompressed(input));
    }

    @Override
    public TagIoWriter getTagIoWriter() {
        return (output, config) -> NbtIo.writeCompressed(MinecraftAdapter.adapt(config), output);
    }

}
