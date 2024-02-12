package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.sound.Sound;
import net.minecraft.sounds.SoundEvent;

public class MinecraftSound implements Sound {

    private final SoundEvent reference;

    public MinecraftSound(SoundEvent reference) {
        this.reference = reference;
    }

    @Override
    public SoundEvent referenceValue() {
        return reference;
    }

    @Override
    public ResourceLocation getId() {
        return new MinecraftResourceLocation(reference.getLocation());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftSound manager && reference.equals(manager.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
