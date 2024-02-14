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
        play(SoundInstance.createMaster(Sounds.UI_BUTTON_CLICK, 1.0F));
    }

}
