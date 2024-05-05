package dev.huskuraft.effortless.vanilla.tag;

import dev.huskuraft.effortless.api.tag.TagLiteral;
import net.minecraft.nbt.StringTag;

public record MinecraftTagLiteral(StringTag referenceValue) implements TagLiteral {

    @Override
    public byte getId() {
        return new MinecraftTagElement(referenceValue()).getId();
    }

    @Override
    public String getAsString() {
        return new MinecraftTagElement(referenceValue()).getAsString();
    }
}
