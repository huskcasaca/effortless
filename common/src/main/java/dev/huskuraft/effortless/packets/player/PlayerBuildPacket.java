package dev.huskuraft.effortless.packets.player;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.networking.BufferSerializer;
import dev.huskuraft.effortless.networking.Packet;
import dev.huskuraft.effortless.networking.serializer.ContextSerializer;
import dev.huskuraft.effortless.packets.AllPacketListener;

public record PlayerBuildPacket(
        Context context
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer extends BufferSerializer<PlayerBuildPacket> {

        @Override
        public PlayerBuildPacket read(Buffer buffer) {
            return new PlayerBuildPacket(buffer.read(new ContextSerializer()));
        }

        @Override
        public void write(Buffer buffer, PlayerBuildPacket packet) {
            buffer.write(packet.context, new ContextSerializer());
        }

    }
}
