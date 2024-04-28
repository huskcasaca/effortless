package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface StatType<T extends PlatformReference> extends PlatformReference {

    Stat<T> get(T value);

}
