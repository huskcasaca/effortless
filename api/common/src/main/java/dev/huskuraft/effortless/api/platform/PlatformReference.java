package dev.huskuraft.effortless.api.platform;

import java.util.function.Supplier;

public interface PlatformReference extends PlatformResource {

    Object referenceValue();

    default <T> T reference() {
        return (T) referenceValue();
    }

    @Override
    default boolean isAvailable() {
        return referenceValue() != null;
    }

    default <T extends PlatformResource> T ifUnavailable(Supplier<T> supplier) {
        return isAvailable() ? (T) this : supplier.get();
    }
}
