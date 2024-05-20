package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.TagPrimitive;
import net.minecraft.nbt.NumericTag;

public record MinecraftTagPrimitive(NumericTag refs) implements TagPrimitive {

    @Override
    public byte getId() {
        return new MinecraftTagElement(refs).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTagElement(refs).getAsString();
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
