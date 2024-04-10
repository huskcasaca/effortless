package dev.huskuraft.effortless.networking.packets.player;

import java.util.UUID;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.ContextSerializer;

public record PlayerBuildPreviewPacket(
        UUID playerId,
        Context context
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements BufferSerializer<PlayerBuildPreviewPacket> {

        @Override
        public PlayerBuildPreviewPacket read(Buffer buffer) {
            return new PlayerBuildPreviewPacket(buffer.readUUID(), buffer.read(new ContextSerializer()));
        }

        @Override
        public void write(Buffer buffer, PlayerBuildPreviewPacket packet) {
            buffer.writeUUID(packet.playerId());
            buffer.write(packet.context(), new ContextSerializer());
        }

    }

}
