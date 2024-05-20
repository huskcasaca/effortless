package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagRecord;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public record MinecraftTagRecord(CompoundTag refs) implements TagRecord {

    public static TagRecord ofNullable(CompoundTag compoundTag) {
        return compoundTag == null ? null : new MinecraftTagRecord(compoundTag);
    }

    @Override
    public byte getId() {
        return new MinecraftTagElement(refs).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTagElement(refs).getAsString();
    }

    @Override
    public TagElement getTag(String key) {
        return MinecraftTagElement.ofNullable(refs.get(key));
    }

    @Override
    public TagElement putTag(String key, TagElement value) {
        return MinecraftTagElement.ofNullable(refs.put(key, (Tag) value.refs()));
    }

    @Override
    public void remove(String key) {
        refs.remove(key);
    }
}
