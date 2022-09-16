package dev.huskuraft.effortless.vanilla.content;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.content.ContentCreator;
import dev.huskuraft.effortless.content.SearchBy;
import dev.huskuraft.effortless.content.SearchTree;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftAdapter;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class MinecraftServerContentCreator extends ContentCreator {

    @Override
    public Buffer allocateButter() {
        return MinecraftAdapter.adapt(new FriendlyByteBuf(Unpooled.buffer()));
    }

    @Override
    public TagRecord emptyTagRecord() {
        return MinecraftAdapter.adapt(new CompoundTag());
    }

    @Override
    public ItemStack emptyItemStack() {
        return MinecraftAdapter.adapt(net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public List<ItemRandomizer> getDefaultRandomizers() {
        return List.of();
    }

    @Override
    public List<Transformer> getDefaultTransformers() {
        return List.of();
    }

    @Override
    public List<Pattern> getDefaultPatterns() {
        return List.of();
    }

    @Override
    public SearchTree<ItemStack> getItemsSearchTree(SearchBy searchBy) {
        return SearchTree.empty();
    }

    @Override
    public <T> SearchTree<T> createSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor) {
        return SearchTree.empty();
    }

    @Override
    public Text empty() {
        return MinecraftAdapter.adapt(Component.empty());
    }

    @Override
    public Text text(String text) {
        return MinecraftAdapter.adapt(Component.literal(text));
    }

    @Override
    public Text text(String text, Text... args) {
        return MinecraftAdapter.adapt(Component.translatable(text, Arrays.stream(args).map(MinecraftAdapter::adapt).toArray(Object[]::new)));
    }

    @Override
    public Text translate(String text) {
        return MinecraftAdapter.adapt(Component.translatable(text));
    }

    @Override
    public Text translate(String text, Text... args) {
        return MinecraftAdapter.adapt(Component.translatable(text, Arrays.stream(args).map(MinecraftAdapter::adapt).toArray(Object[]::new)));
    }

}
