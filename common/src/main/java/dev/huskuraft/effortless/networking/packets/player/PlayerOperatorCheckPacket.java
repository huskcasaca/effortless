package dev.huskuraft.effortless.networking.packets.player;

import java.util.UUID;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.networking.ResponsiblePacket;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;

public record PlayerOperatorCheckPacket(
        UUID responseId,
        UUID playerId,
        boolean isOperator
) implements ResponsiblePacket<AllPacketListener> {

    public PlayerOperatorCheckPacket(UUID playerId) {
        this(playerId, true);
    }

    public PlayerOperatorCheckPacket(UUID playerId, boolean isOp) {
        this(UUID.randomUUID(), playerId, isOp);
    }

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements BufferSerializer<PlayerOperatorCheckPacket> {

        @Override
        public PlayerOperatorCheckPacket read(Buffer buffer) {
            return new PlayerOperatorCheckPacket(buffer.readUUID(), buffer.readUUID(), buffer.readBoolean());
        }

        @Override
        public void write(Buffer buffer, PlayerOperatorCheckPacket packet) {
            buffer.writeUUID(packet.responseId());
            buffer.writeUUID(packet.playerId());
            buffer.writeBoolean(packet.isOperator());
        }

    }

}
