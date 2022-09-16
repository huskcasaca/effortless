package dev.huskuraft.effortless.tag;

import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public abstract class TagRecord extends TagElement {

    public <T extends Enum<T>> T getEnum(String key, Class<T> clazz) {
        var id = Resource.of(getString(key));
        return Enum.valueOf(clazz, id.getPath().toUpperCase(Locale.ROOT));
    }

    public abstract String getString(String key);

    public abstract Text getText(String key);

    public abstract boolean getBoolean(String key);

    public abstract byte getByte(String key);

    public abstract short getShort(String key);

    public abstract int getInt(String key);

    public abstract long getLong(String key);

    public abstract float getFloat(String key);

    public abstract double getDouble(String key);

    public abstract boolean[] getBooleanArray(String key);

    public abstract byte[] getByteArray(String key);

    public abstract short[] getShortArray(String key);

    public abstract int[] getIntArray(String key);

    public abstract long[] getLongArray(String key);

    public abstract float[] getFloatArray(String key);

    public abstract double[] getDoubleArray(String key);

    public abstract ItemStack getItemStack(String key);

    public abstract TagElement getElement(String key);

    public abstract <T> T getElement(String key, Supplier<TagReader<T>> supplier);

    public abstract <T> T get(Supplier<TagReader<T>> supplier);

    public abstract <T> List<T> getList(String key, Supplier<TagReader<T>> supplier);

    public <T extends Enum<T>> void putEnum(String key, Enum<T> value) {
        var id = Resource.of(value.name().toLowerCase(Locale.ROOT));
        putString(key, id.toString());
    }

    public abstract void putString(String key, String value);

    public abstract void putText(String key, Text value);

    public abstract void putBoolean(String key, boolean value);

    public abstract void putByte(String key, byte value);

    public abstract void putShort(String key, short value);

    public abstract void putInt(String key, int value);

    public abstract void putLong(String key, long value);

    public abstract void putFloat(String key, float value);

    public abstract void putDouble(String key, double value);

    public abstract void putBooleanArray(String key, boolean[] value);

    public abstract void putByteArray(String key, byte[] value);

    public abstract void putShortArray(String key, short[] value);

    public abstract void putIntArray(String key, int[] value);

    public abstract void putLongArray(String key, long[] value);

    public abstract void putFloatArray(String key, float[] value);

    public abstract void putDoubleArray(String key, double[] value);

    public abstract void putItemStack(String key, ItemStack value);

    public abstract void putElement(String key, TagElement value);

    public abstract <T> void putElement(String key, T value, Supplier<TagWriter<T>> supplier);

    public abstract <T> void put(T value, Supplier<TagWriter<T>> supplier);

    public abstract <T> void putList(String key, Collection<T> collection, Supplier<TagWriter<T>> supplier);

}
