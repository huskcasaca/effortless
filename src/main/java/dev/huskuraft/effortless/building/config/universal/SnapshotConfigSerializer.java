package dev.huskuraft.effortless.building.config.universal;

import dev.huskuraft.effortless.building.clipboard.Snapshot;
import dev.huskuraft.universal.api.config.ConfigSerializer;
import dev.huskuraft.universal.api.nightconfig.core.Config;
import dev.huskuraft.universal.api.nightconfig.core.ConfigSpec;

public class SnapshotConfigSerializer implements ConfigSerializer<Snapshot> {

    public static final SnapshotConfigSerializer INSTANCE = new SnapshotConfigSerializer();

    private SnapshotConfigSerializer() {
    }


    @Override
    public ConfigSpec getSpec(Config config) {
        return new ConfigSpec();
    }

    @Override
    public Snapshot getDefault() {
        return Snapshot.EMPTY;
    }

    @Override
    public Snapshot deserialize(Config config) {
        return Snapshot.EMPTY;
    }

    @Override
    public Config serialize(Snapshot snapshot) {
        return Config.inMemory();
    }
}
