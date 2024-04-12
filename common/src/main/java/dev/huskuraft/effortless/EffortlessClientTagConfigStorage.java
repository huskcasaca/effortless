package dev.huskuraft.effortless;

import java.util.List;

import dev.huskuraft.effortless.api.file.FileType;
import dev.huskuraft.effortless.api.file.TagElementFileStorage;
import dev.huskuraft.effortless.building.config.PatternSettings;
import dev.huskuraft.effortless.building.config.RenderSettings;
import dev.huskuraft.effortless.building.config.RootSettings;
import dev.huskuraft.effortless.building.config.TransformerPresets;
import dev.huskuraft.effortless.building.config.tag.RootSettingsTagSerializer;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public final class EffortlessClientTagConfigStorage extends TagElementFileStorage<RootSettings> {

    public EffortlessClientTagConfigStorage(EffortlessClient entrance) {
        super("effortless.dat", FileType.NBT, new RootSettingsTagSerializer());
    }

    @Override
    public RootSettings getDefault() {
        return new RootSettings(
                new RenderSettings(),
                new PatternSettings(
                        Pattern.getPatternPresets()),
                new TransformerPresets(
                        List.of(),
                        List.of(),
                        List.of(),
                        ItemRandomizer.getDefaultItemRandomizers())
        );
    }

}
