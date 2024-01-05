package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.building.SingleCommand;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;

public record PlayerCommandPacket(
        SingleCommand action
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements BufferSerializer<PlayerCommandPacket> {

        @Override
        public PlayerCommandPacket read(Buffer buffer) {
            return new PlayerCommandPacket(buffer.readEnum(SingleCommand.class));
        }

        @Override
        public void write(Buffer buffer, PlayerCommandPacket packet) {
            buffer.writeEnum(packet.action);
        }

    }

}
