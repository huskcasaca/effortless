package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.building.clipboard.Snapshot;

public class SnapshotSerializer implements NetByteBufSerializer<Snapshot> {

    @Override
    public Snapshot read(NetByteBuf byteBuf) {
        return new Snapshot(
                byteBuf.readString(),
                byteBuf.readLong(),
                byteBuf.readList(new BlockDataSerializer())
        );
    }

    @Override
    public void write(NetByteBuf byteBuf, Snapshot snapshot) {
        byteBuf.writeString(snapshot.name());
        byteBuf.writeLong(snapshot.createdTimestamp());
        byteBuf.writeList(snapshot.blockData(), new BlockDataSerializer());
    }

}
