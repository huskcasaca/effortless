package dev.huskuraft.effortless.networking.packets.player;

import java.util.UUID;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.universal.api.networking.ResponsiblePacket;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;

public record PlayerPermissionCheckPacket(
        UUID responseId,
        UUID playerId,
        boolean granted
) implements ResponsiblePacket<AllPacketListener> {

    public PlayerPermissionCheckPacket(UUID playerId) {
        this(playerId, true);
    }

    public PlayerPermissionCheckPacket(UUID playerId, boolean granted) {
        this(UUID.randomUUID(), playerId, granted);
    }

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerPermissionCheckPacket> {

        @Override
        public PlayerPermissionCheckPacket read(NetByteBuf byteBuf) {
            return new PlayerPermissionCheckPacket(byteBuf.readUUID(), byteBuf.readUUID(), byteBuf.readBoolean());
        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerPermissionCheckPacket packet) {
            byteBuf.writeUUID(packet.responseId());
            byteBuf.writeUUID(packet.playerId());
            byteBuf.writeBoolean(packet.granted());
        }

    }

}
