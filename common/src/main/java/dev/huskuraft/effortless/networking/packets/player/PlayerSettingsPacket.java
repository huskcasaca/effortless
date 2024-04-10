package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;

public record PlayerSettingsPacket(
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements BufferSerializer<PlayerSettingsPacket> {

        @Override
        public PlayerSettingsPacket read(Buffer buffer) {
            return new PlayerSettingsPacket();
        }

        @Override
        public void write(Buffer buffer, PlayerSettingsPacket packet) {
        }

    }

}
