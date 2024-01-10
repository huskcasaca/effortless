package dev.huskuraft.effortless.api.platform;

public interface PlatformReference extends PlatformResource {

    Object value();

    default <T> T reference() {
        return (T) value();
    }

}
