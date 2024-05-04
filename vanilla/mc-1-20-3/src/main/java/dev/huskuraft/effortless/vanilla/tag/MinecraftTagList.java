package dev.huskuraft.effortless.vanilla.tag;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagList;
import net.minecraft.nbt.ListTag;

public record MinecraftTagList(ListTag referenceValue) implements TagList {

    @Override
    public byte getId() {
        return new MinecraftTagElement(referenceValue()).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTagElement(referenceValue()).getAsString();
    }


    @Override
    public boolean addTag(int index, TagElement tag) {
        return referenceValue().addTag(index, tag.reference());
    }

    @Override
    public boolean setTag(int index, TagElement tag) {
        return referenceValue().setTag(index, tag.reference());
    }

    @Override
    public TagElement get(int index) {
        return MinecraftTagElement.ofNullable(referenceValue().get(index));
    }

    @Override
    public int size() {
        return referenceValue().size();
    }

    @Override
    public Stream<TagElement> stream() {
        return referenceValue().stream().map(MinecraftTagElement::ofNullable);
    }
}
