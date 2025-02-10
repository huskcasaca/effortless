package dev.huskuraft.effortless.networking.packets.session;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.universal.api.networking.Packet;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.SessionSerializer;
import dev.huskuraft.effortless.session.Session;

public record SessionPacket(
        Session session
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<SessionPacket> {

        @Override
        public SessionPacket read(NetByteBuf byteBuf) {
            return new SessionPacket(byteBuf.read(new SessionSerializer()));
        }

        @Override
        public void write(NetByteBuf byteBuf, SessionPacket packet) {
            byteBuf.write(packet.session(), new SessionSerializer());
        }

    }
}
