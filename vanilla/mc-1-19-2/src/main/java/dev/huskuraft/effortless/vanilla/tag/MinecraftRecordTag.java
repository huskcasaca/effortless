package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.Tag;
import dev.huskuraft.effortless.api.tag.RecordTag;
import net.minecraft.nbt.CompoundTag;

public record MinecraftRecordTag(CompoundTag refs) implements RecordTag {

    public static RecordTag ofNullable(CompoundTag compoundTag) {
        return compoundTag == null ? null : new MinecraftRecordTag(compoundTag);
    }

    @Override
    public byte getId() {
        return new MinecraftTag(refs).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTag(refs).getAsString();
    }

    @Override
    public Tag getTag(String key) {
        return MinecraftTag.ofNullable(refs.get(key));
    }

    @Override
    public Tag putTag(String key, Tag value) {
        return MinecraftTag.ofNullable(refs.put(key, (net.minecraft.nbt.Tag) value.refs()));
    }

    @Override
    public void remove(String key) {
        refs.remove(key);
    }
}
