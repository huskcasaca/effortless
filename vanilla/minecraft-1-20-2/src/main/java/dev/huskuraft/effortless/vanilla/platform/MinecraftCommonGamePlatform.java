package dev.huskuraft.effortless.vanilla.platform;

import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.platform.GamePlatform;
import dev.huskuraft.effortless.platform.SearchBy;
import dev.huskuraft.effortless.platform.SearchTree;
import dev.huskuraft.effortless.tag.TagIoReader;
import dev.huskuraft.effortless.tag.TagIoWriter;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftAdapter;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class MinecraftCommonGamePlatform extends GamePlatform {

    @Override
    public Buffer newBuffer() {
        return MinecraftAdapter.adapt(new FriendlyByteBuf(Unpooled.buffer()));
    }

    @Override
    public TagRecord newTagRecord() {
        return MinecraftAdapter.adapt(new CompoundTag());
    }

    @Override
    public Optional<Item> newOptionalItem(Resource resource) {
        return BuiltInRegistries.ITEM.getOptional(MinecraftAdapter.adapt(resource)).map(MinecraftAdapter::adapt);
    }

    @Override
    public ItemStack newItemStack() {
        return MinecraftAdapter.adapt(net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public ItemStack newItemStack(Item item, int count) {
        var itemStack = new net.minecraft.world.item.ItemStack(MinecraftAdapter.adapt(item), count);
        return MinecraftAdapter.adapt(itemStack);
    }

    @Override
    public ItemStack newItemStack(Item item, int count, TagRecord tag) {
        var itemStack = new net.minecraft.world.item.ItemStack(MinecraftAdapter.adapt(item), count);
        itemStack.setTag(MinecraftAdapter.adapt(tag));
        return MinecraftAdapter.adapt(itemStack);
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
    public SearchTree<ItemStack> newItemStackSearchTree(SearchBy searchBy) {
        return SearchTree.empty();
    }

    @Override
    public <T> SearchTree<T> newSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor) {
        return SearchTree.empty();
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
