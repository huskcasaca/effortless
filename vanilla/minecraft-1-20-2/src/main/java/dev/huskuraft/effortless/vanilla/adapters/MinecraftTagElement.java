package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagPrimitive;
import dev.huskuraft.effortless.api.tag.TagRecord;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Objects;

public class MinecraftTagElement implements TagElement {

    protected Tag reference;

    MinecraftTagElement(Tag reference) {
        this.reference = reference;
    }

    public static TagElement toTagElement(Tag tag) {
        if (tag == null) {
            return null;
        }
        return new MinecraftTagElement(tag);
    }

    public static Tag toMinecraft(TagElement tag) {
        if (tag == null) {
            return null;
        }
        return ((MinecraftTagElement) tag).getReference();
    }

    public Tag getReference() {
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
