package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.tag.TagPrimitive;
import dev.huskuraft.effortless.tag.TagRecord;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class MinecraftTagPrimitive extends TagPrimitive {

    private final MinecraftTagElement proxy;

    MinecraftTagPrimitive(MinecraftTagElement tag) {
        this.proxy = tag;
    }

    private Tag getReference() {
        return proxy.getReference();
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
        return getReference().getAsString();
    }

    @Override
    public void putString(String value) {
        setReference(StringTag.valueOf(value));
    }

    @Override
    public int getInt() {
        return ((IntTag) getReference()).getAsInt();
    }

    @Override
    public void putInt(int value) {
        setReference(IntTag.valueOf(value));
    }

    @Override
    public double getDouble() {
        return ((DoubleTag) getReference()).getAsDouble();
    }

    @Override
    public void putDouble(double value) {
        setReference(DoubleTag.valueOf(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftTagPrimitive that)) return false;

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
