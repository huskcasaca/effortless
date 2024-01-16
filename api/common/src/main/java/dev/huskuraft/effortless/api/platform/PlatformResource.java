package dev.huskuraft.effortless.api.platform;

public interface PlatformResource {

    default PlatformVersion isAvailableSince() {
        return PlatformVersion.UNAVAILABLE;
    }

    default PlatformVersion isAvailableUntil() {
        return PlatformVersion.UNAVAILABLE;
    }

    default boolean isAvailableOn(PlatformVersion version) {
        return true;
    }

    default boolean isAvailable() {
        return true;
    }

    static <T> T unavailable() {
        return null;
    }

}
