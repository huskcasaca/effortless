package dev.huskuraft.effortless.api.platform;

public interface Plugin {

    String getId();

    void init();

    default boolean isSupported() {
        return Platform.getInstance().findMod(getId()).isPresent();
    }

}
