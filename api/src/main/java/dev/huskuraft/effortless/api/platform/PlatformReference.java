package dev.huskuraft.effortless.api.platform;

import java.util.function.Supplier;

public interface PlatformReference {

    Object referenceValue();

    default <T> T reference() {
        return (T) referenceValue();
    }

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
        return referenceValue() != null;
    }

    default <T extends PlatformReference> T ifUnavailable(Supplier<T> supplier) {
        return isAvailable() ? (T) this : supplier.get();
    }

    static <T> T unavailable() {
        return null;
    }
}
