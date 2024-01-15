package dev.huskuraft.effortless.api.platform;

import java.util.function.Supplier;

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

    static <T> Supplier<T> unavailable() {
        return () -> null;
    }

}
