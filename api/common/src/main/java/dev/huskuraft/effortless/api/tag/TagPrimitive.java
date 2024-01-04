package dev.huskuraft.effortless.api.tag;

import dev.huskuraft.effortless.api.core.Resource;

import java.util.Locale;

public abstract class TagPrimitive extends TagElement {

    public abstract String getString();

    public abstract void putString(String value);

    public abstract int getInt();

    public abstract void putInt(int value);

    public abstract double getDouble();

    public abstract void putDouble(double value);

    public final <T extends Enum<T>> void putEnum(Enum<T> value) {
        var id = Resource.of(value.name().toLowerCase(Locale.ROOT));
        putString(id.toString());
    }

    public final <T extends Enum<T>> T getEnum(Class<T> clazz) {
        var id = Resource.decompose(getString());
        return Enum.valueOf(clazz, id.getPath().toUpperCase(Locale.ROOT));
    }

}
