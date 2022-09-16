package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagRecord;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

class MinecraftTagElement extends TagElement {

    private final Tag tag;

    MinecraftTagElement(Tag tag) {
        this.tag = tag;
    }

    public Tag getRef() {
        return tag;
    }

    @Override
    public TagRecord getAsRecord() {
        return new MinecraftTagRecord((CompoundTag) getRef());
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
