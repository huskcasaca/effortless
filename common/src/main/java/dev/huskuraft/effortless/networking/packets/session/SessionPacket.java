package dev.huskuraft.effortless.networking.packets.session;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
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

    public static class Serializer implements BufferSerializer<SessionPacket> {

        @Override
        public SessionPacket read(Buffer buffer) {
            return new SessionPacket(buffer.read(new SessionSerializer()));
        }

        @Override
        public void write(Buffer buffer, SessionPacket packet) {
            buffer.write(packet.session(), new SessionSerializer());
        }

    }
}
