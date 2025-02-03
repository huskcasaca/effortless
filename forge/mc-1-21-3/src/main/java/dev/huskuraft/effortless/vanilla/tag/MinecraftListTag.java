package dev.huskuraft.effortless.vanilla.tag;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.tag.ListTag;
import dev.huskuraft.effortless.api.tag.Tag;

public record MinecraftListTag(net.minecraft.nbt.ListTag refs) implements ListTag {

    @Override
    public byte getId() {
        return new MinecraftTag(refs).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTag(refs).getAsString();
    }


    @Override
    public boolean addTag(int index, Tag tag) {
        return refs.addTag(index, tag.reference());
    }

    @Override
    public boolean setTag(int index, Tag tag) {
        return refs.setTag(index, tag.reference());
    }

    @Override
    public Tag getTag(int index) {
        return MinecraftTag.ofNullable(refs.get(index));
    }

    @Override
    public int size() {
        return refs.size();
    }

    @Override
    public Stream<Tag> stream() {
        return refs.stream().map(MinecraftTag::ofNullable);
    }
}
