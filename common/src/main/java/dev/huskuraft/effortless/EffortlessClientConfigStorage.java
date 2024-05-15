package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.file.ConfigFileStorage;
import dev.huskuraft.effortless.api.file.FileType;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.universal.ClientConfigConfigSerializer;
import dev.huskuraft.effortless.building.structure.builder.BuildStructure;

public final class EffortlessClientConfigStorage extends ConfigFileStorage<ClientConfig> {

    private static final String CONFIG_NAME = "effortless-client.toml";

    public EffortlessClientConfigStorage(EffortlessClient entrance) {
        super(CONFIG_NAME, FileType.TOML, new ClientConfigConfigSerializer());
    }


    public void setSelectedBuildStructure(BuildStructure buildStructure) {
        set(get().withBuildStructure(buildStructure));
    }

    public BuildStructure getSelectedBuildStructure() {
        return get().buildStructure();
    }

}
