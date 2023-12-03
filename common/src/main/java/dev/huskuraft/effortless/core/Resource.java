package dev.huskuraft.effortless.core;

public abstract class Resource {

    public static Resource decompose(String value, String separator) {
        return of(value.split(separator)[0], value.split(separator)[1]);
    }

    public static Resource decompose(String value) {
        return decompose(value, ":");
    }

    public static Resource of(String path) {
        return of("effortless", path);
    }

    public static Resource of(String namespace, String path) {
        return new Resource() {
            @Override
            public String getNamespace() {
                return namespace;
            }

            @Override
            public String getPath() {
                return path;
            }
        };
    }

    public abstract String getNamespace();

    public abstract String getPath();

    @Override
    public String toString() {
        return getNamespace() + ":" + getPath();
    }

}
