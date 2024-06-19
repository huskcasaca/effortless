package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;

public record MinecraftTag(net.minecraft.nbt.Tag refs) implements Tag {

    public static Tag ofNullable(net.minecraft.nbt.Tag refs) {
        if (refs == null) return null;
        if (refs instanceof NumericTag numericTag) return new MinecraftNumericTag(numericTag);
        if (refs instanceof StringTag stringTag) return new MinecraftStringTag(stringTag);
        if (refs instanceof CompoundTag compoundTag) return new MinecraftRecordTag(compoundTag);
        if (refs instanceof ListTag listTag) return new MinecraftListTag(listTag);
        return new MinecraftTag(refs);
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
