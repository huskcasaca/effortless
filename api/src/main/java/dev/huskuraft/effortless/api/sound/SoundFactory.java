package dev.huskuraft.effortless.api.sound;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.platform.PlatformServiceLoader;

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

    SoundFactory INSTANCE = PlatformServiceLoader.load(SoundFactory.class).get();

    static SoundFactory getInstance() {
        return INSTANCE;
    }

}
