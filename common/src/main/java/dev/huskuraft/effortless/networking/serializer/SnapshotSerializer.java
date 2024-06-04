package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.building.clipboard.Snapshot;

public class SnapshotSerializer implements NetByteBufSerializer<Snapshot> {

    @Override
    public Snapshot read(NetByteBuf byteBuf) {
        return new Snapshot(
                byteBuf.readList(new BlockDataSerializer())
        );
    }

    @Override
    public void write(NetByteBuf byteBuf, Snapshot snapshot) {
        byteBuf.writeList(snapshot.blockData(), new BlockDataSerializer());
    }

}
