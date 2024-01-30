package dev.huskuraft.effortless.vanilla.tag;

import java.util.Objects;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagPrimitive;
import dev.huskuraft.effortless.api.tag.TagRecord;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class MinecraftTagElement implements TagElement {

    protected Tag reference;

    public MinecraftTagElement(Tag reference) {
        this.reference = reference;
    }

    @Override
    public Tag referenceValue() {
        return Objects.requireNonNull(reference);
    }

    public void setReference(Tag tag) {
        this.reference = tag;
    }

    @Override
    public TagRecord asRecord() {
        if (reference == null) {
            this.reference = new CompoundTag();
        }
        return new MinecraftTagRecord(this);
    }

    @Override
    public TagPrimitive asPrimitive() {
        return new MinecraftTagPrimitive(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftTagElement tagElement && reference.equals(tagElement.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
