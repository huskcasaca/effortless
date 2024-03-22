package dev.huskuraft.effortless.networking.packets.session;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
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

    public static class Serializer implements BufferSerializer<SessionConfigPacket> {

        @Override
        public SessionConfigPacket read(Buffer buffer) {
            return new SessionConfigPacket(buffer.read(new SessionConfigSerializer()));
        }

        @Override
        public void write(Buffer buffer, SessionConfigPacket packet) {
            buffer.write(packet.sessionConfig(), new SessionConfigSerializer());
        }

    }
}
