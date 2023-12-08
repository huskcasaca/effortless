package dev.huskuraft.effortless.tag;

import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class TagRecord extends TagElement {

    public <T extends Enum<T>> T getEnum(String key, Class<T> clazz) {
        var id = Resource.decompose(getString(key));
        return Enum.valueOf(clazz, id.getPath().toUpperCase(Locale.ROOT));
    }

    public abstract String getString(String key);

    public abstract Text getText(String key);

    public Resource getResource(String key) {
        return Resource.decompose(getString(key));
    };

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

    public final Item getItem(String key) {
        return Item.fromId(getResource(key));
    }

    public final ItemStack getItemStack(String key) {
        return getElement(key, () -> (tag1) -> ItemStack.of(
                tag1.getAsRecord().getItem("Item"),
                tag1.getAsRecord().getInt("Count"),
                tag1.getAsRecord().getElement("Tag").getAsRecord()
        ));
    };

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

    public void putResource(String key, Resource value) {
        putString(key, value.toString());
    };

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

    public final void putItem(String key, Item value) {
        putResource(key, value.getId());
    }

    public final void putItemStack(String key, ItemStack value) {
        putElement(key, value, () -> (tag1, itemStack) -> {
            tag1.getAsRecord().putItem("Item", itemStack.getItem());
            tag1.getAsRecord().putInt("Count", itemStack.getStackSize());
            tag1.getAsRecord().putElement("Tag", itemStack.getTag());
        });
    };

    public abstract void putElement(String key, TagElement value);

    public abstract <T> void putElement(String key, T value, Supplier<TagWriter<T>> supplier);

    public abstract <T> void put(T value, Supplier<TagWriter<T>> supplier);

    public abstract <T> void putList(String key, Collection<T> collection, Supplier<TagWriter<T>> supplier);

    public UUID getUUID(String key) {
        return UUID.fromString(getString(key));
    }

    public void putUUID(String key, UUID value) {
        putString(key, value.toString());
    }

}
