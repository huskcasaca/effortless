package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface ResourceLocation extends PlatformReference {

    static ResourceLocation decompose(String value, String separator) {
        try {
            return of(value.split(separator)[0], value.split(separator)[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException(e + "for value: " + value);
        }
    }

    static ResourceLocation decompose(String value) {
        return decompose(value, ":");
    }

    static ResourceLocation vanilla(String path) {
        return of("minecraft", path);
    }

    static ResourceLocation of(String namespace, String path) {
        return ContentFactory.getInstance().newResourceLocation(namespace, path);
    }

    String getNamespace();

    String getPath();

    default String getString() {
        return getNamespace() + ":" + getPath();
    }

}
