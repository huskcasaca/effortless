package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.Registry;

public interface RegistryFactory {

    static RegistryFactory getInstance() {
        return PlatformLoader.getSingleton();
    }

    default  <T extends PlatformReference> Registry<T> getRegistry(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        var clazz = (Class<T>) typeGetter.getClass().getComponentType();
        var registry = getRegistry(clazz);
        if (registry == null) {
            throw new IllegalArgumentException("Unknown registry: " + clazz.getName());
        }
        return registry;
    }

    <T extends PlatformReference> Registry<T> getRegistry(Class<T> clazz);

}
