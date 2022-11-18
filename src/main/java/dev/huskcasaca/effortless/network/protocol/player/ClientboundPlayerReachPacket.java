package dev.huskcasaca.effortless.network.protocol.player;

import dev.huskcasaca.effortless.buildreach.ReachSettingsManager.BuildReachSettings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientboundPlayerReachPacket(
        BuildReachSettings reachSettings
) implements Packet<ClientPlayerPacketListener> {

    public ClientboundPlayerReachPacket(FriendlyByteBuf friendlyByteBuf) {
        this(
                new BuildReachSettings(
                        friendlyByteBuf.readInt(),
                        friendlyByteBuf.readInt(),
                        friendlyByteBuf.readInt(),
                        friendlyByteBuf.readBoolean(),
                        friendlyByteBuf.readBoolean(),
                        friendlyByteBuf.readInt()
                )
        );
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(reachSettings.maxReachDistance());
        friendlyByteBuf.writeInt(reachSettings.maxBlockPlacePerAxis());
        friendlyByteBuf.writeInt(reachSettings.maxBlockPlaceAtOnce());
        friendlyByteBuf.writeBoolean(reachSettings.canBreakFar());
        friendlyByteBuf.writeBoolean(reachSettings.enableUndo());
        friendlyByteBuf.writeInt(reachSettings.undoStackSize());
    }

    @Override
    public void handle(ClientPlayerPacketListener packetListener) {
        packetListener.handle(this);
    }
}
