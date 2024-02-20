package dev.huskuraft.effortless.api.tag;

import java.util.Locale;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public interface TagPrimitive extends TagElement {

    String getString();

    void putString(String value);

    int getInt();

    void putInt(int value);

    double getDouble();

    void putDouble(double value);

    default <T extends Enum<T>> void putEnum(Enum<T> value) {
        var id = ResourceLocation.of("effortless", value.name().toLowerCase(Locale.ROOT));
        putString(id.toString());
    }

    default <T extends Enum<T>> T getEnum(Class<T> clazz) {
        var id = ResourceLocation.decompose(getString());
        return Enum.valueOf(clazz, id.getPath().toUpperCase(Locale.ROOT));
    }

}
