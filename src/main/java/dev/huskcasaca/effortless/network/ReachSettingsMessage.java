package dev.huskcasaca.effortless.network;

import dev.huskcasaca.effortless.buildconfig.ReachSettingsManager;
import dev.huskcasaca.effortless.buildconfig.ReachSettingsManager.ReachSettings;
import dev.huskcasaca.effortless.buildmode.BuildModeHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public record ReachSettingsMessage(
        ReachSettings reachSettings
) implements Message {

    public static void encode(ReachSettingsMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.reachSettings.maxReachDistance());
        buf.writeInt(message.reachSettings.maxBlockPlacePerAxis());
        buf.writeInt(message.reachSettings.maxBlockPlaceAtOnce());
        buf.writeBoolean(message.reachSettings.canBreakFar());
        buf.writeBoolean(message.reachSettings.enableUndo());
        buf.writeInt(message.reachSettings.undoStackSize());
    }

    public static ReachSettingsMessage decode(FriendlyByteBuf buf) {
        return new ReachSettingsMessage(new ReachSettings(
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readInt()
        ));
    }

    public static class Serializer implements MessageSerializer<ReachSettingsMessage> {

        @Override
        public void encode(ReachSettingsMessage message, FriendlyByteBuf buf) {
            ReachSettingsMessage.encode(message, buf);
        }

        @Override
        public ReachSettingsMessage decode(FriendlyByteBuf buf) {
            return ReachSettingsMessage.decode(buf);
        }

    }

    public static class ServerHandler implements ServerMessageHandler<ReachSettingsMessage> {

        @Override
        public void handleServerSide(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, ReachSettingsMessage message, PacketSender responseSender) {
            server.execute(() -> {
                ReachSettingsManager.setReachSettings(player, ReachSettingsManager.sanitize(message.reachSettings, player));
                BuildModeHandler.initializeMode(player);
            });
        }

    }

    @Environment(EnvType.CLIENT)
    public static class ClientHandler implements ClientMessageHandler<ReachSettingsMessage> {

        @Override
        public void handleClientSide(Minecraft client, LocalPlayer player, ClientPacketListener handler, ReachSettingsMessage message, PacketSender responseSender) {
            client.execute(() -> {
                ReachSettingsManager.setReachSettings(player, ReachSettingsManager.sanitize(message.reachSettings, player));
                BuildModeHandler.initializeMode(player);
            });
        }

    }
}
