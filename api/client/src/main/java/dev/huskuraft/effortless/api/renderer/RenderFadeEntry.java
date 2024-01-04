package dev.huskuraft.effortless.api.renderer;

public class RenderFadeEntry<T> {

    private static final int FADE_TICKS = 5;

    private final T value;
    private int ticksTillRemoval;

    public RenderFadeEntry(T value) {
        this.value = value;
        ticksTillRemoval = 1;
    }

    public void tick() {
        ticksTillRemoval--;
    }

    public boolean isAlive() {
        return ticksTillRemoval >= -FADE_TICKS;
    }

    public boolean isFading() {
        return ticksTillRemoval < 0;
    }

    public T getValue() {
        return value;
    }

}
