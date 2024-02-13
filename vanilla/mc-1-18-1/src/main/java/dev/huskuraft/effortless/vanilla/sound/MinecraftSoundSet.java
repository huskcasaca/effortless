package dev.huskuraft.effortless.vanilla.sound;

import dev.huskuraft.effortless.api.sound.Sound;
import dev.huskuraft.effortless.api.sound.SoundSet;
import net.minecraft.world.level.block.SoundType;

public class MinecraftSoundSet implements SoundSet {

    private final SoundType reference;

    public MinecraftSoundSet(SoundType reference) {
        this.reference = reference;
    }

    @Override
    public SoundType referenceValue() {
        return reference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftSoundSet obj1 && reference.equals(obj1.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }


    @Override
    public float volume() {
        return reference.getVolume();
    }

    @Override
    public float pitch() {
        return reference.getPitch();
    }

    @Override
    public Sound breakSound() {
        return new MinecraftSound(reference.getBreakSound());
    }

    @Override
    public Sound stepSound() {
        return new MinecraftSound(reference.getStepSound());
    }

    @Override
    public Sound placeSound() {
        return new MinecraftSound(reference.getPlaceSound());
    }

    @Override
    public Sound hitSound() {
        return new MinecraftSound(reference.getHitSound());
    }

    @Override
    public Sound fallSound() {
        return new MinecraftSound(reference.getFallSound());
    }
}
