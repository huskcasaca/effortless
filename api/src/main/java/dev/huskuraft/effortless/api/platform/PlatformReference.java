package dev.huskuraft.effortless.api.platform;

import java.util.function.Supplier;

public interface PlatformReference {

    static <T> T unavailable() {
        return null;
    }

    Object refs();

    default <T> T reference() {
        return (T) refs();
    }

//    default PlatformVersion isAvailableSince() {
//        return PlatformVersion.UNAVAILABLE;
//    }
//
//    default PlatformVersion isAvailableUntil() {
//        return PlatformVersion.UNAVAILABLE;
//    }
//
//    default boolean isAvailableOn(PlatformVersion version) {
//        return true;
//    }

    default boolean isAvailable() {
        return refs() != null;
    }

    default <T extends PlatformReference> T ifUnavailable(Supplier<T> supplier) {
        return isAvailable() ? (T) this : supplier.get();
    }

    class PlatformUnsupportedException extends UnsupportedOperationException {

        public PlatformUnsupportedException(String message) {
            super(message);
        }

    }
}
