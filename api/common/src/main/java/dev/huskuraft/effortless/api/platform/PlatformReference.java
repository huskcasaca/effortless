package dev.huskuraft.effortless.api.platform;

public interface PlatformReference extends PlatformResource {

    Object referenceValue();

    default <T> T reference() {
        return (T) referenceValue();
    }

}
