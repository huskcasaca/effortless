package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.tag.*;
import dev.huskuraft.effortless.api.text.Text;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MinecraftTagRecord implements TagRecord {

    private final MinecraftTagElement proxy;

    public MinecraftTagRecord(MinecraftTagElement tag) {
        this.proxy = tag;
    }

    public MinecraftTagRecord(CompoundTag tag) {
        this(new MinecraftTagElement(tag == null ? new CompoundTag() : tag));
    }

    @Override
    public CompoundTag referenceValue() {
        return (CompoundTag) proxy.referenceValue();
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
        return referenceValue().getString(key);
    }

    @Override
    public void putString(String key, String value) {
        referenceValue().putString(key, value);
    }

    @Override
    public Text getText(String key) {
        return new MinecraftText(Component.Serializer.fromJson(referenceValue().getString(key)));
    }

    @Override
    public void putText(String key, Text value) {
        referenceValue().putString(key, Component.Serializer.toJson(value.reference()));
    }

    @Override
    public boolean getBoolean(String key) {
        return referenceValue().getBoolean(key);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        referenceValue().putBoolean(key, value);
    }

    @Override
    public byte getByte(String key) {
        return referenceValue().getByte(key);
    }

    @Override
    public void putByte(String key, byte value) {
        referenceValue().putByte(key, value);
    }

    @Override
    public short getShort(String key) {
        return referenceValue().getShort(key);
    }

    @Override
    public void putShort(String key, short value) {
        referenceValue().putShort(key, value);
    }

    @Override
    public int getInt(String key) {
        return referenceValue().getInt(key);
    }

    @Override
    public void putInt(String key, int value) {
        referenceValue().putInt(key, value);
    }

    @Override
    public long getLong(String key) {
        return referenceValue().getLong(key);
    }

    @Override
    public void putLong(String key, long value) {
        referenceValue().putLong(key, value);
    }

    @Override
    public float getFloat(String key) {
        return referenceValue().getFloat(key);
    }

    @Override
    public void putFloat(String key, float value) {
        referenceValue().putFloat(key, value);
    }

    @Override
    public double getDouble(String key) {
        return referenceValue().getDouble(key);
    }

    @Override
    public void putDouble(String key, double value) {
        referenceValue().putDouble(key, value);
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
        return referenceValue().getByteArray(key);
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        referenceValue().putByteArray(key, value);
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
        return referenceValue().getIntArray(key);
    }

    @Override
    public void putIntArray(String key, int[] value) {
        referenceValue().putIntArray(key, value);
    }

    @Override
    public long[] getLongArray(String key) {
        return referenceValue().getLongArray(key);
    }

    @Override
    public void putLongArray(String key, long[] value) {
        referenceValue().putLongArray(key, value);
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
        return new MinecraftTagElement(referenceValue().get(key));
    }

    @Override
    public void putElement(String key, TagElement value) {
        referenceValue().put(key, ((MinecraftTagElement) value).referenceValue());
    }

    @Override
    public <T> T getElement(String key, TagReader<T> reader) {
        return reader.read(new MinecraftTagElement(referenceValue().get(key)));
    }

    @Override
    public <T> void putElement(String key, T value, TagWriter<T> writer) {
        var tagElement = new MinecraftTagElement(null);
        writer.write(tagElement, value);
        referenceValue().put(key, tagElement.referenceValue());
    }

    @Override
    public <T> List<T> getList(String key, TagReader<T> writer) {
        var list = new ArrayList<T>();
        for (var minecraftListTag : (ListTag) referenceValue().get(key)) {
            list.add(writer.read(new MinecraftTagElement(minecraftListTag)));
        }
        return list;
    }

    @Override
    public <T> void putList(String key, Collection<T> collection, TagWriter<T> writer) {
        var minecraftListTag = new ListTag();
        for (var value : collection) {
            var tagElement = new MinecraftTagElement(null);
            writer.write(tagElement, value);
            minecraftListTag.add(tagElement.referenceValue());
        }
        referenceValue().put(key, minecraftListTag);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftTagElement tagElement && referenceValue().equals(tagElement.reference) || obj instanceof MinecraftTagRecord tagRecord && referenceValue().equals(tagRecord.referenceValue());
    }

    @Override
    public int hashCode() {
        return referenceValue().hashCode();
    }

    static class NotImplementedException extends RuntimeException {

        public NotImplementedException(String message) {
            super(message);
        }
    }

}
