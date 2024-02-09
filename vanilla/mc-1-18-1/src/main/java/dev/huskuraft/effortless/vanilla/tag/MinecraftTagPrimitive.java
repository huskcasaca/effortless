package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.TagPrimitive;
import dev.huskuraft.effortless.api.tag.TagRecord;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class MinecraftTagPrimitive implements TagPrimitive {

    private final MinecraftTagElement proxy;

    public MinecraftTagPrimitive(MinecraftTagElement tag) {
        this.proxy = tag;
    }

    @Override
    public Tag referenceValue() {
        return proxy.referenceValue();
    }

    private void setReference(Tag tag) {
        proxy.setReference(tag);
    }

    @Override
    public TagRecord asRecord() {
        return new MinecraftTagRecord(proxy);
    }

    @Override
    public TagPrimitive asPrimitive() {
        return this;
    }

    @Override
    public String getString() {
        return referenceValue().getAsString();
    }

    @Override
    public void putString(String value) {
        setReference(StringTag.valueOf(value));
    }

    @Override
    public int getInt() {
        return ((IntTag) referenceValue()).getAsInt();
    }

    @Override
    public void putInt(int value) {
        setReference(IntTag.valueOf(value));
    }

    @Override
    public double getDouble() {
        return ((DoubleTag) referenceValue()).getAsDouble();
    }

    @Override
    public void putDouble(double value) {
        setReference(DoubleTag.valueOf(value));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftTagElement tagElement && referenceValue().equals(tagElement.reference) || obj instanceof MinecraftTagPrimitive tagPrimitive && referenceValue().equals(tagPrimitive.referenceValue());
    }

    @Override
    public int hashCode() {
        return referenceValue().hashCode();
    }

}
