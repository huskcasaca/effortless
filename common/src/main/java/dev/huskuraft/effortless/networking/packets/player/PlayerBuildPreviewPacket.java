package dev.huskuraft.effortless.networking.packets.player;

import java.util.UUID;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.ContextSerializer;

public record PlayerBuildPreviewPacket(
        UUID playerId,
        Context context
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerBuildPreviewPacket> {

        @Override
        public PlayerBuildPreviewPacket read(NetByteBuf byteBuf) {
            return new PlayerBuildPreviewPacket(byteBuf.readUUID(), byteBuf.read(new ContextSerializer()));
        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerBuildPreviewPacket packet) {
            byteBuf.writeUUID(packet.playerId());
            byteBuf.write(packet.context(), new ContextSerializer());
        }

    }

}
