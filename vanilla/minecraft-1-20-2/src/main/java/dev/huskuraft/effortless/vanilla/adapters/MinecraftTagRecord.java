package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagReader;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.tag.TagWriter;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class MinecraftTagRecord extends TagRecord {

    private final CompoundTag tag;

    MinecraftTagRecord(CompoundTag tag) {
        this.tag = tag;
    }

    public CompoundTag getRef() {
        return tag;
    }

    @Override
    public TagRecord getAsRecord() {
        return this;
    }

    @Override
    public String getString(String key) {
        return getRef().getString(key);
    }

    @Override
    public void putString(String key, String value) {
        getRef().putString(key, value);
    }

    @Override
    public Text getText(String key) {
        return MinecraftAdapter.adapt(Component.Serializer.fromJson(getRef().getString(key)));
    }

    @Override
    public void putText(String key, Text value) {
        getRef().putString(key, Component.Serializer.toJson(MinecraftAdapter.adapt(value)));
    }

    @Override
    public boolean getBoolean(String key) {
        return getRef().getBoolean(key);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        getRef().putBoolean(key, value);
    }

    @Override
    public byte getByte(String key) {
        return getRef().getByte(key);
    }

    @Override
    public void putByte(String key, byte value) {
        getRef().putByte(key, value);
    }

    @Override
    public short getShort(String key) {
        return getRef().getShort(key);
    }

    @Override
    public void putShort(String key, short value) {
        getRef().putShort(key, value);
    }

    @Override
    public int getInt(String key) {
        return getRef().getInt(key);
    }

    @Override
    public void putInt(String key, int value) {
        getRef().putInt(key, value);
    }

    @Override
    public long getLong(String key) {
        return getRef().getLong(key);
    }

    @Override
    public void putLong(String key, long value) {
        getRef().putLong(key, value);
    }

    @Override
    public float getFloat(String key) {
        return getRef().getFloat(key);
    }

    @Override
    public void putFloat(String key, float value) {
        getRef().putFloat(key, value);
    }

    @Override
    public double getDouble(String key) {
        return getRef().getDouble(key);
    }

    @Override
    public void putDouble(String key, double value) {
        getRef().putDouble(key, value);
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
        return getRef().getByteArray(key);
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        getRef().putByteArray(key, value);
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
        return getRef().getIntArray(key);
    }

    @Override
    public void putIntArray(String key, int[] value) {
        getRef().putIntArray(key, value);
    }

    @Override
    public long[] getLongArray(String key) {
        return getRef().getLongArray(key);
    }

    @Override
    public void putLongArray(String key, long[] value) {
        getRef().putLongArray(key, value);
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
        return MinecraftAdapter.adapt(getRef().get(key));
    }

    @Override
    public void putElement(String key, TagElement value) {
        getRef().put(key, MinecraftAdapter.adapt(value));
    }

    @Override
    public <T> T getElement(String key, TagReader<T> reader) {
        return reader.read(new MinecraftTagElement(getRef().get(key)));
    }

    @Override
    public <T> void putElement(String key, T value, TagWriter<T> writer) {
        var tag = new MinecraftTagElement(new CompoundTag());
        writer.write(tag, value);
        getRef().put(key, tag.getRef());
    }

    @Override
    public <T> List<T> getList(String key, TagReader<T> writer) {
        var list = new ArrayList<T>();
        for (var tag : getRef().getList(key, Tag.TAG_COMPOUND)) {
            list.add(writer.read(new MinecraftTagRecord((CompoundTag) tag)));
        }
        return list;
    }

    @Override
    public <T> void putList(String key, Collection<T> collection, TagWriter<T> writer) {
        var listTag = new ListTag();
        for (var value : collection) {
            var tag = new MinecraftTagRecord(new CompoundTag());
            writer.write(tag, value);
            listTag.add(tag.getRef());
        }
        getRef().put(key, listTag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftTagRecord that)) return false;

        return getRef().equals(that.getRef());
    }

    @Override
    public int hashCode() {
        return getRef().hashCode();
    }

    static class NotImplementedException extends RuntimeException {

        public NotImplementedException(String message) {
            super(message);
        }

    }

}
