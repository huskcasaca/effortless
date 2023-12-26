package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagPrimitive;
import dev.huskuraft.effortless.tag.TagRecord;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Objects;

public class MinecraftTagElement extends TagElement {

    private Tag tag;

    MinecraftTagElement(Tag tag) {
        this.tag = tag;
    }

    public Tag getRef() {
        return Objects.requireNonNull(tag);
    }

    public void setRef(Tag tag) {
        this.tag = tag;
    }

    @Override
    public TagRecord asRecord() {
        if (tag == null) {
            this.tag = new CompoundTag();
        }
        return new MinecraftTagRecord(this);
    }

    @Override
    public TagPrimitive asPrimitive() {
        return new MinecraftTagPrimitive(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftTagElement that)) return false;

        return getRef().equals(that.getRef());
    }

    @Override
    public int hashCode() {
        return getRef().hashCode();
    }
}
