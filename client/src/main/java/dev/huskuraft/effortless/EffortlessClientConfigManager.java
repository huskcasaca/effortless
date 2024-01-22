package dev.huskuraft.effortless;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.config.*;
import dev.huskuraft.effortless.config.serializer.BaseConfigurationSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public final class EffortlessClientConfigManager extends ClientConfigManager {

    private static final Logger LOGGER = Logger.getLogger("Effortless");
    private static final String CONFIG_NAME = "effortless.dat";
    private final EffortlessClient entrance;
    private BaseConfiguration config;

    public EffortlessClientConfigManager(EffortlessClient entrance) {
        this.entrance = entrance;
    }

    private EffortlessClient getEntrance() {
        return entrance;
    }

    private void readConfig() {
        try {
            var dataDir = getEntrance().getPlatform().getConfigDir().toFile();
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                throw new IOException("Could not create data directory: " + dataDir.getAbsolutePath());
            }
            var configFile = new File(dataDir, CONFIG_NAME);

            if (!configFile.exists()) {
                throw new IOException("Could not find config file: " + configFile.getName());
            }
            var tag = getEntrance().getContentFactory().getTagIoReader().read(new FileInputStream(configFile));

            var read = new BaseConfigurationSerializer().read(tag);
            read.validate();
            this.config = read;
        } catch (Exception e) {
            LOGGER.warning("Cannot read config file");
            e.printStackTrace();
            this.config = getDefault();
        }
        writeConfig();
    }

    private void writeConfig() {
        try {
            var dataDir = getEntrance().getPlatform().getConfigDir().toFile();
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                throw new IOException("Could not create data directory: " + dataDir.getAbsolutePath());
            }
            var configFile = new File(dataDir, CONFIG_NAME);


            var tag = getEntrance().getContentFactory().newTagRecord();
            config.validate();
            new BaseConfigurationSerializer().write(tag, config);
            getEntrance().getContentFactory().getTagIoWriter().write(new FileOutputStream(configFile), tag);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warning("Cannot save config file");
        }
    }

    @Override
    public void editConfig(Consumer<BaseConfiguration> consumer) {
        synchronized (this) {
            consumer.accept(getConfig());
            writeConfig();
        }
    }

    @Override
    public BaseConfiguration getConfig() {
        synchronized (this) {
//            if (config == null) {
            readConfig();
//            }
            return config;
        }
    }

    @Override
    public void saveConfig(BaseConfiguration configuration) {
        synchronized (this) {
            config = configuration;
            writeConfig();
        }
    }

    private BaseConfiguration getDefault() {
        return new BaseConfiguration(
                new PreviewConfiguration(),
                new TransformerConfiguration(
                        List.of(),
                        List.of(),
                        List.of(),
                        ItemRandomizer.getDefaultItemRandomizers()),
                new PatternConfiguration(
                        Pattern.getDefaultPatterns())
        );
    }

}
