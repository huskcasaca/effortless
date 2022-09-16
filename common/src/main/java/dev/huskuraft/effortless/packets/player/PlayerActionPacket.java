package dev.huskuraft.effortless.packets.player;

import dev.huskuraft.effortless.building.structure.SingleAction;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.networking.BufferSerializer;
import dev.huskuraft.effortless.networking.Packet;
import dev.huskuraft.effortless.packets.AllPacketListener;

public record PlayerActionPacket(
        SingleAction action
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer extends BufferSerializer<PlayerActionPacket> {

        @Override
        public PlayerActionPacket read(Buffer buffer) {
            return new PlayerActionPacket(buffer.readEnum(SingleAction.class));
        }

        @Override
        public void write(Buffer buffer, PlayerActionPacket packet) {
            buffer.writeEnum(packet.action);
        }

    }

}
