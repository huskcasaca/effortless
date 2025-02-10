package dev.huskuraft.effortless.networking.packets.player;

import java.util.UUID;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.universal.api.networking.Packet;
import dev.huskuraft.effortless.building.clipboard.Snapshot;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.SnapshotSerializer;

public record PlayerSnapshotCapturePacket(
        UUID uuid,
        Snapshot snapshot
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerSnapshotCapturePacket> {

        @Override
        public PlayerSnapshotCapturePacket read(NetByteBuf byteBuf) {
            return new PlayerSnapshotCapturePacket(byteBuf.readUUID(), byteBuf.read(new SnapshotSerializer()));
        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerSnapshotCapturePacket packet) {
            byteBuf.writeUUID(packet.uuid());
            byteBuf.write(packet.snapshot(), new SnapshotSerializer());
        }

    }

}
