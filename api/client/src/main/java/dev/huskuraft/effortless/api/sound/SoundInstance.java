package dev.huskuraft.effortless.api.sound;

import dev.huskuraft.effortless.api.math.Vector3f;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface SoundInstance extends PlatformReference {

   enum Attenuation {
       NONE,
       LINEAR;
   }

    static SoundInstance createMaster(
            Sound sound,
            float volume,
            float pitch
    ) {
        return SoundFactory.getInstance().createSimpleSoundInstance(sound, SoundSource.MASTER, volume, pitch, false, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, true);
    }

    static SoundInstance createMaster(
            Sound sound,
            float pitch
    ) {
        return createMaster(sound, 0.25f, pitch);
    }

    static SoundInstance createMusic(Sound sound) {
        return SoundFactory.getInstance().createSimpleSoundInstance(sound, SoundSource.MUSIC, 1.0F, 1.0F, false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
    }

    static SoundInstance createRecord(Sound sound, Vector3f location) {
        return SoundFactory.getInstance().createSimpleSoundInstance(sound, SoundSource.RECORDS, 4.0F, 1.0F, false, 0, SoundInstance.Attenuation.LINEAR, location.x(), location.y(), location.z(), false);
    }

}
