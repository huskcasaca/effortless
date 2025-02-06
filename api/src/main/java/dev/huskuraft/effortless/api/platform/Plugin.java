package dev.huskuraft.effortless.api.platform;

public interface Plugin {

    String getId();

    void init();

    default boolean isSupported(Platform platform) {
        return platform.findMod(getId()).isPresent();
    }

}
