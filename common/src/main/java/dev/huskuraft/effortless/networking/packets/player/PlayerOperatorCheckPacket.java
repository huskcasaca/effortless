package dev.huskuraft.effortless.networking.packets.player;

import java.util.UUID;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
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

    public static class Serializer implements NetByteBufSerializer<PlayerOperatorCheckPacket> {

        @Override
        public PlayerOperatorCheckPacket read(NetByteBuf byteBuf) {
            return new PlayerOperatorCheckPacket(byteBuf.readUUID(), byteBuf.readUUID(), byteBuf.readBoolean());
        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerOperatorCheckPacket packet) {
            byteBuf.writeUUID(packet.responseId());
            byteBuf.writeUUID(packet.playerId());
            byteBuf.writeBoolean(packet.isOperator());
        }

    }

}
