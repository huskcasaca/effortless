package dev.huskuraft.effortless.vanilla.platform;

import java.util.Optional;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.Stat;
import dev.huskuraft.effortless.api.core.StatType;
import dev.huskuraft.effortless.api.core.StatTypes;
import dev.huskuraft.effortless.api.core.fluid.Fluid;
import dev.huskuraft.effortless.api.core.fluid.Fluids;
import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.OperatingSystem;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.sound.Sound;
import dev.huskuraft.effortless.api.sound.Sounds;
import dev.huskuraft.effortless.api.tag.InputStreamTagReader;
import dev.huskuraft.effortless.api.tag.OutputStreamTagWriter;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.vanilla.core.MinecraftFluid;
import dev.huskuraft.effortless.vanilla.core.MinecraftItem;
import dev.huskuraft.effortless.vanilla.core.MinecraftItemStack;
import dev.huskuraft.effortless.vanilla.core.MinecraftResourceLocation;
import dev.huskuraft.effortless.vanilla.core.MinecraftText;
import dev.huskuraft.effortless.vanilla.sound.MinecraftSound;
import dev.huskuraft.effortless.vanilla.tag.MinecraftRecordTag;
import net.minecraft.Util;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;

@AutoService(ContentFactory.class)
public class MinecraftContentFactory implements ContentFactory {

    @Override
    public ResourceLocation newResourceLocation(String namespace, String path) {
        return new MinecraftResourceLocation(new net.minecraft.resources.ResourceLocation(namespace, path));
    }

    @Override
    public Optional<Item> newOptionalItem(ResourceLocation location) {
        return DefaultedRegistry.ITEM.getOptional(location.<net.minecraft.resources.ResourceLocation>reference()).map(MinecraftItem::ofNullable);
    }

    @Override
    public ItemStack newItemStack() {
        return new MinecraftItemStack(net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public ItemStack newItemStack(Item item, int count) {
        return new MinecraftItemStack(new net.minecraft.world.item.ItemStack((net.minecraft.world.item.Item) item.reference(), count));
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
    public Text newTranslatableText(String text) {
        return new MinecraftText(Component.translatable(text));
    }

    @Override
    public Text newTranslatableText(String text, Object... args) {
        return new MinecraftText(Component.translatable(text, args));
    }

    @Override
    public InputStreamTagReader getInputStreamTagReader() {
        return input -> new MinecraftRecordTag(NbtIo.readCompressed(input));
    }

    @Override
    public OutputStreamTagWriter getOutputStreamTagWriter() {
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

    @Override
    public Sound getSound(Sounds sounds) {
        var sound = switch (sounds) {
            case UI_BUTTON_CLICK -> SoundEvents.UI_BUTTON_CLICK;
            case UI_TOAST_IN -> SoundEvents.UI_TOAST_IN;
            case UI_TOAST_OUT -> SoundEvents.UI_TOAST_OUT;
        };
        return new MinecraftSound(sound);
    }

    @Override
    public Fluid getFluid(Fluids fluids) {
        var fluid = switch (fluids) {
            case EMPTY -> net.minecraft.world.level.material.Fluids.EMPTY;
            case FLOWING_WATER -> net.minecraft.world.level.material.Fluids.FLOWING_WATER;
            case WATER -> net.minecraft.world.level.material.Fluids.WATER;
            case FLOWING_LAVA -> net.minecraft.world.level.material.Fluids.FLOWING_LAVA;
            case LAVA -> net.minecraft.world.level.material.Fluids.LAVA;
        };
        return new MinecraftFluid(fluid);
    }

    @Override
    public <T extends PlatformReference> StatType<T> getStatType(StatTypes statTypes) {
        var statType = switch (statTypes) {
            case ITEM_USED -> Stats.ITEM_USED;
            case ITEM_BROKEN -> Stats.ITEM_BROKEN;
            case ITEM_PICKED_UP -> Stats.ITEM_PICKED_UP;
            case ITEM_DROPPED -> Stats.ITEM_DROPPED;
        };
        return new StatType<T>() {
            @Override
            public Stat<T> get(T value) {
                return () -> statType.get(value.reference());
            }

            @Override
            public Object refs() {
                return statType;
            }
        };
    }
}
