package dev.huskuraft.effortless;

import dev.huskuraft.universal.api.file.FileType;
import dev.huskuraft.universal.api.file.TagElementFileStorage;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.tag.ClientConfigTagSerializer;

public final class EffortlessClientTagConfigStorage extends TagElementFileStorage<ClientConfig> {

    public EffortlessClientTagConfigStorage(EffortlessClient entrance) {
        super("effortless-client.dat", FileType.NBT, new ClientConfigTagSerializer());
    }

    @Override
    public ClientConfig getDefault() {
        return ClientConfig.DEFAULT;
    }

}
