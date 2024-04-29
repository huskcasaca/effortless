package dev.huskuraft.effortless;

import java.util.List;

import dev.huskuraft.effortless.api.file.FileType;
import dev.huskuraft.effortless.api.file.TagElementFileStorage;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.config.RenderConfig;
import dev.huskuraft.effortless.building.config.TransformerPresets;
import dev.huskuraft.effortless.building.config.tag.RootSettingsTagSerializer;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public final class EffortlessClientTagConfigStorage extends TagElementFileStorage<ClientConfig> {

    public EffortlessClientTagConfigStorage(EffortlessClient entrance) {
        super("effortless.dat", FileType.NBT, new RootSettingsTagSerializer());
    }

    @Override
    public ClientConfig getDefault() {
        return new ClientConfig(
                new RenderConfig(),
                new PatternConfig(
                        Pattern.getPatternPresets()),
                new TransformerPresets(
                        List.of(),
                        List.of(),
                        List.of(),
                        ItemRandomizer.getDefaultItemRandomizers())
        );
    }

}
