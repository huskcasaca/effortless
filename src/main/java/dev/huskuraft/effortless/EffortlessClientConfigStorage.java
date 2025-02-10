package dev.huskuraft.effortless;

import dev.huskuraft.universal.api.file.ConfigFileStorage;
import dev.huskuraft.universal.api.file.FileType;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.universal.ClientConfigConfigSerializer;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public final class EffortlessClientConfigStorage extends ConfigFileStorage<ClientConfig> {

    public static final String CONFIG_NAME = "effortless-client.toml";

    public EffortlessClientConfigStorage(EffortlessClient entrance) {
        super(CONFIG_NAME, FileType.TOML, new ClientConfigConfigSerializer());
    }

    public void setStructure(Structure structure) {
        set(get().withStructure(structure));
    }

    public Structure getStructure(BuildMode buildMode) {
        return get().getStructure(buildMode);
    }

}
