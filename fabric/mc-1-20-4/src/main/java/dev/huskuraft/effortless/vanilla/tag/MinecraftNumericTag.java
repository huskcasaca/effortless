package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.NumericTag;

public record MinecraftNumericTag(net.minecraft.nbt.NumericTag refs) implements NumericTag {

    @Override
    public byte getId() {
        return new MinecraftTag(refs).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTag(refs).getAsString();
    }

    @Override
    public long getAsLong() {
        return refs.getAsLong();
    }

    @Override
    public int getAsInt() {
        return refs.getAsInt();
    }

    @Override
    public short getAsShort() {
        return refs.getAsShort();
    }

    @Override
    public byte getAsByte() {
        return refs.getAsByte();
    }

    @Override
    public double getAsDouble() {
        return refs.getAsDouble();
    }

    @Override
    public float getAsFloat() {
        return refs.getAsFloat();
    }

    @Override
    public Number getAsNumber() {
        return refs.getAsNumber();
    }

}
