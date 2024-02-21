package dev.huskuraft.effortless.api.sound;

import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.math.Vector3f;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface SoundInstance extends PlatformReference {

    static SoundInstance create(Sound sound,
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
        return SoundFactory.getInstance().createSimpleSoundInstance(sound.getId(), source, volume, pitch, looping, delay, attenuation, x, y, z, relative);
    }

    static SoundInstance createMaster(Sound sound, float volume, float pitch) {
        return create(sound, SoundSource.MASTER, volume, pitch, false, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, true);
    }

    static SoundInstance createMaster(Sound sound, float pitch) {
        return createMaster(sound, 0.25f, pitch);
    }

    static SoundInstance createMusic(Sound sound) {
        return create(sound, SoundSource.MUSIC, 1.0F, 1.0F, false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
    }

    static SoundInstance createRecord(Sound sound, Vector3f location) {
        return create(sound, SoundSource.RECORDS, 4.0F, 1.0F, false, 0, SoundInstance.Attenuation.LINEAR, location.x(), location.y(), location.z(), false);
    }

    static SoundInstance createBlock(Sound sound, float volume, float pitch, Vector3d location) {
        return create(sound, SoundSource.BLOCKS, volume, pitch, false, 0, SoundInstance.Attenuation.LINEAR, location.x(), location.y(), location.z(), false);
    }

    enum Attenuation {
        NONE,
        LINEAR;
    }

}
