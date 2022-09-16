package dev.huskuraft.effortless.config;

import java.util.function.Consumer;

public abstract class ConfigManager<C extends Configuration> {

    public void editConfig(Consumer<C> consumer) {
        var config = getConfig();
        consumer.accept(config);
        saveConfig(config);
    }

    public abstract C getConfig();

    public abstract void saveConfig(C configuration);

}
