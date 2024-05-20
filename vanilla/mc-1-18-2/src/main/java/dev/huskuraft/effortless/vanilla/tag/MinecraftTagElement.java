package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.TagElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public record MinecraftTagElement(Tag refs) implements TagElement {

    public static TagElement ofNullable(Tag tag) {
        if (tag == null) return null;
        if (tag instanceof NumericTag numericTag) return new MinecraftTagPrimitive(numericTag);
        if (tag instanceof StringTag stringTag) return new MinecraftTagLiteral(stringTag);
        if (tag instanceof CompoundTag compoundTag) return new MinecraftTagRecord(compoundTag);
        if (tag instanceof ListTag listTag) return new MinecraftTagList(listTag);

        return new MinecraftTagElement(tag);
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
