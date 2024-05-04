package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagRecord;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public record MinecraftTagRecord(CompoundTag referenceValue) implements TagRecord {

    @Override
    public byte getId() {
        return new MinecraftTagElement(referenceValue()).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTagElement(referenceValue()).getAsString();
    }

    @Override
    public TagElement getTag(String key) {
        return MinecraftTagElement.ofNullable(referenceValue().get(key));
    }

    @Override
    public TagElement putTag(String key, TagElement value) {
        return MinecraftTagElement.ofNullable(referenceValue().put(key, (Tag) value.referenceValue()));
    }

    @Override
    public void remove(String key) {
        referenceValue().remove(key);
    }
}
