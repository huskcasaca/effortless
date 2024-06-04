package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;

public record MinecraftTag(net.minecraft.nbt.Tag refs) implements Tag {

    public static Tag ofNullable(net.minecraft.nbt.Tag tag) {
        if (tag == null) return null;
        if (tag instanceof NumericTag numericTag) return new MinecraftNumericTag(numericTag);
        if (tag instanceof StringTag stringTag) return new MinecraftStringTag(stringTag);
        if (tag instanceof CompoundTag compoundTag) return new MinecraftRecordTag(compoundTag);
        if (tag instanceof ListTag listTag) return new MinecraftListTag(listTag);

        return new MinecraftTag(tag);
    }

    @Override
    public byte getId() {
        return refs.getId();
    }

    @Override
    public String getAsString() {
        return refs.getAsString();
    }
}
