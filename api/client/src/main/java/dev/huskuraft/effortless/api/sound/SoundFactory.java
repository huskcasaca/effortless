package dev.huskuraft.effortless.api.sound;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public interface SoundFactory {

    SoundInstance createSimpleSoundInstance(ResourceLocation location,
                                            SoundSource source,
                                            float volume,
                                            float pitch,
                                            boolean looping,
                                            int delay,
                                            SoundInstance.Attenuation attenuation,
                                            double x,
                                            double y,
                                            double z,
                                            boolean relative);

    default SoundInstance createSimpleSoundInstance(Sound sound,
                                                    SoundSource source,
                                                    float volume,
                                                    float pitch,
                                                    boolean looping,
                                                    int delay,
                                                    SoundInstance.Attenuation attenuation,
                                                    double x,
                                                    double y,
                                                    double z,
                                                    boolean relative) {
        return createSimpleSoundInstance(sound.getId(), source, volume, pitch, looping, delay, attenuation, x, y, z, relative);
    }

}
