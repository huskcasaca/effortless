package dev.huskuraft.effortless.vanilla.platform;

import java.util.Arrays;
import java.util.Optional;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.OperatingSystem;
import dev.huskuraft.effortless.api.sound.Sound;
import dev.huskuraft.effortless.api.sound.Sounds;
import dev.huskuraft.effortless.api.tag.TagIoReader;
import dev.huskuraft.effortless.api.tag.TagIoWriter;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.vanilla.core.MinecraftItem;
import dev.huskuraft.effortless.vanilla.core.MinecraftItemStack;
import dev.huskuraft.effortless.vanilla.core.MinecraftResourceLocation;
import dev.huskuraft.effortless.vanilla.core.MinecraftText;
import dev.huskuraft.effortless.vanilla.networking.MinecraftBuffer;
import dev.huskuraft.effortless.vanilla.sound.MinecraftSound;
import dev.huskuraft.effortless.vanilla.tag.MinecraftTagRecord;
import io.netty.buffer.Unpooled;
import net.minecraft.Util;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

@AutoService(ContentFactory.class)
public class MinecraftContentFactory implements ContentFactory {

    @Override
    public ResourceLocation newResourceLocation(String namespace, String path) {
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
        return DefaultedRegistry.ITEM.getOptional(location.<net.minecraft.resources.ResourceLocation>reference()).map(item -> new MinecraftItem(item));
    }

    @Override
    public ItemStack newItemStack() {
        return new MinecraftItemStack(net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public ItemStack newItemStack(Item item, int count) {
        return new MinecraftItemStack(item.reference(), count);
    }

    @Override
    public ItemStack newItemStack(Item item, int count, TagRecord tag) {
        return new MinecraftItemStack(item.reference(), tag.reference(), count);
    }

    @Override
    public Text newText() {
        return new MinecraftText(Component.empty());
    }

    @Override
    public Text newText(String text) {
        return new MinecraftText(Component.literal(text));
    }

    @Override
    public Text newText(String text, Text... args) {
        return new MinecraftText(Component.translatable(text, Arrays.stream(args).map(text1 -> text1.reference()).toArray(Object[]::new)));
    }

    @Override
    public Text newTranslatableText(String text) {
        return new MinecraftText(Component.translatable(text));
    }

    @Override
    public Text newTranslatableText(String text, Text... args) {
        return new MinecraftText(Component.translatable(text, Arrays.stream(args).map(text1 -> text1.reference()).toArray(Object[]::new)));
    }

    @Override
    public Text newTranslatableText(String text, Object... args) {
        return new MinecraftText(Component.translatable(text, args));
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
    public OperatingSystem getOperatingSystem() {
        return switch (Util.getPlatform()) {
            case LINUX -> OperatingSystem.LINUX;
            case SOLARIS -> OperatingSystem.SOLARIS;
            case WINDOWS -> OperatingSystem.WINDOWS;
            case OSX -> OperatingSystem.MACOS;
            case UNKNOWN -> OperatingSystem.UNKNOWN;
        };
    }

    public Sound getSound(Sounds sounds) {
        var sound = switch (sounds) {
            case UI_BUTTON_CLICK -> SoundEvents.UI_BUTTON_CLICK;
            case UI_TOAST_IN -> SoundEvents.UI_TOAST_IN;
            case UI_TOAST_OUT -> SoundEvents.UI_TOAST_OUT;
        };
        return new MinecraftSound(sound);
    }

}
