package dev.huskuraft.effortless.vanilla.sound;

import dev.huskuraft.effortless.api.sound.Sound;
import dev.huskuraft.effortless.api.sound.SoundSet;
import net.minecraft.world.level.block.SoundType;

public record MinecraftSoundSet(
        SoundType referenceValue
) implements SoundSet {


    @Override
    public float volume() {
        return referenceValue().getVolume();
    }

    @Override
    public float pitch() {
        return referenceValue().getPitch();
    }

    @Override
    public Sound breakSound() {
        return new MinecraftSound(referenceValue().getBreakSound());
    }

    @Override
    public Sound stepSound() {
        return new MinecraftSound(referenceValue().getStepSound());
    }

    @Override
    public Sound placeSound() {
        return new MinecraftSound(referenceValue().getPlaceSound());
    }

    @Override
    public Sound hitSound() {
        return new MinecraftSound(referenceValue().getHitSound());
    }

    @Override
    public Sound fallSound() {
        return new MinecraftSound(referenceValue().getFallSound());
    }
}
