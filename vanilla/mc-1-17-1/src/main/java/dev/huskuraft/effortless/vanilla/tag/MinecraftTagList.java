package dev.huskuraft.effortless.vanilla.tag;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagList;
import net.minecraft.nbt.ListTag;

public record MinecraftTagList(ListTag refs) implements TagList {

    @Override
    public byte getId() {
        return new MinecraftTagElement(refs).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTagElement(refs).getAsString();
    }


    @Override
    public boolean addTag(int index, TagElement tag) {
        return refs.addTag(index, tag.reference());
    }

    @Override
    public boolean setTag(int index, TagElement tag) {
        return refs.setTag(index, tag.reference());
    }

    @Override
    public TagElement getTag(int index) {
        return MinecraftTagElement.ofNullable(refs.get(index));
    }

    @Override
    public int size() {
        return refs.size();
    }

    @Override
    public Stream<TagElement> stream() {
        return refs.stream().map(MinecraftTagElement::ofNullable);
    }
}
