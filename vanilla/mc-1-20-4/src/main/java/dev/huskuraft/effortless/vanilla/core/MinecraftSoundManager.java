package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.sound.SoundInstance;
import dev.huskuraft.effortless.api.sound.SoundManager;

public class MinecraftSoundManager implements SoundManager {

    private final net.minecraft.client.sounds.SoundManager reference;

    public MinecraftSoundManager(net.minecraft.client.sounds.SoundManager reference) {
        this.reference = reference;
    }

    @Override
    public void play(SoundInstance soundInstance) {
        reference.play(soundInstance.reference());
    }

    @Override
    public void stop(SoundInstance soundInstance) {
        reference.stop(soundInstance.reference());
    }

    @Override
    public void playDelayed(SoundInstance soundInstance, int delay) {
        reference.playDelayed(soundInstance.reference(), delay);
    }

    @Override
    public boolean isActive(SoundInstance soundInstance) {
        return reference.isActive(soundInstance.reference());
    }

    @Override
    public void pause() {
        reference.pause();
    }

    @Override
    public void stop() {
        reference.stop();
    }

    @Override
    public void destroy() {
        reference.destroy();
    }

    @Override
    public void resume() {
        reference.resume();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftSoundManager manager && reference.equals(manager.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
