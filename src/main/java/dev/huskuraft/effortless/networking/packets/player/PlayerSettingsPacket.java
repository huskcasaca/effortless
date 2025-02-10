package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.universal.api.networking.Packet;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;

public record PlayerSettingsPacket(
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerSettingsPacket> {

        @Override
        public PlayerSettingsPacket read(NetByteBuf byteBuf) {
            return new PlayerSettingsPacket();
        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerSettingsPacket packet) {
        }

    }

}
