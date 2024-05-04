package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.TagPrimitive;
import net.minecraft.nbt.NumericTag;

public record MinecraftTagPrimitive(NumericTag referenceValue) implements TagPrimitive {

    @Override
    public byte getId() {
        return new MinecraftTagElement(referenceValue()).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTagElement(referenceValue()).getAsString();
    }

    @Override
    public long getAsLong() {
        return referenceValue().getAsLong();
    }

    @Override
    public int getAsInt() {
        return referenceValue().getAsInt();
    }

    @Override
    public short getAsShort() {
        return referenceValue().getAsShort();
    }

    @Override
    public byte getAsByte() {
        return referenceValue().getAsByte();
    }

    @Override
    public double getAsDouble() {
        return referenceValue().getAsDouble();
    }

    @Override
    public float getAsFloat() {
        return referenceValue().getAsFloat();
    }

    @Override
    public Number getAsNumber() {
        return referenceValue().getAsNumber();
    }

}
