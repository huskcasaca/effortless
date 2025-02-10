package dev.huskuraft.effortless.networking.packets.session;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.universal.api.networking.Packet;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.SessionConfigSerializer;
import dev.huskuraft.effortless.session.config.SessionConfig;

public record SessionConfigPacket(
        SessionConfig sessionConfig
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<SessionConfigPacket> {

        @Override
        public SessionConfigPacket read(NetByteBuf byteBuf) {
            return new SessionConfigPacket(byteBuf.read(new SessionConfigSerializer()));
        }

        @Override
        public void write(NetByteBuf byteBuf, SessionConfigPacket packet) {
            byteBuf.write(packet.sessionConfig(), new SessionConfigSerializer());
        }

    }
}
