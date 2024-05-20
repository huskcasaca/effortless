package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.TagLiteral;
import net.minecraft.nbt.StringTag;

public record MinecraftTagLiteral(StringTag refs) implements TagLiteral {

    @Override
    public byte getId() {
        return new MinecraftTagElement(refs).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTagElement(refs).getAsString();
    }
}
