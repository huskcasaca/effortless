package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.file.FileType;
import dev.huskuraft.effortless.api.file.TagElementFileStorage;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.tag.RootSettingsTagSerializer;

public final class EffortlessClientTagConfigStorage extends TagElementFileStorage<ClientConfig> {

    public EffortlessClientTagConfigStorage(EffortlessClient entrance) {
        super("effortless-client.dat", FileType.NBT, new RootSettingsTagSerializer());
    }

    @Override
    public ClientConfig getDefault() {
        return ClientConfig.getDefault();
    }

}
