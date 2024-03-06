package dev.huskuraft.effortless;

import java.util.List;

import dev.huskuraft.effortless.api.file.FileType;
import dev.huskuraft.effortless.api.file.TagElementFileStorage;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.PreviewConfig;
import dev.huskuraft.effortless.building.config.RootConfig;
import dev.huskuraft.effortless.building.config.TransformerConfig;
import dev.huskuraft.effortless.building.config.serializer.RootConfigSerializer;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public final class EffortlessClientConfigStorage extends TagElementFileStorage<RootConfig> {

    private static final String CONFIG_NAME = "effortless.dat";
    private static final RootConfigSerializer ROOT_CONFIG_SERIALIZER = new RootConfigSerializer();
    private final EffortlessClient entrance;

    public EffortlessClientConfigStorage(EffortlessClient entrance) {
        this.entrance = entrance;
    }

    private EffortlessClient getEntrance() {
        return entrance;
    }

    @Override
    public String getFileName() {
        return CONFIG_NAME;
    }

    @Override
    public RootConfig getDefault() {
        return new RootConfig(
                new PreviewConfig(),
                new TransformerConfig(
                        List.of(),
                        List.of(),
                        List.of(),
                        ItemRandomizer.getDefaultItemRandomizers()),
                new PatternConfig(
                        Pattern.getDefaultPatterns())
        );
    }

    @Override
    public FileType getFileType() {
        return FileType.NBT;
    }

    @Override
    public TagSerializer<RootConfig> getSerializer() {
        return ROOT_CONFIG_SERIALIZER;
    }

}
