package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.ContextSerializer;

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
