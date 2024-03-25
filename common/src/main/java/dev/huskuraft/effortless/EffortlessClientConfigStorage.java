package dev.huskuraft.effortless;

import java.util.List;

import dev.huskuraft.effortless.api.file.FileType;
import dev.huskuraft.effortless.api.file.TagElementFileStorage;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.config.PatternSettings;
import dev.huskuraft.effortless.building.config.RenderSettings;
import dev.huskuraft.effortless.building.config.RootSettings;
import dev.huskuraft.effortless.building.config.TransformerPresets;
import dev.huskuraft.effortless.building.config.serializer.RootSettingsTagSerializer;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public final class EffortlessClientConfigStorage extends TagElementFileStorage<RootSettings> {

    private static final String CONFIG_NAME = "effortless.dat";
    private static final RootSettingsTagSerializer ROOT_CONFIG_SERIALIZER = new RootSettingsTagSerializer();
    private final EffortlessClient entrance;

    public EffortlessClientConfigStorage(EffortlessClient entrance) {
        this.entrance = entrance;
    }

    @Override
    public String getFileName() {
        return CONFIG_NAME;
    }

    @Override
    public RootSettings getDefault() {
        return new RootSettings(
                new RenderSettings(),
                new PatternSettings(
                        Pattern.getDefaultPatterns()),
                new TransformerPresets(
                        List.of(),
                        List.of(),
                        List.of(),
                        ItemRandomizer.getDefaultItemRandomizers())
        );
    }

    @Override
    public FileType getFileType() {
        return FileType.NBT;
    }

    @Override
    public TagSerializer<RootSettings> getSerializer() {
        return ROOT_CONFIG_SERIALIZER;
    }

}
