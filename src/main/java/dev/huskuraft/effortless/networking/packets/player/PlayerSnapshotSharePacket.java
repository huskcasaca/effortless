package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.universal.api.networking.Packet;
import dev.huskuraft.effortless.building.clipboard.Snapshot;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.SnapshotSerializer;

import java.util.UUID;

public record PlayerSnapshotSharePacket(
        UUID from,
        UUID to,
        Snapshot snapshot
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerSnapshotSharePacket> {

        @Override
        public PlayerSnapshotSharePacket read(NetByteBuf byteBuf) {
            return new PlayerSnapshotSharePacket(byteBuf.readUUID(), byteBuf.readUUID(), byteBuf.read(new SnapshotSerializer()));
        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerSnapshotSharePacket packet) {
            byteBuf.writeUUID(packet.from());
            byteBuf.writeUUID(packet.to());
            byteBuf.write(packet.snapshot(), new SnapshotSerializer());
        }

    }

}
