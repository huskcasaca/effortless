package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.building.settings.DimensionSettings;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;

public record PlayerSettingsPacket(
        DimensionSettings dimensionSettings
) implements Packet<AllPacketListener> {

    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements BufferSerializer<PlayerSettingsPacket> {

        @Override
        public PlayerSettingsPacket read(Buffer buffer) {
            return new PlayerSettingsPacket(
                    new DimensionSettings(
                            buffer.readInt(),
                            buffer.readInt(),
                            buffer.readInt(),
                            buffer.readBoolean(),
                            buffer.readBoolean(),
                            buffer.readInt()
                    )
            );
        }

        @Override
        public void write(Buffer buffer, PlayerSettingsPacket packet) {
            buffer.writeInt(packet.dimensionSettings().maxReachDistance());
            buffer.writeInt(packet.dimensionSettings().maxBlockPlacePerAxis());
            buffer.writeInt(packet.dimensionSettings().maxBlockPlaceAtOnce());
            buffer.writeBoolean(packet.dimensionSettings().canBreakFar());
            buffer.writeBoolean(packet.dimensionSettings().enableUndoRedo());
            buffer.writeInt(packet.dimensionSettings().undoStackSize());
        }

    }

}
