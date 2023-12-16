package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.tag.TagPrimitive;
import dev.huskuraft.effortless.tag.TagRecord;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

class MinecraftTagPrimitive extends TagPrimitive {

    private final MinecraftTagElement proxy;

    MinecraftTagPrimitive(MinecraftTagElement proxy) {
        this.proxy = proxy;
    }

    public Tag getRef() {
        return proxy.getRef();
    }

    public void setRef(Tag tag) {
        proxy.setRef(tag);
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
        return getRef().getAsString();
    }

    @Override
    public void putString(String value) {
        setRef(StringTag.valueOf(value));
    }

    @Override
    public int getInt() {
        return ((IntTag) getRef()).getAsInt();
    }

    @Override
    public void putInt(int value) {
        setRef(IntTag.valueOf(value));
    }

    @Override
    public double getDouble() {
        return ((DoubleTag) getRef()).getAsDouble();
    }

    @Override
    public void putDouble(double value) {
        setRef(DoubleTag.valueOf(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftTagPrimitive that)) return false;

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
