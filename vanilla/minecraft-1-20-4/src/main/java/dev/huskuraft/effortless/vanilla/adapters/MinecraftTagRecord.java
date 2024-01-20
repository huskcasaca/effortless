package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.text.Text;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MinecraftTagRecord extends TagRecord {

    private final MinecraftTagElement proxy;

    MinecraftTagRecord(MinecraftTagElement tag) {
        this.proxy = tag;
    }

    public static TagRecord fromMinecraft(CompoundTag tag) {
        if (tag == null) {
            return null;
        }
        return new MinecraftTagElement(tag).asRecord();
    }

    public static CompoundTag toMinecraft(TagRecord tag) {
        if (tag == null) {
            return null;
        }
        return ((MinecraftTagRecord) tag).getReference();
    }

    private CompoundTag getReference() {
        return (CompoundTag) proxy.getReference();
    }

    @Override
    public TagRecord asRecord() {
        return this;
    }

    @Override
    public TagPrimitive asPrimitive() {
        return new MinecraftTagPrimitive(proxy);
    }

    @Override
    public String getString(String key) {
        return getReference().getString(key);
    }

    @Override
    public void putString(String key, String value) {
        getReference().putString(key, value);
    }

    @Override
    public Text getText(String key) {
        return MinecraftText.fromMinecraftText(Component.Serializer.fromJson(getReference().getString(key)));
    }

    @Override
    public void putText(String key, Text value) {
        getReference().putString(key, Component.Serializer.toJson(MinecraftText.toMinecraftText(value)));
    }

    @Override
    public boolean getBoolean(String key) {
        return getReference().getBoolean(key);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        getReference().putBoolean(key, value);
    }

    @Override
    public byte getByte(String key) {
        return getReference().getByte(key);
    }

    @Override
    public void putByte(String key, byte value) {
        getReference().putByte(key, value);
    }

    @Override
    public short getShort(String key) {
        return getReference().getShort(key);
    }

    @Override
    public void putShort(String key, short value) {
        getReference().putShort(key, value);
    }

    @Override
    public int getInt(String key) {
        return getReference().getInt(key);
    }

    @Override
    public void putInt(String key, int value) {
        getReference().putInt(key, value);
    }

    @Override
    public long getLong(String key) {
        return getReference().getLong(key);
    }

    @Override
    public void putLong(String key, long value) {
        getReference().putLong(key, value);
    }

    @Override
    public float getFloat(String key) {
        return getReference().getFloat(key);
    }

    @Override
    public void putFloat(String key, float value) {
        getReference().putFloat(key, value);
    }

    @Override
    public double getDouble(String key) {
        return getReference().getDouble(key);
    }

    @Override
    public void putDouble(String key, double value) {
        getReference().putDouble(key, value);
    }

    @Override
    public boolean[] getBooleanArray(String key) {
        throw new NotImplementedException("getBooleanArray is not implemented yet");
    }

    @Override
    public void putBooleanArray(String key, boolean[] value) {
        throw new NotImplementedException("putBooleanArray is not implemented yet");
    }

    @Override
    public byte[] getByteArray(String key) {
        return getReference().getByteArray(key);
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        getReference().putByteArray(key, value);
    }

    @Override
    public short[] getShortArray(String key) {
        throw new NotImplementedException("getShortArray is not implemented yet");
    }

    @Override
    public void putShortArray(String key, short[] value) {
        throw new NotImplementedException("putShortArray is not implemented yet");
    }

    @Override
    public int[] getIntArray(String key) {
        return getReference().getIntArray(key);
    }

    @Override
    public void putIntArray(String key, int[] value) {
        getReference().putIntArray(key, value);
    }

    @Override
    public long[] getLongArray(String key) {
        return getReference().getLongArray(key);
    }

    @Override
    public void putLongArray(String key, long[] value) {
        getReference().putLongArray(key, value);
    }

    @Override
    public float[] getFloatArray(String key) {
        throw new NotImplementedException("getFloatArray is not implemented yet");
    }

    @Override
    public void putFloatArray(String key, float[] value) {
        throw new NotImplementedException("putFloatArray is not implemented yet");
    }

    @Override
    public double[] getDoubleArray(String key) {
        throw new NotImplementedException("getDoubleArray is not implemented yet");
    }

    @Override
    public void putDoubleArray(String key, double[] value) {
        throw new NotImplementedException("putDoubleArray is not implemented yet");
    }

    @Override
    public TagElement getElement(String key) {
        return MinecraftTagElement.toTagElement(getReference().get(key));
    }

    @Override
    public void putElement(String key, TagElement value) {
        getReference().put(key, MinecraftTagElement.toMinecraft(value));
    }

    @Override
    public <T> T getElement(String key, TagReader<T> reader) {
        return reader.read(new MinecraftTagElement(getReference().get(key)));
    }

    @Override
    public <T> void putElement(String key, T value, TagWriter<T> writer) {
        var tag = new MinecraftTagElement(null);
        writer.write(tag, value);
        getReference().put(key, tag.getReference());
    }

    @Override
    public <T> List<T> getList(String key, TagReader<T> writer) {
        var list = new ArrayList<T>();
        for (var tag : (ListTag) getReference().get(key)) {
            list.add(writer.read(new MinecraftTagElement(tag)));
        }
        return list;
    }

    @Override
    public <T> void putList(String key, Collection<T> collection, TagWriter<T> writer) {
        var listTag = new ListTag();
        for (var value : collection) {
            var tag = new MinecraftTagElement(null);
            writer.write(tag, value);
            listTag.add(tag.getReference());
        }
        getReference().put(key, listTag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftTagRecord that)) return false;

        return getReference().equals(that.getReference());
    }

    @Override
    public int hashCode() {
        return getReference().hashCode();
    }

    static class NotImplementedException extends RuntimeException {

        public NotImplementedException(String message) {
            super(message);
        }

    }

}
