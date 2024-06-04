package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.StringTag;

public record MinecraftStringTag(net.minecraft.nbt.StringTag refs) implements StringTag {

    @Override
    public byte getId() {
        return new MinecraftTag(refs).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTag(refs).getAsString();
    }
}
