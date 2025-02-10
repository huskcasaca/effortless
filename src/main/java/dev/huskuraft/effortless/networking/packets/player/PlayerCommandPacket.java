package dev.huskuraft.effortless.networking.packets.player;

import java.util.UUID;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.universal.api.networking.ResponsiblePacket;
import dev.huskuraft.effortless.building.SingleCommand;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;

public record PlayerCommandPacket(
        UUID responseId,
        SingleCommand action
) implements ResponsiblePacket<AllPacketListener> {

    public PlayerCommandPacket(
            SingleCommand action
    ) {
        this(UUID.randomUUID(), action);
    }

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerCommandPacket> {

        @Override
        public PlayerCommandPacket read(NetByteBuf byteBuf) {
            return new PlayerCommandPacket(byteBuf.readUUID(), byteBuf.readEnum(SingleCommand.class));
        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerCommandPacket packet) {
            byteBuf.writeUUID(packet.responseId());
            byteBuf.writeEnum(packet.action());
        }

    }

}
