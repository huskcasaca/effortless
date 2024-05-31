package dev.huskuraft.effortless.api.core;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Registry<T extends PlatformReference> extends Iterable<T>, PlatformReference {

    int DEFAULT = -1;

    int getId(T value);

    @Nullable
    T byId(int key);

    default T byIdOrThrow(int key) {
        T $$1 = this.byId(key);
        if ($$1 == null) {
            throw new IllegalArgumentException("No value with id " + key);
        } else {
            return $$1;
        }
    }

}
