package dev.huskuraft.effortless.api.platform;

import java.util.Locale;
import java.util.Optional;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.StatType;
import dev.huskuraft.effortless.api.core.StatTypes;
import dev.huskuraft.effortless.api.core.fluid.Fluid;
import dev.huskuraft.effortless.api.core.fluid.Fluids;
import dev.huskuraft.effortless.api.sound.Sound;
import dev.huskuraft.effortless.api.sound.Sounds;
import dev.huskuraft.effortless.api.tag.InputStreamTagReader;
import dev.huskuraft.effortless.api.tag.OutputStreamTagWriter;
import dev.huskuraft.effortless.api.text.Text;

public interface ContentFactory {

    static ContentFactory getInstance() {
        return PlatformLoader.getSingleton();
    }

    ResourceLocation newResourceLocation(String namespace, String path);

    Optional<Item> newOptionalItem(ResourceLocation location);

    default Item newItem(ResourceLocation location) {
        return newOptionalItem(location).orElseThrow();
    }

    ItemStack newItemStack();

    ItemStack newItemStack(Item item, int count);

    Text newText();

    Text newText(String text);

    Text newTranslatableText(String text);

    Text newTranslatableText(String text, Object... args);

    InputStreamTagReader getInputStreamTagReader();

    OutputStreamTagWriter getOutputStreamTagWriter();

    OperatingSystem getOperatingSystem();

    Sound getSound(Sounds sounds);

    default Optional<Item> getOptionalItem(Items items) {
        return newOptionalItem(ResourceLocation.of("minecraft", items.name().toLowerCase(Locale.ROOT)));
    }

    default Item getItem(Items items) {
        return getOptionalItem(items).orElseThrow();
    }

    Fluid getFluid(Fluids fluids);

    <T extends PlatformReference> StatType<T> getStatType(StatTypes statTypes);

}
