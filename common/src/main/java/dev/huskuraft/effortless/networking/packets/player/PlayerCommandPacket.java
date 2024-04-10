package dev.huskuraft.effortless.networking.packets.player;

import java.util.UUID;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.networking.ResponsiblePacket;
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

    public static class Serializer implements BufferSerializer<PlayerCommandPacket> {

        @Override
        public PlayerCommandPacket read(Buffer buffer) {
            return new PlayerCommandPacket(buffer.readUUID(), buffer.readEnum(SingleCommand.class));
        }

        @Override
        public void write(Buffer buffer, PlayerCommandPacket packet) {
            buffer.writeUUID(packet.responseId());
            buffer.writeEnum(packet.action());
        }

    }

}
