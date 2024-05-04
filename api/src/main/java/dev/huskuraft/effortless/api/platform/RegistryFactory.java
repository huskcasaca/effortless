package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.Registry;

public interface RegistryFactory {

    static RegistryFactory getInstance() {
        return PlatformLoader.getSingleton();
    }


    <T extends PlatformReference> Registry<T> getRegistry(T... typeGetter);

}
