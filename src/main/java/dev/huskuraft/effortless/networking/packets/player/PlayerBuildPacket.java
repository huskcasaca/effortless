package dev.huskuraft.effortless.networking.packets.player;

import java.util.UUID;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.universal.api.networking.Packet;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.ContextSerializer;

public record PlayerBuildPacket(
        UUID playerId,
        Context context
) implements Packet<AllPacketListener> {

    public static PlayerBuildPacket by(Player player, Context context) {
        return new PlayerBuildPacket(player.getId(), context);
    }

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerBuildPacket> {

        @Override
        public PlayerBuildPacket read(NetByteBuf byteBuf) {
            return new PlayerBuildPacket(byteBuf.readUUID(), byteBuf.read(new ContextSerializer()));
        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerBuildPacket packet) {
            byteBuf.writeUUID(packet.playerId());
            byteBuf.write(packet.context(), new ContextSerializer());
        }

    }

}
