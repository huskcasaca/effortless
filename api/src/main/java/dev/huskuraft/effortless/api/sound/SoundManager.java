package dev.huskuraft.effortless.api.sound;

public interface SoundManager {

    void play(SoundInstance soundInstance);

    void stop(SoundInstance soundInstance);

    void playDelayed(SoundInstance soundInstance, int delay);

    boolean isActive(SoundInstance soundInstance);

    void pause();

    void stop();

    void destroy();

    void resume();

    default void playButtonClickSound() {
        playButtonClickSound(0.2f);
    }

    default void playButtonClickSound(float volume) {
        play(SoundInstance.createMaster(Sounds.UI_BUTTON_CLICK.sound(), volume, 0.5f));
    }

}
