package dev.huskuraft.effortless.vanilla.sound;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.sound.Sound;
import dev.huskuraft.effortless.vanilla.core.MinecraftResourceLocation;
import net.minecraft.sounds.SoundEvent;

public record MinecraftSound(
    SoundEvent referenceValue
) implements Sound {

    @Override
    public ResourceLocation getId() {
        return new MinecraftResourceLocation(referenceValue().getLocation());
    }
}
