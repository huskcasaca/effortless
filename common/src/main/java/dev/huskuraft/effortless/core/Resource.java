package dev.huskuraft.effortless.core;

import dev.huskuraft.effortless.Effortless;

public abstract class Resource {

    public static Resource decompose(String value, String separator) {
        try {
            return of(value.split(separator)[0], value.split(separator)[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException(e + "for value: " + value);
        }
    }

    public static Resource decompose(String value) {
        return decompose(value, ":");
    }

    public static Resource of(String path) {
        return of("effortless", path);
    }

    public static Resource of(String namespace, String path) {
        return Effortless.getInstance().getPlatform().newResource(namespace, path);
    }

    public abstract String getNamespace();

    public abstract String getPath();

    @Override
    public String toString() {
        return getNamespace() + ":" + getPath();
    }

}
